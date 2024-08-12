package api.odds;

import api.odds.base.ApiOddsBaseTest;
import api.odds.pojo.scores.ScorePojo;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;


public class GetNBAScores extends ApiOddsBaseTest {
    @Test
    public void testGetNBAScores () {
       Response response = requestSpecification.queryParam("daysFrom", "1")
               .when()
               .get("https://api.the-odds-api.com/v4/sports/basketball_nba/scores/");

        //Assert status code
        Assert.assertEquals(response.statusCode(), 200);

        //Assert headers
        Assert.assertNotNull(response.header("Content-Type"));
        Assert.assertEquals(response.header("Content-Type"), "application/json; charset=utf-8");

        //Assert results are for sport NBA
        List<ScorePojo> list = Arrays.asList(response.getBody().as(ScorePojo[].class));
        for (ScorePojo scorePojo: list) {
            Assert.assertEquals(scorePojo.getSportKey(), "basketball_nba");
            Assert.assertEquals(scorePojo.getSportTitle(), "NBA");
        }

    }
}
