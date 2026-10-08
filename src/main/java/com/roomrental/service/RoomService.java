package com.roomrental.service;

import com.roomrental.dto.RoomRequest;
import com.roomrental.entity.Owner;
import com.roomrental.entity.Room;
import com.roomrental.entity.RoomType;
import com.roomrental.exception.ResourceNotFoundException;
import com.roomrental.repository.RoomRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final OwnerService ownerService;

    public RoomService(RoomRepository roomRepository, OwnerService ownerService) {
        this.roomRepository = roomRepository;
        this.ownerService = ownerService;
    }

    /** One query for "available rooms", "search" and "my rooms". Every filter is optional. */
    @Transactional(readOnly = true)
    public List<Room> search(String location, BigDecimal minRent, BigDecimal maxRent,
                             RoomType roomType, boolean availableOnly, Long ownerId) {
        Specification<Room> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (availableOnly) {
                predicates.add(cb.isTrue(root.get("available")));
            }
            if (location != null && !location.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("location")),
                        "%" + location.trim().toLowerCase() + "%"));
            }
            if (minRent != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("rent"), minRent));
            }
            if (maxRent != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("rent"), maxRent));
            }
            if (roomType != null) {
                predicates.add(cb.equal(root.get("roomType"), roomType));
            }
            if (ownerId != null) {
                predicates.add(cb.equal(root.get("owner").get("id"), ownerId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return roomRepository.findAll(spec, Sort.by(Sort.Direction.ASC, "rent"));
    }

    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room " + id + " not found"));
    }

    @Transactional
    public Room create(RoomRequest request) {
        if (request.getOwnerId() == null) {
            throw new IllegalArgumentException("Owner is required");
        }
        Owner owner = ownerService.findById(request.getOwnerId());
        Room room = new Room();
        room.setOwner(owner);
        apply(room, request);
        return roomRepository.save(room);
    }

    @Transactional
    public Room update(Long id, RoomRequest request) {
        Room room = findById(id);
        apply(room, request);
        return roomRepository.save(room);
    }

    @Transactional
    public Room setAvailability(Long id, boolean available) {
        Room room = findById(id);
        room.setAvailable(available);
        return roomRepository.save(room);
    }

    @Transactional
    public void delete(Long id) {
        roomRepository.delete(findById(id));
        roomRepository.flush();
    }

    private void apply(Room room, RoomRequest request) {
        room.setRoomNumber(request.getRoomNumber().trim());
        room.setLocation(request.getLocation().trim());
        room.setRent(request.getRent());
        room.setRoomType(request.getRoomType());
        room.setDescription(request.getDescription() == null ? null : request.getDescription().trim());
        if (request.getAvailable() != null) {
            room.setAvailable(request.getAvailable());
        }
    }
}
