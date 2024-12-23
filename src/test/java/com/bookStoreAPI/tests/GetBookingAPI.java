package com.bookStoreAPI.tests;

import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class GetBookingAPI {
    RequestSpecification requestSpecification = given().baseUri("https://demoqa.com/BookStore").//https://demoqa.com/BookStore/v1/Books
                                                       filters(new RequestLoggingFilter(LogDetail.URI),new ResponseLoggingFilter()).contentType(ContentType.JSON);
    @Test
    public void testQueryParameter(){
        Map<String,String> queryMap = new HashMap<>();
        queryMap.put("ISBN","9781449337711");
        queryMap.put("pages","238");
        Response response = given().spec(requestSpecification).basePath("/v1/Book").
                                    //queryParam("ISBN","9781449337711").
                                            queryParams(queryMap).
                                    when().get();
        //response.prettyPrint();
        //System.out.println(response.asPrettyString());
    }
}
