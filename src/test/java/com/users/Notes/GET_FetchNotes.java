package com.users.Notes;

import com.aventstack.extentreports.Status;
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

public class GET_FetchNotes extends RequestSpec {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(POST_CreateNote.class);
    //    Properties prop;
    private String path;
    private SoftAssert sa;

    @BeforeMethod
    void getSourcePath() {
        sa = new SoftAssert();
        try {
            path = prop.getProperty("Notes");
        } catch (Exception e) {
            logger.error("Exception found at source path: ", e);
            Assert.fail("Exception found at source path: " + e.getMessage(), e);
        }
    }

    //    TC - Verify to fetch all created notes successfully
    @Test(priority = 0)
    public void fetchAllNotes(){
        Response res = given().spec(requestSpec).when().get(baseURI+path).then().assertThat().statusCode(SC_OK).log().all().extract().response();
        getTest().log(Status.PASS, "Test case to fetch all created notes successfully");

        String resBody = res.getBody().asString();

        //Assert that request is created successfully
        sa.assertTrue(resBody.contains("Notes successfully retrieved"));

    }

    //    TC - Verify bad request error
    @Test(priority = 1)
    public void BadRequestCheck(){
        given().header("accept", "application/json").
                header("Content-Type", "application/PDF").
                header("x-auth-token", "f6cba598a45f43d29662d1cc02b112b8ccf23574e64a4e03a3ca096193b33652").
                when().post(baseURI+path).then().assertThat().statusCode(SC_BAD_REQUEST).log().all().extract().response();

        getTest().log(Status.PASS, "Test case to check 400 error code");

    }

    //    TC - Verify 401 error
    @Test(priority = 2)
    public void UnauthorizedErrorCheck(){
        Map<String, Object> map = new HashMap<>();
        JSONObject obj = new JSONObject(map);

        obj.put("title", "Test Title");
        obj.put("description", "This is a testing purpose comment.");
        obj.put("category","Test1234");

        String reqBody = obj.toJSONString();
        System.out.println("Request Body:" + reqBody);

        given().header("accept", "application/json").
                header("Content-Type", "application/json").
                when().post(baseURI+path).then().assertThat().statusCode(SC_UNAUTHORIZED).log().all().extract().response();
        getTest().log(Status.PASS, "Test case to check 401 error code")

    }

}
