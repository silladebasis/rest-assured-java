package com.bookingAPI.tests;

import com.bookingAPI.models.request.BookingDates;
import com.bookingAPI.models.request.CreateBookingRequest;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class SimplePostApi {
    RequestSpecification requestSpecification = given().baseUri("https://restful-booker.herokuapp.com/").
                                                       filters(new RequestLoggingFilter(),new ResponseLoggingFilter()).
                                                       contentType(ContentType.JSON);

    public Map<String, Object> createBookingPayload(String firstName, String lastName, String additionalNeeds) {
        Map<String,Object> requestPayload = new HashMap<>();
        Map<String,Object> bookingDatesMap = new HashMap<>();
        bookingDatesMap.put("checkin","2025-01-01");
        bookingDatesMap.put("checkout","2025-02-01");
        requestPayload.put("firstname",firstName);
        requestPayload.put("lastname",lastName);
        requestPayload.put("totalprice",120);
        requestPayload.put("depositpaid",true);
        requestPayload.put("bookingdates",bookingDatesMap);
        requestPayload.put("additionalneeds",additionalNeeds);
        return requestPayload;
    }
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

    //Using Map as request body
    @Test
    public void testCreateBookingAPI(){
        Map<String,Object> requestPayload = createBookingPayload("Karate","Automation","Postman");

        Response createBookingResponse = given().spec(requestSpecification).basePath("booking").
                                                body(requestPayload).
                                                when().post().
                                                then().assertThat().statusCode(200).and().
                                                body("bookingid", Matchers.is(Matchers.notNullValue())).
                                                extract().response();

        int bookingId = createBookingResponse.jsonPath().getInt("bookingid");
        System.out.println(bookingId);

        //Update Call
        Map<String,Object> updatedPayload = createBookingPayload("RestAssured","Automation","Bruno");
        Response updateBookingResponse = given().spec(requestSpecification).basePath("booking/{bookingId}").
                                                body(updatedPayload).
                                                pathParam("bookingId",bookingId).
                                                auth().preemptive().basic("admin","password123").
                                                when().put().
                                                then().assertThat().statusCode(200).
                                                extract().response();
    }
    @Test
    public void testCreateBookingAPIUsingPojo(){
        CreateBookingRequest createBookingRequestPayload = createBookingPayloadPojo("Rest Assured","Automation","Bruno");

        Response createBookingResponse = given().spec(requestSpecification).basePath("booking").
                                                body(createBookingRequestPayload).
                                                when().post().
                                                then().assertThat().statusCode(200).and().
                                                body("bookingid", Matchers.is(Matchers.notNullValue())).
                                                extract().response();

        int bookingId = createBookingResponse.jsonPath().getInt("bookingid");
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
