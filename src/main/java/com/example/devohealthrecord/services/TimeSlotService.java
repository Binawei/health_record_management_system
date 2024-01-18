package com.example.devohealthrecord.services;
import com.example.devohealthrecord.entities.TimeSlot;
import com.example.devohealthrecord.repository.TimeSlotRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;

    @PostConstruct
    public void init() {
        List<TimeSlot> timeSlots = Arrays.asList(
                new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 0)),
                new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0)),
                new TimeSlot(LocalTime.of(2, 0), LocalTime.of(3, 0))
        );

        timeSlotRepository.saveAll(timeSlots);
    }
}
