package pl.bykowski.rectangleapp.model;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "base",
    "date",
    "time_last_updated",
    "rates"
})
public class Currency {

    @JsonProperty("base")
    private String base;
    @JsonProperty("date")
    private String date;
    @JsonProperty("time_last_updated")
    private Integer timeLastUpdated;
    @JsonProperty("rates")
    private Rates rates;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("base")
    public String getBase() {
        return base;
    }

    @JsonProperty("base")
    public void setBase(String base) {
        this.base = base;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("time_last_updated")
    public Integer getTimeLastUpdated() {
        return timeLastUpdated;
    }

    @JsonProperty("time_last_updated")
    public void setTimeLastUpdated(Integer timeLastUpdated) {
        this.timeLastUpdated = timeLastUpdated;
    }

    @JsonProperty("rates")
    public Rates getRates() {
        return rates;
    }

    @JsonProperty("rates")
    public void setRates(Rates rates) {
        this.rates = rates;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
