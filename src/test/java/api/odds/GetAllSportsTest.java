package api.odds;

import api.odds.base.ApiOddsBaseTest;
import api.odds.pojo.allsports.SportPojo;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;


public class GetAllSportsTest extends ApiOddsBaseTest {
    @Test
    public void getAllSportsNamesTest () {

       Response response = requestSpecification
        .when()
        .get("https://api.the-odds-api.com/v4/sports/");

       //Assert status code
       Assert.assertEquals(response.statusCode(), 200);

       //Assert headers
       Assert.assertNotNull(response.header("Content-Type"));
       Assert.assertEquals(response.header("Content-Type"), "application/json; charset=utf-8");

       //Assert sport names
        List <SportPojo> listOfSports = Arrays.asList(response.getBody().as(SportPojo[].class));
        List <String> actualSportNames = listOfSports.stream().map(a-> a.getKey()).toList();
        Assert.assertTrue(actualSportNames.contains("americanfootball_cfl"));
        Assert.assertTrue(actualSportNames.contains("boxing_boxing"));
        Assert.assertTrue(actualSportNames.contains("icehockey_nhl"));
        Assert.assertTrue(actualSportNames.contains("basketball_nba"));
        Assert.assertTrue(actualSportNames.contains("politics_us_presidential_election_winner"));

        //Assert US Election category
        SportPojo usElection = listOfSports.stream().filter(a-> a.getKey().equals("politics_us_presidential_election_winner")).findAny().orElse(null);
        Assert.assertTrue(usElection.getGroup().equals("Politics"));
        Assert.assertTrue(usElection.getTitle().equals("US Presidential Elections Winner"));
        Assert.assertTrue(usElection.getDescription().equals("2024 US Presidential Election Winner"));
        Assert.assertTrue(usElection.isActive());
        Assert.assertTrue(usElection.isHasOutrights());
    }
}
