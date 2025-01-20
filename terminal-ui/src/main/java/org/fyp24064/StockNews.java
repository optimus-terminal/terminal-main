package org.fyp24064;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class StockNews {
    private List<Result> results;
    private String status;
    @JsonProperty("request_id")
    private String requestId;
    private int count;
    @JsonProperty("next_url")
    private String nextURL;

    @Getter
    @Setter
    public static class Result {
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

        public static class Publisher {
            @JsonProperty("name")
            private String name;
            @JsonProperty("homepage_url")
            private String homepageUrl;
            @JsonProperty("logo_url")
            private String logoUrl;
            @JsonProperty("favicon_url")
            private String faviconUrl;
        }

        @Getter
        public static class Insight {
            private String ticker;
            private String sentiment;
            @JsonProperty("sentiment_reasoning")
            private String sentimentReasoning;
        }

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
}