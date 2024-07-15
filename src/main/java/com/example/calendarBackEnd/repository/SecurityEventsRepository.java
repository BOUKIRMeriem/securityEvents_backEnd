package com.example.calendarBackEnd.repository;

import com.example.calendarBackEnd.entity.SecurityEventsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SecurityEventsRepository extends JpaRepository<SecurityEventsEntity, Long> {

    @Query( value = "select event from SecurityEventsEntity event where month(event.localDate) = :month")
    List<SecurityEventsEntity> getEventsByMonth(@Param("month") int month);
    @Query("select event from SecurityEventsEntity event where event.eventType = 'red'")
    List<SecurityEventsEntity> getEventsByColorRed();


}
