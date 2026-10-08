package com.roomrental.service;

import com.roomrental.dto.BookingRequest;
import com.roomrental.entity.Booking;
import com.roomrental.entity.BookingStatus;
import com.roomrental.entity.Room;
import com.roomrental.entity.Tenant;
import com.roomrental.exception.ResourceNotFoundException;
import com.roomrental.repository.BookingRepository;
import com.roomrental.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final RoomService roomService;
    private final TenantService tenantService;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository,
                          RoomService roomService, TenantService tenantService) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.roomService = roomService;
        this.tenantService = tenantService;
    }

    @Transactional
    public Booking book(BookingRequest request) {
        Room room = roomService.findById(request.getRoomId());

        if (!room.isAvailable()) {
            throw new IllegalArgumentException("This room is not available right now");
        }
        if (request.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Move-in date cannot be in the past");
        }
        if (!request.getEndDate().isAfter(request.getStartDate())) {
            throw new IllegalArgumentException("Move-out date must be after the move-in date");
        }
        if (bookingRepository.countOverlapping(room.getId(), BookingStatus.ACTIVE,
                request.getStartDate(), request.getEndDate()) > 0) {
            throw new IllegalArgumentException("This room is already booked for those dates");
        }

        Tenant tenant = tenantService.findOrCreate(
                request.getTenantName(), request.getTenantEmail(), request.getTenantPhone());

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setTenant(tenant);
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setStatus(BookingStatus.ACTIVE);
        Booking saved = bookingRepository.save(booking);

        room.setAvailable(false);
        roomRepository.save(room);
        return saved;
    }

    @Transactional
    public Booking cancel(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking " + bookingId + " not found"));
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return booking;
        }
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        Room room = booking.getRoom();
        if (bookingRepository.countByRoomIdAndStatus(room.getId(), BookingStatus.ACTIVE) == 0) {
            room.setAvailable(true);
            roomRepository.save(room);
        }
        return booking;
    }

    public List<Booking> findByOwner(Long ownerId) {
        return bookingRepository.findByRoomOwnerIdOrderByCreatedAtDesc(ownerId);
    }

    public List<Booking> findByTenantEmail(String email) {
        return bookingRepository.findByTenantEmailIgnoreCaseOrderByCreatedAtDesc(email);
    }
}
