package com.example.calendarBackEnd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record Kpi(
        long record,
        long daysBetween,
        @JsonFormat(pattern = "yyyy-MM-dd",shape = JsonFormat.Shape.STRING)
        LocalDate lastAccident
) {
}
