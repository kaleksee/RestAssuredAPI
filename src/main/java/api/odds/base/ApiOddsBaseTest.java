package api.odds.base;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ApiOddsBaseTest {
    private final static String apiKey = "4f6a10730387a9e211ad975d2806f0fa";

    public RequestSpecification requestSpecification = given().queryParam("apiKey", apiKey);
}
