package org.fyp24064.dashboard.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoricalQuote {
    private Date date;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double volume;
}