package com.jeannaclark.android.stockhawk;

import android.content.ContentProviderOperation;
import android.net.Uri;
import android.util.Log;
import com.jeannaclark.android.stockhawk.data.StockContentProvider;
import com.jeannaclark.android.stockhawk.data.StockDBContract;
import com.jeannaclark.android.stockhawk.model.Chart;
import com.jeannaclark.android.stockhawk.model.Stock;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Updated by jeannaClark on 08/10/16.
 * Created by sam_chordas on 10/8/15.
 */
public class Utility {

  private static String LOG_TAG = Utility.class.getSimpleName();

  public static ArrayList quoteJsonToContentVals(String JSON){
    ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();
    JSONObject jsonObject = null;
    JSONArray resultsArray = null;
    try{
      jsonObject = new JSONObject(JSON);
        jsonObject = jsonObject.getJSONObject("query");
        int count = Integer.parseInt(jsonObject.getString("count"));
        if (count == 1){
          jsonObject = jsonObject.getJSONObject("results")
              .getJSONObject("quote");
          if (jsonObject.getString("Bid").contains("null")) {
            throw new JSONException("JSON object elements are null: " + jsonObject.toString());
          }
          batchOperations.add(buildBatchOperation(jsonObject));
        } else{
          resultsArray = jsonObject.getJSONObject("results").getJSONArray("quote");

          if (resultsArray != null && resultsArray.length() != 0){
            for (int i = 0; i < resultsArray.length(); i++){
              jsonObject = resultsArray.getJSONObject(i);
                batchOperations.add(buildBatchOperation(jsonObject));
            }
          }
        }
    } catch (JSONException e){
      Log.e(LOG_TAG, "String to JSON failed: " + e);
    }
    return batchOperations;
  }

  public static Chart quoteJsonToChart(String JSON) {
    Chart chart = new Chart();
    JSONObject jsonObject = null;
    JSONArray resultsArray = null;


    // TODO: parse chart data & batchInsert into the database



    return chart;
  }

  public static String truncateBidPrice(String bidPrice){
      bidPrice = String.format("%.2f", Float.parseFloat(bidPrice));
      return bidPrice;
  }

  public static String truncateChange(String change, boolean isPercentChange){
    String weight = change.substring(0,1);
    String ampersand = "";
      if (isPercentChange) {
        ampersand = change.substring(change.length() - 1, change.length());
        change = change.substring(0, change.length() - 1);
      }
      change = change.substring(1, change.length());
      double round = (double) Math.round(Double.parseDouble(change) * 100) / 100;
      change = String.format("%.2f", round);
      StringBuffer changeBuffer = new StringBuffer(change);
      changeBuffer.insert(0, weight);
      changeBuffer.append(ampersand);
      change = changeBuffer.toString();
      return change;
  }

  public static ContentProviderOperation buildBatchOperation(JSONObject jsonObject) {
    ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
            StockContentProvider.Quotes.CONTENT_URI);
      try {
          String change = jsonObject.getString("Change");
          builder.withValue(StockDBContract.SYMBOL, jsonObject.getString("symbol"));
          builder.withValue(StockDBContract.BIDPRICE, truncateBidPrice(jsonObject.getString("Bid")));
          builder.withValue(StockDBContract.PERCENT_CHANGE, truncateChange(
                  jsonObject.getString("ChangeinPercent"), true));
          builder.withValue(StockDBContract.CHANGE, truncateChange(change, false));
          builder.withValue(StockDBContract.ISCURRENT, 1);
          if (change.charAt(0) == '-') {
            builder.withValue(StockDBContract.ISUP, 0);
          } else {
            builder.withValue(StockDBContract.ISUP, 1);
          }
          builder.withValue(StockDBContract.NAME, jsonObject.getString("Name"));
          builder.withValue(StockDBContract.EXCHANGE, jsonObject.getString("StockExchange"));
      } catch (JSONException e) {
        e.printStackTrace();
      }
   return builder.build();
  }

  public static String getStockSymbolFromUri(Uri uri) {
    return uri.getPathSegments().get(1);
  }

  public static Stock localizeEgypt(Stock stock) {
    //TODO: add USD to EGP conversion for the Egypt localization
    //  Locale myLocale = getResources().getConfiguration().locale;
    //  Log.i("locale", Currency.getInstance(myLocale).getCurrencyCode());
    //  NumberFormat.getInstance(myLocale);
    //  DateUtils....
    //  NumberFormat.getCurrencyInstance(myLocale);
    //  NumberFormat.getPercentInstance(myLocale);
    // update stock object to the locale formats
    return stock;
  }
}
