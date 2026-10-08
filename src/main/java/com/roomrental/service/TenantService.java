package com.roomrental.service;

import com.roomrental.entity.Tenant;
import com.roomrental.exception.ResourceNotFoundException;
import com.roomrental.repository.TenantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;

    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public List<Tenant> findAll() {
        return tenantRepository.findAll();
    }

    public Tenant findById(Long id) {
        return tenantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant " + id + " not found"));
    }

    /** Reuses the tenant with the same email, otherwise creates a new one. */
    public Tenant findOrCreate(String name, String email, String phone) {
        return tenantRepository.findByEmailIgnoreCase(email.trim()).map(existing -> {
            existing.setName(name.trim());
            existing.setPhone(phone.trim());
            return tenantRepository.save(existing);
        }).orElseGet(() -> {
            Tenant tenant = new Tenant();
            tenant.setName(name.trim());
            tenant.setEmail(email.trim());
            tenant.setPhone(phone.trim());
            return tenantRepository.save(tenant);
        });
    }
}
