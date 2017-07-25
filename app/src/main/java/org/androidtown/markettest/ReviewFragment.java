package org.androidtown.markettest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;

import info.hoang8f.widget.FButton;

/**
 * Created by Administrator on 2017-07-24.
 */

public class ReviewFragment extends ScrollTabHolderFragment{

    private static final String ARG_POSITION = "position";
    private ScrollView scrollView;

    //1
    private ListView mListView;
    private ArrayList<String> mListItems;

    FButton button;

    private int mPosition;
    public static Fragment newInstance(int position) {
        ReviewFragment f = new ReviewFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);

        //2
        //리스트에 들어갈 아이템 세팅
        mListItems = new ArrayList<String>();
        for (int i = 1; i <= 100; i++) {
            mListItems.add(i + ". item - currnet page: " + (mPosition + 1));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_review, container,false);
        button = (FButton)v.findViewById(R.id.f_twitter_button);
        button.setButtonColor(getResources().getColor(R.color.fbutton_color_belize_hole));
        button.setShadowEnabled(true);
        button.setShadowHeight(6);
        button.setCornerRadius(15);
        scrollView = (ScrollView)v.findViewById(R.id.reviewScrollView);

        //3
        //리스트 할당
        mListView = (ListView) v.findViewById(R.id.reviewListView);

//        View placeHolderView = inflater.inflate(R.layout.view_header_placeholder, mListView, false);
//        placeHolderView.setBackgroundColor(0xFFFFFFFF);
//        mListView.addHeaderView(placeHolderView);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                mScrollTabHolder.onScroll(scrollView, 0,0,0, mPosition);
            }
        });

//        //리스트뷰에 스크롤 리스너 부착
//        mListView.setOnScrollListener(new MenuFragment.OnScroll());
//
        //리스트뷰에 어댑터 부착
        mListView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item, android.R.id.text1, mListItems));
//
//        if(MainActivity.NEEDS_PROXY){//in my moto phone(android 2.1),setOnScrollListener do not work well
//            mListView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (mScrollTabHolder != null)
//                        mScrollTabHolder.onScroll(mListView, 0, 0, 0, mPosition);
//                    return false;
//                }
//            });
//        }
    }

    @Override
    public void adjustScroll(int scrollHeight) {
//        if (scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
//            return;
//        }
//
//        mListView.setSelectionFromTop(1, scrollHeight);

    }

    public class OnScroll implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (mScrollTabHolder != null)
                mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, mPosition);
        }
    }


    @Override
    public void onScroll(View view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount, int pagePosition) {
    }
}
