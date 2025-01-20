package org.fyp24064;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockData {
    private String symbol;
    private List<HistoricalQuote> entries;

    // method for future use
    public void addEntry(HistoricalQuote entry) {
        entries.add(entry);
    }

    @Getter
    @Setter
    public static class HistoricalQuote {
        private Date date;
        private Double open;
        private Double high;
        private Double low;
        private Double close;
        private Double volume;
    }

    public StockData() {
        this.entries = new ArrayList<>();
    }
}
