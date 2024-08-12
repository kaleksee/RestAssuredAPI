package api.petstore;

import api.petstore.pojo.Category;
import api.petstore.pojo.PetPojo;
import api.petstore.pojo.Tag;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.entity.ContentType;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Arrays;

public class PetStoreAPITest {

    private int id;
    private PetPojo pet;
    private int invalidId = 7000;
    RequestSpecification requestSpecification;

    @BeforeTest
    public void setUp () {
        id = (int) Math.floor(Math.random()*100000+1);
        //Create object
        pet = new PetPojo();
        pet.setId(id);
        Category category = new Category();
        category.setId(777);
        category.setName("MyCategory");
        pet.setCategory(category);
        pet.setName("doggie");
        Tag tag = new Tag();
        tag.setId(0);
        tag.setName("TagName");
        pet.setTags(Arrays.asList(tag));
        System.out.println("id " + id);
    }

    @Test(priority = 1)
    public void createPet () {

        //send request
        Response response = RestAssured.given().baseUri( "https://petstore.swagger.io/v2")
                .when()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .with()
                .body(pet)
                .post("/pet");

        //Assert Status code
        Assert.assertEquals(response.statusCode(), 200);

        //Assert values in response
        assertPetFields(response);
    }

    @Test(priority = 2)
    public void validatePetExists () {
        //send request
        Response response = RestAssured.given().baseUri( "https://petstore.swagger.io/v2")
                .when()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .get("/pet/" + id);

        //Assert Status code
        Assert.assertEquals(response.statusCode(), 200);

        //Assert values in response
        assertPetFields(response);
    }

    @Test(priority = 3)
    public void updatePet () {
        //update value in field name
        pet.setName("NewName");
        //send request
        Response response = RestAssured.given().baseUri( "https://petstore.swagger.io/v2").formParam("name", "Wolf")
                .when()
                .post("/pet/" + id);

        //Assert Status code
        Assert.assertEquals(response.statusCode(), 200);

        //Assert values in response
        assertPetFields(response);
    }

    @Test(priority = 4)
    public void deletePet () {
        //send request
        Response response = RestAssured.given().baseUri( "https://petstore.swagger.io/v2")
                .when()
                .delete("/pet/" + id);

        //Assert Status code
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 5)
    public void validatePetDoesntExist() {
        //send request
        Response response = RestAssured.given().baseUri( "https://petstore.swagger.io/v2")
                .when()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .get("/pet/" + id);

        //Assert Status code
        Assert.assertEquals(response.statusCode(), 404);
    }

    private void assertPetFields (Response response) {
        //Assert values in response
        PetPojo responsePet = response.getBody().as(PetPojo.class);
        Assert.assertEquals(responsePet.getId(), id);
        Assert.assertEquals(responsePet.getName(), pet.getName());
        Assert.assertEquals(responsePet.getStatus(), pet.getStatus());
        Assert.assertEquals(responsePet.getCategory().getId(), pet.getCategory().getId());
        Assert.assertEquals(responsePet.getCategory().getName(), pet.getCategory().getName());
        Assert.assertEquals(responsePet.getTags().getFirst().getId(), pet.getTags().getFirst().getId());
        Assert.assertEquals(responsePet.getTags().getFirst().getName(), pet.getTags().getFirst().getName());
    }
}
