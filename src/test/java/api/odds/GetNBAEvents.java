package api.odds;

import api.odds.base.ApiOddsBaseTest;
import api.odds.pojo.events.SportEventPojo;
import api.odds.pojo.sporteventodds.SportEventOddsPojo;
import api.odds.pojo.sportsodds.Bookmaker;
import api.odds.pojo.sportsodds.Market;
import api.odds.pojo.sportsodds.OutCome;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GetNBAEvents extends ApiOddsBaseTest {

    private List <String> eventIds = new ArrayList<>();
    @Test
    public void testGetNBAEvents () {
        Response response = requestSpecification
                .when()
                .get("https://api.the-odds-api.com/v4/sports/basketball_nba/events");

        //Assert status code
        Assert.assertEquals(response.statusCode(), 200);

        //Assert headers
        Assert.assertNotNull(response.header("Content-Type"));
        Assert.assertEquals(response.header("Content-Type"), "application/json; charset=utf-8");

        //Assert sport_key and sport_title
        List <SportEventPojo> sportEventPojosList = Arrays.asList(response.getBody().as(SportEventPojo[].class));
        for (SportEventPojo event: sportEventPojosList) {
            Assert.assertEquals(event.getSportKey(), "basketball_nba");
            Assert.assertEquals(event.getSportTitle(), "NBA");
        }

        //Store event Ids in a variable
        for (SportEventPojo event: sportEventPojosList) {
            eventIds.add(event.getId());
        }
        Assert.assertTrue(!eventIds.isEmpty());
    }

    @Test(dependsOnMethods = {"testGetNBAEvents"})
    public void getOddsForEachEvent () {
        for (String eventId: eventIds) {
            Response response = requestSpecification.queryParam("regions", "us")
                    .when()
                    .get("https://api.the-odds-api.com/v4/sports/basketball_nba/events/"+eventId+"/odds/");

            //Assert status code
            Assert.assertEquals(response.statusCode(), 200);

            //Assert values in response
            SportEventOddsPojo sportEventOddsPojo = response.getBody().as(SportEventOddsPojo.class);
            Assert.assertEquals(sportEventOddsPojo.getSportKey(), "basketball_nba");
            Assert.assertEquals(sportEventOddsPojo.getSportTitle(), "NBA");
            Assert.assertTrue(!sportEventOddsPojo.getBookmakersList().isEmpty());
            Assert.assertTrue(!sportEventOddsPojo.getHomeTeam().isEmpty());
            Assert.assertTrue(!sportEventOddsPojo.getAwayTeam().isEmpty());
            List <String> expectedBookmakers = new ArrayList<>(Arrays.asList("draftkings", "fanduel", "bovada", "betmgm"));
            List <String> actualBookmakers = sportEventOddsPojo.getBookmakersList().stream().map(a->a.getKey()).toList();
            Assert.assertEquals(expectedBookmakers, actualBookmakers);
            //Assert odds are not empty and  > 0
            for (Bookmaker bookmaker: sportEventOddsPojo.getBookmakersList()) {
                for (Market market: bookmaker.getMarkets()) {
                    for (OutCome outCome: market.getOutcomesList()) {
                        Assert.assertTrue(outCome.getPrice() > 0);
                    }
                }
            }

        }
    }

}
