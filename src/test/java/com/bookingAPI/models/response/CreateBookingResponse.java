package com.bookingAPI.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateBookingResponse{
	private Booking booking;
	@JsonProperty("bookingid")
	private int bookingId;
}