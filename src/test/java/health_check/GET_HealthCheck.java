package health_check;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.RequestSpec;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class GET_HealthCheck extends RequestSpec {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GET_HealthCheck.class);
    //    Properties prop;
    String path;

    @BeforeMethod
    public void getSourcePath() {
        try {
            path = prop.getProperty("Health");
        } catch (Exception e) {
            logger.error("Exception found at source path: ", e);
            Assert.fail("Exception found at source path: " + e.getMessage(), e);
        }
    }

    @Test
    public void healthCheck() {
        Response res = given().baseUri(baseURI).get(path).then().assertThat().statusCode(SC_OK).log().all().extract().response();
        String resBody = res.getBody().asString();

        //Assert the data
        Assert.assertTrue(resBody.contains("Notes API is Running"));
    }
}
