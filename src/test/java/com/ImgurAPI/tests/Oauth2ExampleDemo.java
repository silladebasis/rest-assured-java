package com.ImgurAPI.tests;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class Oauth2ExampleDemo {
    RequestSpecification requestSpecification = given().baseUri("https://api.imgur.com/3").
                                                       filters(new RequestLoggingFilter(),new ResponseLoggingFilter());
    String deleteImageHash = "";
    @Test
    public void uploadImageWithOauth2(){
        String accessToken = System.getenv("IMGUR_ACCESS_TOKEN");
        //System.out.println(accessToken);
        Response uploadApiResponse = given().spec(requestSpecification).basePath("/image").
                                            contentType(ContentType.MULTIPART).
                                            and().multiPart("image",new File("src/test/resources/Puma.jpg")).
                                            and().multiPart("type","image").
                                            and().multiPart("title","Puma").
                                            and().multiPart("description","Rest Assured Upload Image").
                                            auth().oauth2(accessToken).
                                            when().post().then().assertThat().statusCode(200).extract().response();
        deleteImageHash = uploadApiResponse.jsonPath().getString("data.deletehash");
    }
    @Test(dependsOnMethods = "uploadImageWithOauth2" )
    public void deleteImageWithOauth2(){
        String accessToken = System.getenv("IMGUR_ACCESS_TOKEN");
        Response uploadApiResponse = given().spec(requestSpecification).basePath("/image/{imageHash}").
                                            auth().oauth2(accessToken).
                                            pathParam("imageHash",deleteImageHash).contentType(ContentType.JSON).
                                            when().delete().then().assertThat().statusCode(200).extract().response();
    }
    @Test
    public void generateNewAccessToken(){
        given().baseUri("https://api.imgur.com").basePath("oauth2/token").
                filters(new RequestLoggingFilter(),new ResponseLoggingFilter()).
                formParam("refresh_token",System.getenv("IMGUR_REFRESH_TOKEN")).
                formParam("client_id",System.getenv("IMGUR_CLIENT_ID")).
                formParam("client_secret",System.getenv("IMGUR_CLIENT_SECRET")).
                formParam("grant_type","refresh_token").
                when().post().then().assertThat().statusCode(200).extract().response();
    }
}
