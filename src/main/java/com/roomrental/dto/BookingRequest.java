package com.roomrental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookingRequest {

    @NotNull(message = "Room is required")
    private Long roomId;

    @NotBlank(message = "Your name is required")
    private String tenantName;

    @NotBlank(message = "Your email is required")
    @Email(message = "Enter a valid email")
    private String tenantEmail;

    @NotBlank(message = "Your phone number is required")
    private String tenantPhone;

    @NotNull(message = "Move-in date is required")
    private LocalDate startDate;

    @NotNull(message = "Move-out date is required")
    private LocalDate endDate;
}
