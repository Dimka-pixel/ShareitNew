package com.example.Shareit.Booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDto {

    private int id;
    @NotNull
    @FutureOrPresent
    private LocalDateTime start;
    @Future
    @NotNull
    private LocalDateTime end;
    private int bookerId;
    @NotNull
    private int itemId;
    private String ItemName;
    private Status status;

}
