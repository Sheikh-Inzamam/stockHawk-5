package com.jeannaclark.android.stockhawk.data;

import android.net.Uri;
import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by sam_chordas on 10/5/15.
 */
@ContentProvider(authority = StockContentProvider.AUTHORITY, database = StockDBHelper.class)
public class StockContentProvider {
  public static final String AUTHORITY = "com.jeannaclark.android.stockhawk.data.StockContentProvider";

  static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

  interface Path{
    String QUOTES = "quotes";
  }

  private static Uri buildUri(String... paths){
    Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
    for (String path:paths){
      builder.appendPath(path);
    }
    return builder.build();
  }

  @TableEndpoint(table = StockDBHelper.QUOTES)

  public static class Quotes{
    @ContentUri(
        path = Path.QUOTES,
        type = "vnd.android.cursor.dir/quote",
        defaultSort = StockDBContract.NAME + " ASC"
    )
    public static final Uri CONTENT_URI = buildUri(Path.QUOTES);

    @InexactContentUri(
        name = "QUOTE_ID",
        path = Path.QUOTES + "/*",
        type = "vnd.android.cursor.item/quote",
        defaultSort = StockDBContract.NAME + " ASC",
        whereColumn = StockDBContract.SYMBOL,
        pathSegment = 1
    )
    public static Uri withSymbol(String symbol){
      return buildUri(Path.QUOTES, symbol);
    }
  }
}
