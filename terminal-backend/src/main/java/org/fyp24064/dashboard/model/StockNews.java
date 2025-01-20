package org.fyp24064.dashboard.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class StockNews {
    private List<StockNewsResult> results;
    private String status;
    @JsonProperty("request_id")
    private String requestId;
    private int count;
    @JsonProperty("next_url")
    private String nextURL;
}