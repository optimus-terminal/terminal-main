package org.fyp24064.dashboard.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Insight {
    private String ticker;
    private String sentiment;
    @JsonProperty("sentiment_reasoning")
    private String sentimentReasoning;
}