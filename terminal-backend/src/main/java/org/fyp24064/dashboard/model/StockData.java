package org.fyp24064.dashboard.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockData {
    private String symbol;
    private List<HistoricalQuote> entries;

    // method for future use: real time refresh
    public void addEntry(HistoricalQuote entry) {
        entries.add(entry);
    }

    public StockData() {
        this.entries = new ArrayList<>();
    }
}