package com.auth.tests;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class BasicAndDigestAuthTests {
    RequestSpecification requestSpecification = given().baseUri("https://restful-booker.herokuapp.com/").
                                                       filters(new RequestLoggingFilter(),new ResponseLoggingFilter()).contentType(ContentType.JSON);
    @Test
    public void simpleHttpDeleteWithPreemptiveBasicAuth(){
        Response deleteAPiResponse = given().spec(requestSpecification).
                                            basePath("booking/{bookingId}").
                                            auth().preemptive().basic("admin","password123").// challenged basic auth
                                            pathParam("bookingId",10).
                                            when().delete();
    }
    @Test
    public void simpleHttpDeleteWithCustomHeader(){
        Response deleteAPiResponse = given().spec(requestSpecification).
                                            basePath("booking/{bookingId}").
                                            //header("Cookie","token=2b0ab6f9517e36c").
                                            header("Authorization","Basic YWRtaW46cGFzc3dvcmQxMjM=")  .
                                            pathParam("bookingId",521).
                                            when().delete();
    }
    @Test
    public void challengedBasicAuth(){
        Response deleteAPiResponse = given().baseUri("https://the-internet.herokuapp.com/").
                                            basePath("basic_auth").
                                            auth().basic("admin","admin").when().get();
        deleteAPiResponse.prettyPrint();
    }
    @Test
    public void challengedDigestAuth(){
        Response deleteAPiResponse = given().baseUri("https://the-internet.herokuapp.com/").
                                            basePath("digest_auth").
                                            auth().digest("admin","admin").when().get();
        deleteAPiResponse.prettyPrint();
    }
}
