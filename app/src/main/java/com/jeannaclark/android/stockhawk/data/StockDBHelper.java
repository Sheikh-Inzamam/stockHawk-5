package com.jeannaclark.android.stockhawk.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by sam_chordas on 10/5/15.
 */
@Database(version = StockDBHelper.VERSION)
public class StockDBHelper {
  private StockDBHelper(){}

  public static final int VERSION = 3;

  @Table(StockDBContract.class) public static final String QUOTES = "quotes";
}
