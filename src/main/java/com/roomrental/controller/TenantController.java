package com.roomrental.controller;

import com.roomrental.entity.Tenant;
import com.roomrental.service.TenantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping
    public List<Tenant> all() {
        return tenantService.findAll();
    }

    @GetMapping("/{id}")
    public Tenant one(@PathVariable Long id) {
        return tenantService.findById(id);
    }
}
