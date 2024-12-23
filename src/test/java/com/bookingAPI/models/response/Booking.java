package com.bookingAPI.models.response;

import com.bookingAPI.models.request.BookingDates;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
public class Booking{
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