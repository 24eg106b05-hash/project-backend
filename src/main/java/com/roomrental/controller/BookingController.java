package com.roomrental.controller;

import com.roomrental.dto.BookingRequest;
import com.roomrental.entity.Booking;
import com.roomrental.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /** GET /api/bookings?ownerId=1  or  GET /api/bookings?tenantEmail=a@b.com */
    @GetMapping
    public List<Booking> list(@RequestParam(required = false) Long ownerId,
                              @RequestParam(required = false) String tenantEmail) {
        if (ownerId != null) {
            return bookingService.findByOwner(ownerId);
        }
        if (tenantEmail != null && !tenantEmail.isBlank()) {
            return bookingService.findByTenantEmail(tenantEmail);
        }
        throw new IllegalArgumentException("Provide ownerId or tenantEmail");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Booking book(@Valid @RequestBody BookingRequest request) {
        return bookingService.book(request);
    }

    @PatchMapping("/{id}/cancel")
    public Booking cancel(@PathVariable Long id) {
        return bookingService.cancel(id);
    }
}
