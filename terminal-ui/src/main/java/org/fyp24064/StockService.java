package org.fyp24064;

import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StockService {
    // To be replaced with actual server
    private static final String STOCK_PRICE_API = "http://127.0.0.1:8000/stock-price/%s?period=%s";
    private static final String STOCK_NEWS_API = "http://127.0.0.1:8000/stock-news-polygon";
    private static final String STOCK_NEWS_LANGCHAIN_API = "http://127.0.0.1:8000/stock-news-langchain";
    private StockData data;
    private ObjectMapper mapper = new ObjectMapper();

    public StockService() {
        this.data = new StockData();
    }

    public List<StockData.HistoricalQuote> getQuotes(String stockSymbol, String period) throws Exception {
        String content = "";
        try {
            Response response = Request.get(String.format(STOCK_PRICE_API, stockSymbol, period)).execute();
            content = response.returnContent().asString();
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            data.setEntries(mapper.readValue(content, StockData.class).getEntries());
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
        return data.getEntries();
    }

    public List<StockNews.Result> getNews(String stockSymbol) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost postRequest = new HttpPost(STOCK_NEWS_API);

            String jsonBody = String.format("{\"symbol\": \"%s\", \"order\": \"desc\", \"limit\": \"10\"}", stockSymbol);
            StringEntity entity = new StringEntity(jsonBody);
            postRequest.setEntity(entity);
            postRequest.setHeader("Content-type", "application/json");

            return httpClient.execute(postRequest, response -> {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                StockNews stockNews = mapper.readValue(jsonResponse, StockNews.class);
                return stockNews.getResults();
            });
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }
}
