package com.users.auth;

import com.aventstack.extentreports.Status;
import health_check.GET_HealthCheck;
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

public class Forgot_Password extends RequestSpec {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GET_HealthCheck.class);
    //    Properties prop;
    private String path;
    private SoftAssert sa;

    @BeforeMethod
    void getSourcePath() {
        sa = new SoftAssert();
        try {
            path = prop.getProperty("Forgot_Password");
        } catch (Exception e) {
            logger.error("Exception found at source path: ", e);
            Assert.fail("Exception found at source path: " + e.getMessage(), e);
        }
    }

    //TC - Verify request for forget password with valid email
    @Test(priority = 0)
    public void forgetPassWithValidEmail(){
        Map<String, Object> map = new HashMap<>();
        JSONObject obj = new JSONObject(map);

        obj.put("email", "tuser1@mailinator.com");

        String reqData = obj.toJSONString();

        Response res = given().spec(requestSpec).body(reqData).when().post(baseURI+path).then().assertThat().statusCode(SC_OK).log().all().extract().response();
        getTest().log(Status.PASS,"Test case to verify request for forget password with VALID email");

        String resBody = res.getBody().asString();
        sa.assertTrue(resBody.contains("true"));
        getTest().log(Status.PASS,"Response body contains Success as True.");
        sa.assertTrue(resBody.contains("Password reset link successfully sent to practice@expandtesting.com. Please verify by clicking on the given lin"));
        getTest().log(Status.PASS,"Response body contains message for reset link sent successfully");
    }

    //TC - Verify request for forget password with invalid email
    @Test(priority = 1)
    public void forgetPassWithInvalidEmail(){
        Map<String, Object> map = new HashMap<>();
        JSONObject obj = new JSONObject(map);

        obj.put("email", "tuser1@gmail.com");

        String reqData = obj.toJSONString();

        given().spec(requestSpec).body(reqData).when().post(baseURI+path).then().assertThat().statusCode(SC_UNAUTHORIZED).log().all().extract().response();
        getTest().log(Status.PASS,"Test case to verify request for forget password with INVALID email.");
    }

    //TC - Verify request for forget password with invalid email
    @Test(priority = 2)
    public void forgetPassWithInvalidEmail2(){
        Map<String, Object> map = new HashMap<>();
        JSONObject obj = new JSONObject(map);

        obj.put("email", "tuser1@gmail");

        String reqData = obj.toJSONString();

        given().spec(requestSpec).body(reqData).when().post(baseURI+path).then().assertThat().statusCode(SC_BAD_REQUEST).log().all().extract().response();
        getTest().log(Status.PASS,"Test case to verify request for forget password with INVALID email - 2.");
    }

    //TC - Verify request for forget password with invalid email
    @Test(priority = 3)
    public void forgetPassWithEmptyEmail(){
        Map<String, Object> map = new HashMap<>();
        JSONObject obj = new JSONObject(map);

        obj.put("email", "");

        String reqData = obj.toJSONString();

        given().spec(requestSpec).body(reqData).when().post(baseURI+path).then().assertThat().statusCode(SC_BAD_REQUEST).log().all().extract().response();
        getTest().log(Status.PASS,"Test case to verify request for forget password with EMPTY email field.");
    }

    //TC - Verify request for forget password with invalid email
    @Test(priority = 4)
    public void forgetPassWithDifferentHTTPMethod(){
        Map<String, Object> map = new HashMap<>();
        JSONObject obj = new JSONObject(map);

        obj.put("email", "tuser1@gmail.com");

        String reqData = obj.toJSONString();

        given().spec(requestSpec).body(reqData).when().put(baseURI+path).then().assertThat().
                statusCode(SC_NOT_FOUND).log().all().extract().response();
        getTest().log(Status.PASS,"Test case to verify request for forget password with different HTTP method.");
    }
}
