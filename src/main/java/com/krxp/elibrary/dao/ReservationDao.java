package com.krxp.elibrary.dao;

import com.krxp.elibrary.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationDao extends JpaRepository<Reservation, Long> {

    Reservation findByBookId(Long bookId);

    List<Reservation> findByUserId(Long userId);
}
