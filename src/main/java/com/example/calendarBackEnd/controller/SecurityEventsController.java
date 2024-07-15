package com.example.calendarBackEnd.controller;

import com.example.calendarBackEnd.dto.Kpi;
import com.example.calendarBackEnd.entity.SecurityEventsEntity;
import com.example.calendarBackEnd.service.SecurityEventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/events")
@CrossOrigin("*")
public class SecurityEventsController {
    @Autowired
    private SecurityEventsService securityEventsService;

    @PostMapping("/add")
    public SecurityEventsEntity createEvent(@RequestBody SecurityEventsEntity event) {
        return securityEventsService.save(event);
    }
    @PutMapping("/update/{ids}")
    public  SecurityEventsEntity update( @RequestBody SecurityEventsEntity securityEvent,@PathVariable  Long ids){
        return securityEventsService.update(securityEvent, ids);
    }
    @GetMapping("/month/{month}")
    List<SecurityEventsEntity> get(@PathVariable(name = "month") int month){
        return securityEventsService.get(month);
    }
    @GetMapping("/kpi")
        public Kpi getDaysSinceLastRedEvent() {
        return securityEventsService.reccordAndAccidentFree();
    }

}
