package com.bookingAPI.tests;

import com.bookingAPI.models.request.BookingDates;
import com.bookingAPI.models.request.CreateBookingRequest;
import com.bookingAPI.models.response.CreateBookingResponse;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

public class ResponseParsing {
    RequestSpecification requestSpecification = given().baseUri("https://restful-booker.herokuapp.com/").
                                                       filters(new RequestLoggingFilter(),new ResponseLoggingFilter()).
                                                       contentType(ContentType.JSON);
    public CreateBookingRequest createBookingPayloadPojo(String firstName, String lastName, String additionalNeeds) {
        CreateBookingRequest createBookingRequest = new CreateBookingRequest();
        BookingDates bookingDates = new BookingDates();
        bookingDates.setCheckIn("2025-01-01");
        bookingDates.setCheckOut("2025-02-01");
        createBookingRequest.setFirstName(firstName);
        createBookingRequest.setLastName(lastName);
        createBookingRequest.setTotalPrice(250);
        createBookingRequest.setDepositPaid(true);
        createBookingRequest.setBookingDates(bookingDates);
        createBookingRequest.setAdditionalNeeds(additionalNeeds);
        return createBookingRequest;
    }
    @Test
    public void testCreateBookingAPIUsingPojo(){
        CreateBookingRequest createBookingRequestPayload = createBookingPayloadPojo("Selenium","Automation","Playwright");

        CreateBookingResponse createBookingResponse = given().spec(requestSpecification).basePath("booking").
                                                body(createBookingRequestPayload).
                                                when().post().
                                                then().assertThat().statusCode(200).and().
                                                body("bookingid", Matchers.is(Matchers.notNullValue())).
                                                extract().response().as(CreateBookingResponse.class);

        int bookingId = createBookingResponse.getBookingId();
        assertThat(bookingId,Matchers.is(Matchers.notNullValue()));
        assertThat(createBookingResponse.getBooking().getAdditionalNeeds(),Matchers.equalTo("Playwright"));
        System.out.println("Booking id :  " + bookingId);

        //Update Call
        CreateBookingRequest updatedPayload = createBookingPayloadPojo("Rest Assured","Automation","Postman");
        Response updateBookingResponse = given().spec(requestSpecification).basePath("booking/{bookingId}").
                                                body(updatedPayload).
                                                pathParam("bookingId",bookingId).
                                                auth().preemptive().basic("admin","password123").
                                                when().put().
                                                then().assertThat().statusCode(200).
                                                extract().response();
    }
}
