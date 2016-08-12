package com.jeannaclark.android.stockhawk.touch_helper;

import android.net.Uri;

/**
 * * Updated by jeannaClark on 08/10/16.
 * Created by sam_chordas on 10/6/15.
 * credit to Paul Burke (ipaulpro)
 * Interface for enabling swiping to delete
 */
public interface ItemTouchHelperViewHolder {
  void onItemSelected(Uri stockSymbolUri);


  void onItemSelected();
  //TODO: confirm this onItemSelected() is properly overloaded

  void onItemClear();
}

