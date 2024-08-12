package api.odds.pojo.sportsodds;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonPropertyOrder({
        "key",
        "last_update",
        "outcomes"

})
public class Market {
    @JsonProperty("key")
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<OutCome> getOutcomesList() {
        return list;
    }

    public void setOutcomesList(List<OutCome> list) {
        this.list = list;
    }

    @JsonProperty("last_update")
    private String lastUpdate;
    @JsonProperty("outcomes")
    private List <OutCome> list = new ArrayList<>();

}
