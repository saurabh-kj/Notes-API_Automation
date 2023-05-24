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
    static String cookie = "__stripe_mid=c0eb9fb3-a0ae-4e0a-8cff-f134271f811c29eed1; __stripe_sid=a94a8e24-8124-4cde-b8b4-042961bb822b01e247";
    @BeforeClass
    public void authSetup() {
        RequestSpecBuilder build = new RequestSpecBuilder();
        build.addHeader("accept", "application/json");
        build.addHeader("Content-Type", "application/json");
        /*build.addHeader("Host","reqres.in");
        build.addHeader("Origin", "https://reqres.in");
        build.addHeader("Cookie", cookie);*/

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
