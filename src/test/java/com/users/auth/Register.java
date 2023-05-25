package com.users.auth;

import com.aventstack.extentreports.Status;
import health_check.GET_HealthCheck;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.RequestSpec;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static utils.ExcelUtils.*;
import static utils.extent.ExtentTestManager.getTest;

@Listeners(utils.Listener.ListenerClass.class)

public class Register extends RequestSpec {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GET_HealthCheck.class);
    //    Properties prop;
    String path;
    private SoftAssert sa;

    @BeforeMethod
    public void getSourcePath() {
        sa = new SoftAssert();
        try {
            path = prop.getProperty("Register");
        } catch (Exception e) {
            logger.error("Exception found at source path: ", e);
            sa.fail("Exception found at source path: " + e.getMessage(), e);
        }
    }

    // TC - User is registered successfully.
    @Test(priority = 0)
    public void registerSuccess(){
        try{
            Map<String, Object> map = new HashMap<>();
            JSONObject obj = new JSONObject(map);

            obj.put("name","Test User 110");
            obj.put("email","user110@mailinator.com");
            obj.put("password", "test@123");


            String reqBody = obj.toJSONString();
            System.out.println("Request Body: " + reqBody);

            Response res = given().log().all().spec(requestSpec).body(reqBody).when().post(baseURI+path).then().log().all().extract().response();
            getTest().log(Status.PASS, "Test Case to verify User is registered successfully.");
            String resBody = res.getBody().asString();
            System.out.println("Response Body: " + res.asPrettyString());

            sa.assertTrue(resBody.contains("User account is created successfully."));
            getTest().log(Status.PASS, "User account is created successfully.");
            sa.assertTrue(resBody.contains("user110@mailinator.com"));
            getTest().log(Status.PASS, "User email is found in response body.");
            sa.assertTrue(resBody.contains("Test User 110"));
            getTest().log(Status.PASS, "User's name is found in response body.");

//          Print created user's id
            JsonPath jsonPath = res.jsonPath();
            String userID = jsonPath.get("data.id");
                System.out.println("Created User's ID: " + userID);

        }catch (Exception e) {
            logger.error("Exception found at source path: ", e);
            sa.fail("Exception found at source path: " + e.getMessage(), e);
        }
    }

    // TC - Verify 409 error code while registering with existing user
    @Test(priority = 1)
    public void registerWithExistingUser(){
        try{
            Map<String, Object> map = new HashMap<>();
            JSONObject obj = new JSONObject(map);

            obj.put("name","Test User 1");
            obj.put("email","user1@mailinator.com");
            obj.put("password", "test@123");

            String reqBody = obj.toJSONString();
            System.out.println("Request Body: " + reqBody);

            Response res = given().log().all().spec(requestSpec).body(reqBody).when().post(baseURI+path).then().
                    assertThat().statusCode(SC_CONFLICT).log().all().extract().response();
            getTest().log(Status.PASS, "Test Case to Verify 409 Error code while registering with existing user.");

            String resBody = res.getBody().asString();

            sa.assertTrue(resBody.contains("An account already exists with the same email address"));
            getTest().log(Status.PASS, "Response body contains message as an account is already exists with input email id.");
            sa.assertTrue(resBody.contains("409"));
            getTest().log(Status.PASS, "Response body contains 409 error code.");
        }catch (Exception e) {
            logger.error("Exception found at source path: ", e);
            sa.fail("Exception found at source path: " + e.getMessage(), e);
        }
    }

    // TC - Create bulk users successfully using XLSX file.
    @Test(dataProvider = "users", priority = 2)
    public void createBulkUsers(String name, String email){
        try{
            Map<String, Object> map = new HashMap<>();
            JSONObject obj = new JSONObject(map);

            obj.put("name", name);
            obj.put("email", email);
            obj.put("password", "test@123");

            String reqBody = obj.toJSONString();
            System.out.println("Request Body: " + reqBody);

            Response res = given().log().all().spec(requestSpec).body(reqBody).when().post(baseURI+path).then().
                    assertThat().statusCode(SC_CREATED).log().all().extract().response();
            getTest().log(Status.PASS, "Test Case to verify to create users in bulk.");

            String resBody = res.getBody().asString();

            sa.assertTrue(resBody.contains("User account is created successfully."));
            getTest().log(Status.PASS, "User account is created successfully.");
            sa.assertTrue(resBody.contains(name));
            getTest().log(Status.PASS, "Response body contains user's name.");
            sa.assertTrue(resBody.contains(email));
            getTest().log(Status.PASS, "Response body contains user's email.");

        }catch (Exception e) {
            logger.error("Exception found at source path: ", e);
            sa.fail("Exception found at source path: " + e.getMessage(), e);
        }
    }

    @DataProvider(name = "users")
    String[][] getFileData() throws Exception {
        //Read data from excel
        String path = "./src/test/artifacts/users.xlsx";
        int row_count = getRowCount(path, "Sheet1");
        int col_count = getCellCount(path, "Sheet1", 1);

        String fileData[][] = new String[row_count][col_count];
        for (int i = 1; i <= row_count; i++) {                                       // for rows
            for (int j = 0; j < col_count; j++) {                                     // for columns
                fileData[i - 1][j] = getCellData(path, "Sheet1", i, j);
            }
        }
        return fileData;
    }

    // TC - Send request with empty name field
    @Test(priority = 3)
    public void requiredNameField(){
        try{
            Map<String, Object> map = new HashMap<>();
            JSONObject obj = new JSONObject(map);

            obj.put("name","");
            obj.put("email","user11@mailinator.com");
            obj.put("password", "test@123");

            String reqBody = obj.toJSONString();
            System.out.println("Request Body: " + reqBody);

            given().log().all().spec(requestSpec).body(reqBody).when().post(baseURI+path).then().
                    assertThat().statusCode(SC_BAD_REQUEST).log().all().extract().response();
            getTest().log(Status.PASS, "Test case to verify 400 error while sending request with empty name field.");
        }catch (Exception e) {
            logger.error("Exception found at source path: ", e);
            sa.fail("Exception found at source path: " + e.getMessage(), e);
        }
    }

    // TC - Send request with empty email field
    @Test(priority = 3)
    public void requiredEmailField(){
        try{
            Map<String, Object> map = new HashMap<>();
            JSONObject obj = new JSONObject(map);

            obj.put("name","Test User 11");
            obj.put("email","");
            obj.put("password", "test@123");

            String reqBody = obj.toJSONString();
            System.out.println("Request Body: " + reqBody);

            given().log().all().spec(requestSpec).body(reqBody).when().post(baseURI+path).then().
                    assertThat().statusCode(SC_BAD_REQUEST).log().all().extract().response();
            getTest().log(Status.PASS, "Test case to verify 400 error while sending request with empty email field.");
        }catch (Exception e) {
            logger.error("Exception found at source path: ", e);
            sa.fail("Exception found at source path: " + e.getMessage(), e);
        }
    }

    // TC - Send request with empty name field
    @Test(priority = 4)
    public void requiredPasswordField(){
        try{
            Map<String, Object> map = new HashMap<>();
            JSONObject obj = new JSONObject(map);

            obj.put("name","Test User11");
            obj.put("email","user11@mailinator.com");
            obj.put("password", "");

            String reqBody = obj.toJSONString();
            System.out.println("Request Body: " + reqBody);

            given().log().all().spec(requestSpec).body(reqBody).when().post(baseURI+path).then().
                    assertThat().statusCode(SC_BAD_REQUEST).log().all().extract().response();
            getTest().log(Status.PASS, "Test case to verify 400 error while sending request with empty password field.");
        }catch (Exception e) {
            logger.error("Exception found at source path: ", e);
            sa.fail("Exception found at source path: " + e.getMessage(), e);
        }
    }

}
