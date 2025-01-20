package org.fyp24064;

import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.fyp24064.dashboard.model.StockNewsResult;
import org.fyp24064.dashboard.service.StockService;

public class NewsDisplay {
    private StockService stockService;

    public NewsDisplay() {
        this.stockService = new StockService();
    }

    public ScrollPane constructNode(String stockSymbol) {
        VBox newsEntries = new VBox();
        newsEntries.getStyleClass().add("pane");

        try {
            List<StockNewsResult> newsResults = stockService.getNews(stockSymbol);
            if (newsResults == null || newsResults.isEmpty()) {
                return null;
            }
            int newsCount = 1;
            for (StockNewsResult newsEntry : newsResults) {
                newsEntries.getChildren().add(createNewsEntry(newsEntry, stockSymbol, newsCount));
                newsCount++;
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }

        ScrollPane root = new ScrollPane(newsEntries);
        root.getStyleClass().add("pane");
        root.setFitToWidth(true);
        return root;
    }

    private VBox createNewsEntry(StockNewsResult newsEntry, String ticker, int newsCount) {
        Label content = new Label(
                newsCount + ". \""
                + newsEntry.getTitle() + "\"\n"
                + "Description: " + newsEntry.getDescription() + "\n"
                + "Sentiment: " + newsEntry.getSentiment(ticker) + "\n"
                + newsEntry.getSentimentReasoning(ticker) + "\n"
        );
        content.getStyleClass().add("news-content");

        VBox entry = new VBox(content);
        entry.getStyleClass().add("entry");
        return entry;
    }
}
