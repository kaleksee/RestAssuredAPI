package api.odds;

import api.odds.base.ApiOddsBaseTest;
import api.odds.pojo.sportsodds.SportsOddsPojo;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class GetUSElectionOdds extends ApiOddsBaseTest {
    @Test
    public void testUsElectionOdds () {
        Response response = requestSpecification.queryParam("regions", "us")
        .when()
        .get("https://api.the-odds-api.com/v4/sports/politics_us_presidential_election_winner/odds/");

        //Assert status code
        Assert.assertEquals(response.statusCode(), 200);

        //Assert headers
        Assert.assertNotNull(response.header("Content-Type"));
        Assert.assertEquals(response.header("Content-Type"), "application/json; charset=utf-8");
        List<SportsOddsPojo> listOfSports = Arrays.asList(response.getBody().as(SportsOddsPojo[].class));

        //Assert object fields
        Assert.assertEquals(listOfSports.get(0).getId(), "a0ae0a6a79e8fd87122e3282a75f94be");
        Assert.assertEquals(listOfSports.get(0).isHasOutrights(), true);
        Assert.assertEquals(listOfSports.get(0).getSportKey(), "politics_us_presidential_election_winner");
        Assert.assertEquals(listOfSports.get(0).getSportTitle(), "US Presidential Elections Winner");
        Assert.assertNull(listOfSports.get(0).getHomeTeam());
        Assert.assertNull(listOfSports.get(0).getAwayTeam());
        List <String> expectedBookmakerNames = Arrays.asList("bovada", "betonlineag");
        List <String> actualBookmakerNames = listOfSports.get(0).getBookmakers().stream().map(a->a.getKey()).toList();
        Assert.assertEquals(expectedBookmakerNames, actualBookmakerNames);


    }
}
