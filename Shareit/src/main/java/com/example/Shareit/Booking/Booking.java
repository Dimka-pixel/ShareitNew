package com.example.Shareit.Booking;

import com.example.Shareit.Item.Item;
import com.example.Shareit.User.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
@Builder
public class Booking {

    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "start_date")
    @NotNull
    private LocalDateTime start;
    @Column(name = "end_date")
    @NotNull
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "booker_id")
    @NotNull
    private User booker;
    @ManyToOne
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
