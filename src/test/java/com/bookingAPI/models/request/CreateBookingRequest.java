package com.bookingAPI.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateBookingRequest{
	@JsonProperty("firstname")
	private String firstName;
	@JsonProperty("lastname")
	private String lastName;
	@JsonProperty("totalprice")
	private int totalPrice;
	@JsonProperty("depositpaid")
	private boolean depositPaid;
	@JsonProperty("bookingdates")
	private BookingDates bookingDates;
	@JsonProperty("additionalneeds")
	private String additionalNeeds;







}