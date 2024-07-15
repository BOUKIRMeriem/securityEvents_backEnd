package com.example.calendarBackEnd.service.serviceImp;
import com.example.calendarBackEnd.dto.Kpi;
import com.example.calendarBackEnd.entity.SecurityEventsEntity;
import com.example.calendarBackEnd.repository.SecurityEventsRepository;
import com.example.calendarBackEnd.service.SecurityEventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.time.temporal.ChronoUnit;

@Service
public class SecurityEventsServiceImp implements SecurityEventsService {
    @Autowired
    private SecurityEventsRepository securityEventsRepository;
    @Override
    public SecurityEventsEntity save(SecurityEventsEntity securityEvent) {
        return securityEventsRepository.save(securityEvent);
    }
    @Override
    public SecurityEventsEntity update(SecurityEventsEntity securityEvent, Long ids) {
        SecurityEventsEntity existingEvent = securityEventsRepository.findById(ids)
                .orElseThrow(() -> new IllegalArgumentException("Event with ID " + ids + " not found"));
        existingEvent.setLocalDate(securityEvent.getLocalDate());
        existingEvent.setEventType(securityEvent.getEventType());
         securityEventsRepository.save(existingEvent);
        return existingEvent;
    }

    @Override
    public Kpi reccordAndAccidentFree() {
        long daysBetween = accidentFree();
        long record = record();
        LocalDate lastAccident= lastAccident();
        return new Kpi( record,  daysBetween, lastAccident);
    }

    @Override
    public List<SecurityEventsEntity> get(int month) {
        return getEventsDataFilteredByJavaCode(month);
    }

    private List<SecurityEventsEntity> getEventsDataFilteredByJavaCode(int month){
        List<SecurityEventsEntity> events = securityEventsRepository.findAll();
        return events.stream().filter(event -> (event.getLocalDate().getMonth() + 1) == month).collect(Collectors.toList());
    }

    private List<SecurityEventsEntity> getEventsDataFilteredBySQLQuery(int month){
        return securityEventsRepository.getEventsByMonth(month);
    }
   private List<SecurityEventsEntity> getEventByColor(){
        return securityEventsRepository.getEventsByColorRed();
   }

   private  long accidentFree(){
       List<SecurityEventsEntity> eventList = getEventByColor();
       if (!eventList.isEmpty()) {
       List<SecurityEventsEntity> sortedList = eventList.stream()
               .sorted((e1, e2) -> e2.getLocalDate().compareTo(e1.getLocalDate()))
               .collect(Collectors.toList());
         Date  firstEventDate = sortedList.get(0).getLocalDate();
           // Convertir Date en LocalDate
           LocalDate firstEventLocalDate = Instant.ofEpochMilli(firstEventDate.getTime())
                   .atZone(ZoneId.systemDefault())
                   .toLocalDate();
           LocalDate todayLocalDate =  LocalDate.now();
           // Calculer la différence en jours
           return ChronoUnit.DAYS.between(firstEventLocalDate, todayLocalDate);
       }else{
           return 0;
       }
   }


    private Long record() {
        List<SecurityEventsEntity> eventList = getEventByColor();
        if (!eventList.isEmpty()) {
            List<SecurityEventsEntity> sortedList = eventList.stream()
                    .sorted((e1, e2) -> e1.getLocalDate().compareTo(e2.getLocalDate()))
                    .collect(Collectors.toList());
            long maxPeriod = 0;
            long max = 0;
            long max1 = 0;
            LocalDate today = LocalDate.now();
            LocalDate beginningOfYear = LocalDate.of(today.getYear(), 1, 1);
            LocalDate firstDate = sortedList.get(0).getLocalDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate lastDate = sortedList.get(sortedList.size() - 1).getLocalDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            max = ChronoUnit.DAYS.between(beginningOfYear, firstDate);
            max1 = ChronoUnit.DAYS.between(lastDate, today);

            for (int i = 0; i < sortedList.size() - 1; i++) {
                LocalDate dateA = sortedList.get(i).getLocalDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate dateB = sortedList.get(i + 1).getLocalDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                long diffInDays = ChronoUnit.DAYS.between(dateA, dateB);

                if (diffInDays > maxPeriod) {
                    maxPeriod = diffInDays - 1;
                }
            }
            return Math.max(Math.max(max, max1), maxPeriod);
        }else{
            LocalDate today = LocalDate.now();
            LocalDate beginningOfYear = LocalDate.of(today.getYear(), 1, 1);
            return ChronoUnit.DAYS.between(beginningOfYear, today) +1;
        }
    }
   private LocalDate  lastAccident() {
       List<SecurityEventsEntity> eventList = getEventByColor();
       if (!eventList.isEmpty()) {
           List<SecurityEventsEntity> sortedList = eventList.stream()
                   .sorted((e1, e2) -> e2.getLocalDate().compareTo(e1.getLocalDate()))
                   .collect(Collectors.toList());

       return sortedList.get(0).getLocalDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
       }
       return  null;
   }


}
