package com.roomrental.service;

import com.roomrental.entity.Owner;
import com.roomrental.exception.ResourceNotFoundException;
import com.roomrental.repository.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    public Owner findById(Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner " + id + " not found"));
    }

    public Owner create(Owner owner) {
        if (ownerRepository.existsByEmailIgnoreCase(owner.getEmail())) {
            throw new IllegalArgumentException("An owner with this email already exists");
        }
        owner.setId(null);
        return ownerRepository.save(owner);
    }
}
