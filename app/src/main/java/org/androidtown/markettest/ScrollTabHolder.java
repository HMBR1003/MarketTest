package org.androidtown.markettest;

import android.view.View;
import android.widget.AbsListView;

public interface ScrollTabHolder {

	void adjustScroll(int scrollHeight);

	void onScroll(View view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition);
}
