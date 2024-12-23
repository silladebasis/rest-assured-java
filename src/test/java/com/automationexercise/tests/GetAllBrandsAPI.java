package com.automationexercise.tests;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class GetAllBrandsAPI {
    RequestSpecification requestSpecification = given().baseUri("https://automationexercise.com/").
                                                       basePath("api/brandsList").
                                                       filters(new RequestLoggingFilter(),new ResponseLoggingFilter()).contentType(ContentType.JSON);
    @Test
    public void testGetAllBrandsAPI(){
        Response response = given().spec(requestSpecification).
                                   when().get().
                                   then().extract().response();
        System.out.println(response.asPrettyString());
    }
    @Test
    public void testHeadersOBrandsAPI(){
        Map<String,Object> map = new HashMap<>();
        map.put("test-1","value-1");
        map.put("test-2","value-2");

        Header header = new Header("header-1","header-value1");

        Response response = given().spec(requestSpecification).
                                   headers(map). // multiple headers using hashMap
                                   header(header).
                                   when().get().
                                   then().extract().response();
        System.out.println(response.asPrettyString());
    }
}
