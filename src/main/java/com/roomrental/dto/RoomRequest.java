package com.roomrental.dto;

import com.roomrental.entity.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RoomRequest {

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Rent is required")
    @Positive(message = "Rent must be greater than zero")
    private BigDecimal rent;

    @NotNull(message = "Room type is required")
    private RoomType roomType;

    private String description;

    private Boolean available;

    /** Required when creating a room; ignored on update. */
    private Long ownerId;
}
