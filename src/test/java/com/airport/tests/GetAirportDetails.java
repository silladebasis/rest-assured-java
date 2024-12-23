package com.airport.tests;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetAirportDetails {
    RequestSpecification requestSpecification = given().baseUri("https://airportgap.com/").
                                                       filters(new RequestLoggingFilter(),new ResponseLoggingFilter()).contentType(ContentType.JSON);
    @Test
    public void testGetAirportAPI(){
        Response response = given().spec(requestSpecification).basePath("api/airports").
                                   when().get().
                                   then().extract().response();
        System.out.println(response.asPrettyString());
        System.out.println(response.getHeader("Content-Type"));
        System.out.println(response.getStatusCode());
        System.out.println(response.getStatusLine());
        System.out.println(response.getContentType());
        System.out.println(response.getHeaders());
    }
    @Test
    public void testPathParameter(){
        Response response = given().spec(requestSpecification).basePath("api/airports/{id}").
                                   pathParam("id","GKA").when().get();
        //System.out.println(response.asString());
    }
    @Test
    public void validateResponse(){
        Response response = given().spec(requestSpecification).basePath("api/airports/{id}").
                                              pathParam("id","GKA").
                                              when().get().
                                              then().assertThat().statusCode(200).
                                              body("data.attributes.city", equalTo("Goroka")).and().
                                              body("data.type",equalTo("airport")).and().
                                              body("data.attributes.country",Matchers.is(notNullValue())).
                                              extract().response();

    }
    @Test
    public void ResponseExtractionUsingJsonPath(){
        Response response = given().spec(requestSpecification).basePath("api/airports/{id}").
                                   pathParam("id","GKA").
                                   when().get().
                                   then().assertThat().statusCode(200).
                                   body("data.attributes.country",Matchers.is(notNullValue())).
                                   extract().response();

        String city = response.jsonPath().getString("data.attributes.city");
        String type = response.jsonPath().getString("data.type");
        assertThat(city,equalTo("Goroka"));
        assertThat(type,equalTo("airport"));

        Map<String,Object>attributesMap = response.jsonPath().getMap("data.attributes");
        System.out.println(attributesMap);
    }
    @Test
    public void jsonSchemaValidationTest(){
        Response response = given().spec(requestSpecification).basePath("api/airports/{id}").
                                   pathParam("id","GKA").
                                   when().get().
                                   then().assertThat().statusCode(200).
                                   body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/getAirportDetails.json"))).
                                   extract().response();

    }
    @Test
    public void rootPathTest(){
        Response response = given().spec(requestSpecification).basePath("api/airports/{id}").
                                   pathParam("id","GKA").
                                   when().get().
                                   then().assertThat().statusCode(200).
                                   rootPath("data.attributes").
                                   body("city",equalTo("Goroka")).
                                   body("country",equalTo("Papua New Guinea")).
                                   detachRootPath("data.attributes").
                                   body("data.type",equalTo("airport")).
                                   extract().response();

    }
}
