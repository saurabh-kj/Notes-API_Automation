package com.users.auth;

import com.aventstack.extentreports.Status;
import health_check.GET_HealthCheck;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.RequestSpec;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static utils.extent.ExtentTestManager.getTest;

@Listeners(utils.Listener.ListenerClass.class)

public class Login extends RequestSpec {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GET_HealthCheck.class);
    //    Properties prop;
    private String path;
    private SoftAssert sa;

    @BeforeMethod
    void getSourcePath() {
        sa = new SoftAssert();
        try {
            path = prop.getProperty("Login");
        } catch (Exception e) {
            logger.error("Exception found at source path: ", e);
            Assert.fail("Exception found at source path: " + e.getMessage(), e);
        }
    }

    //TC - Verify login success with valid credentials
    @Test(priority = 0)
    public void loginWithValidCreds(){
        Map<String, Object> map = new HashMap<>();
        JSONObject obj = new JSONObject(map);

        obj.put("email", "tuser1@mailinator.com");
        obj.put("password", "test@123");

        String reqBody = obj.toJSONString();
        System.out.println("Request body: " + reqBody);

        Response res = given().spec(requestSpec).body(reqBody).when().post(baseURI+path).then().assertThat().statusCode(SC_OK).log().all().extract().response();
        getTest().log(Status.PASS, "Test case to verify login success with valid credentials");

        String resBody = res.getBody().asString();

        //Assert that user is logged in successfully
        sa.assertTrue(resBody.contains("Login successful"));
        getTest().log(Status.PASS, "Response body contains message for successful user login.");
        //Fetch ID of logged in user
        JsonPath jp = res.jsonPath();
        String userID = jp.get("data.id");
        String name = jp.get("data.name");
        String email = jp.get("data.email");
        String token = jp.get("data.token");
        System.out.println("Logged in User's Details:");
        System.out.println("Name: " + name+ "\nEmail: "+ email + "\nID: "+userID + "T\noken: " + token);
    }

    //TC - Verify login failure with invalid credentials
    @Test(priority = 1)
    public void loginWithInvalidCreds(){
        Map<String, Object> map = new HashMap<>();
        JSONObject obj = new JSONObject(map);

        obj.put("email", "tuser1@mailinator.com");
        obj.put("password", "test@123qroiuo");

        String reqBody = obj.toJSONString();
        System.out.println("Request body: " + reqBody);

        given().spec(requestSpec).body(reqBody).when().post(baseURI+path).then().assertThat().statusCode(SC_UNAUTHORIZED).log().all().extract().response();
        getTest().log(Status.PASS, "Test case to verify login failure with invalid credentials.");
    }

    //TC - Verify login failure with empty fields
    @Test(priority = 2)
    public void loginWithEmptyField(){
        Map<String, Object> map = new HashMap<>();
        JSONObject obj = new JSONObject(map);

        obj.put("email", "tuser1@mailinator.com");
        obj.put("password", "");

        String reqBody = obj.toJSONString();
        System.out.println("Request body: " + reqBody);

        given().spec(requestSpec).body(reqBody).when().post(baseURI+path).then().assertThat().statusCode(SC_BAD_REQUEST).log().all().extract().response();
        getTest().log(Status.PASS, "Test case to verify login failure with empty fields.");
    }

    //TC - Verify login failure with different HTTP method
    @Test(priority = 3)
    public void loginWithDifferentHTTPMethod(){
        Map<String, Object> map = new HashMap<>();
        JSONObject obj = new JSONObject(map);

        obj.put("email", "tuser1@mailinator.com");
        obj.put("password", "");

        String reqBody = obj.toJSONString();
        System.out.println("Request body: " + reqBody);

        given().spec(requestSpec).body(reqBody).when().get(baseURI+path).then().assertThat().statusCode(SC_NOT_FOUND).log().all().extract().response();
        getTest().log(Status.PASS, "Test case to verify login failure while sending request with different HTTP method.");
    }
}
