package com.roomrental.controller;

import com.roomrental.dto.RoomRequest;
import com.roomrental.entity.Room;
import com.roomrental.entity.RoomType;
import com.roomrental.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * GET /api/rooms                                   -> all available rooms
     * GET /api/rooms?location=&minRent=&maxRent=&roomType=  -> search
     * GET /api/rooms?ownerId=1&availableOnly=false     -> everything one owner has listed
     */
    @GetMapping
    public List<Room> list(@RequestParam(required = false) String location,
                           @RequestParam(required = false) BigDecimal minRent,
                           @RequestParam(required = false) BigDecimal maxRent,
                           @RequestParam(required = false) RoomType roomType,
                           @RequestParam(defaultValue = "true") boolean availableOnly,
                           @RequestParam(required = false) Long ownerId) {
        return roomService.search(location, minRent, maxRent, roomType, availableOnly, ownerId);
    }

    @GetMapping("/{id}")
    public Room one(@PathVariable Long id) {
        return roomService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Room create(@Valid @RequestBody RoomRequest request) {
        return roomService.create(request);
    }

    @PutMapping("/{id}")
    public Room update(@PathVariable Long id, @Valid @RequestBody RoomRequest request) {
        return roomService.update(id, request);
    }

    @PatchMapping("/{id}/availability")
    public Room availability(@PathVariable Long id, @RequestParam boolean available) {
        return roomService.setAvailability(id, available);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        roomService.delete(id);
    }
}
