package com.roomrental.repository;

import com.roomrental.entity.Booking;
import com.roomrental.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select count(b) from Booking b where b.room.id = :roomId and b.status = :status "
            + "and b.startDate <= :endDate and b.endDate >= :startDate")
    long countOverlapping(@Param("roomId") Long roomId,
                          @Param("status") BookingStatus status,
                          @Param("startDate") LocalDate startDate,
                          @Param("endDate") LocalDate endDate);

    long countByRoomIdAndStatus(Long roomId, BookingStatus status);

    List<Booking> findByRoomOwnerIdOrderByCreatedAtDesc(Long ownerId);

    List<Booking> findByTenantEmailIgnoreCaseOrderByCreatedAtDesc(String email);
}
