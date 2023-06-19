package utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.util.Properties;

public class RequestSpec {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(RequestSpec.class);
    public static Properties prop;
    public static String baseURI;
    public static RequestSpecification requestSpec;
    @BeforeClass
    public void authSetup() {
        RequestSpecBuilder build = new RequestSpecBuilder();
        build.addHeader("accept", "application/json");
        build.addHeader("Content-Type", "application/json");
        build.addHeader("x-auth-token", "f6cba598a45f43d29662d1cc02b112b8ccf23574e64a4e03a3ca096193b33652");
        requestSpec = build.build();
    }

    @BeforeSuite
    public void preCondition(){
        try{
            FileInputStream fis = new FileInputStream("./properties/config.properties");
            prop = new Properties();
            prop.load(fis);
            baseURI = System.getProperty("Host");
            if (baseURI == null){
                baseURI = prop.getProperty("Host");
            }
        }catch (Exception e){
            logger.error("Exception found at pre-condition: ", e);
            Assert.fail("Exception found at pre-condition: "+ e.getMessage(), e);
        }
    }

}
