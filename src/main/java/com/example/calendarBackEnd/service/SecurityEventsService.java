package com.example.calendarBackEnd.service;
import com.example.calendarBackEnd.dto.Kpi;
import com.example.calendarBackEnd.entity.SecurityEventsEntity;
import java.util.List;
public interface SecurityEventsService {

    List<SecurityEventsEntity> get(int month);

    SecurityEventsEntity save(SecurityEventsEntity securityEvent);

    SecurityEventsEntity update(SecurityEventsEntity securityEvent, Long ids);

    Kpi reccordAndAccidentFree();

}

