package com.roomrental.controller;

import com.roomrental.entity.Owner;
import com.roomrental.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public List<Owner> all() {
        return ownerService.findAll();
    }

    @GetMapping("/{id}")
    public Owner one(@PathVariable Long id) {
        return ownerService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Owner create(@Valid @RequestBody Owner owner) {
        return ownerService.create(owner);
    }
}
