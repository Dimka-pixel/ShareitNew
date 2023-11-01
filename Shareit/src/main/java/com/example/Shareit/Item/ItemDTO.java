package com.example.Shareit.Item;

import com.example.Shareit.Booking.BookingDTO;
import com.example.Shareit.Booking.BookingView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO {

    private int id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private BookingDTO lastBooking;
    private BookingDTO nextBooking;
    private List <CommentDTO> comments;



}
