package org.fyp24064.dashboard.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class StockNewsResult {
    private String id;
    private Publisher publisher;
    private String title;
    private String author;
    @JsonProperty("published_utc")
    private String publishedUtc;
    @JsonProperty("article_url")
    private String articleUrl;
    private List<String> tickers;
    @JsonProperty("amp_url")
    private String ampUrl;
    @JsonProperty("image_url")
    private String imageUrl;
    private String description;
    private List<String> keywords;
    private List<Insight> insights;

    public String getSentiment(String ticker) {
        if (insights != null) {
            for (Insight insight : insights) {
                if (insight.getTicker().equals(ticker)) {
                    return insight.getSentiment();
                }
            }
        }
        return null;
    }

    public String getSentimentReasoning(String ticker) {
        if (insights != null) {
            for (Insight insight : insights) {
                if (insight.getTicker().equals(ticker)) {
                    return insight.getSentimentReasoning();
                }
            }
        }
        return null;
    }
}