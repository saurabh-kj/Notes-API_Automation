package com.users.auth;

import health_check.GET_HealthCheck;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.RequestSpec;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static utils.ExcelUtils.*;

public class Register extends RequestSpec {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GET_HealthCheck.class);
    //    Properties prop;
    String path;

    @BeforeMethod
    public void getSourcePath() {
        try {
            path = prop.getProperty("Register");
        } catch (Exception e) {
            logger.error("Exception found at source path: ", e);
            Assert.fail("Exception found at source path: " + e.getMessage(), e);
        }
    }

    // TC_001 - User is registered successfully.
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

            String resBody = res.getBody().asString();
            System.out.println("Response Body: " + res.asPrettyString());

            Assert.assertTrue(resBody.contains("User account created successfully"));
            Assert.assertTrue(resBody.contains("user110@mailinator.com"));
            Assert.assertTrue(resBody.contains("Test User 110"));

//          Print created user's id
            JsonPath jsonPath = res.jsonPath();
            String userID = jsonPath.get("data.id");
            System.out.println("Created User's ID: " + userID);

        }catch (Exception e) {
            logger.error("Exception found at source path: ", e);
            Assert.fail("Exception found at source path: " + e.getMessage(), e);
        }
    }

    // TC_002 - Verify 409 error code while registering with existing user
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

            String resBody = res.getBody().asString();

            Assert.assertTrue(resBody.contains("An account already exists with the same email address"));
            Assert.assertTrue(resBody.contains("409"));
        }catch (Exception e) {
            logger.error("Exception found at source path: ", e);
            Assert.fail("Exception found at source path: " + e.getMessage(), e);
        }
    }

    // TC_003 - Create bulk users successfully using XLSX file.
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

            String resBody = res.getBody().asString();

            Assert.assertTrue(resBody.contains("User account created successfully"));
            Assert.assertTrue(resBody.contains(name));
            Assert.assertTrue(resBody.contains(email));

        }catch (Exception e) {
            logger.error("Exception found at source path: ", e);
            Assert.fail("Exception found at source path: " + e.getMessage(), e);
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

    // TC_004 - Send request with empty name field
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

        }catch (Exception e) {
            logger.error("Exception found at source path: ", e);
            Assert.fail("Exception found at source path: " + e.getMessage(), e);
        }
    }

    // TC_005 - Send request with empty email field
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

        }catch (Exception e) {
            logger.error("Exception found at source path: ", e);
            Assert.fail("Exception found at source path: " + e.getMessage(), e);
        }
    }

    // TC_006 - Send request with empty name field
    @Test(priority = 3)
    public void requiredPasswordField(){
        try{
            Map<String, Object> map = new HashMap<>();
            JSONObject obj = new JSONObject(map);

            obj.put("name","Test User11");
            obj.put("email","user11@mailinator.com");
            obj.put("password", "");

            String reqBody = obj.toJSONString();
            System.out.println("Request Body: " + reqBody);

            Response res = given().log().all().spec(requestSpec).body(reqBody).when().post(baseURI+path).then().
                    assertThat().statusCode(SC_BAD_REQUEST).log().all().extract().response();

        }catch (Exception e) {
            logger.error("Exception found at source path: ", e);
            Assert.fail("Exception found at source path: " + e.getMessage(), e);
        }
    }

}
