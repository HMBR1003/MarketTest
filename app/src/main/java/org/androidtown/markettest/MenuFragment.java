//package org.androidtown.markettest;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//import android.widget.AbsListView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.ScrollView;
//
//import java.util.ArrayList;
//
///**
// * Created by Administrator on 2017-07-24.
// */
//
//public class MenuFragment extends ScrollTabHolderFragment{
//
//    private static final String ARG_POSITION = "position";
//    private ScrollView scrollView;
////    private ListView mListView;
////    private ArrayList<String> mListItems;
//
//    private int mPosition;
//    public static Fragment newInstance(int position) {
//        MenuFragment f = new MenuFragment();
//        Bundle b = new Bundle();
//        b.putInt(ARG_POSITION, position);
//        f.setArguments(b);
//        return f;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mPosition = getArguments().getInt(ARG_POSITION);
//
////        //리스트에 들어갈 아이템 세팅
////        mListItems = new ArrayList<String>();
////        for (int i = 1; i <= 100; i++) {
////            mListItems.add(i + ". item - currnet page: " + (mPosition + 1));
////        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_menu, null);
//
//        scrollView = (ScrollView)v.findViewById(R.id.menuScrollView);
//        //리스트 할당
////        mListView = (ListView) v.findViewById(R.id.listView);
////
////        View placeHolderView = inflater.inflate(R.layout.view_header_placeholder, mListView, false);
////        placeHolderView.setBackgroundColor(0xFFFFFFFF);
////        mListView.addHeaderView(placeHolderView);
//
//        return v;
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                mScrollTabHolder.onScroll(scrollView, 0,0,0, mPosition);
//            }
//        });
//
////        //리스트뷰에 스크롤 리스너 부착
////        mListView.setOnScrollListener(new MenuFragment.OnScroll());
////
////        //리스트뷰에 어댑터 부착
////        mListView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item, android.R.id.text1, mListItems));
////
////        if(MainActivity.NEEDS_PROXY){//in my moto phone(android 2.1),setOnScrollListener do not work well
////            mListView.setOnTouchListener(new View.OnTouchListener() {
////                @Override
////                public boolean onTouch(View v, MotionEvent event) {
////                    if (mScrollTabHolder != null)
////                        mScrollTabHolder.onScroll(mListView, 0, 0, 0, mPosition);
////                    return false;
////                }
////            });
////        }
//    }
//
//    @Override
//    public void adjustScroll(int scrollHeight) {
////        if (scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
////            return;
////        }
////
////        mListView.setSelectionFromTop(1, scrollHeight);
//
//    }
//
//    public class OnScroll implements AbsListView.OnScrollListener {
//
//        @Override
//        public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//        }
//
//        @Override
//        public void onScroll(AbsListView view, int firstVisibleItem,
//                             int visibleItemCount, int totalItemCount) {
//            if (mScrollTabHolder != null)
//                mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, mPosition);
//        }
//
//    }
//
//
//    @Override
//    public void onScroll(View view, int firstVisibleItem,
//                         int visibleItemCount, int totalItemCount, int pagePosition) {
//    }
//}
package org.androidtown.markettest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class MenuFragment extends ScrollTabHolderFragment {

    private static final String ARG_POSITION = "position";

    private GridViewWithHeaderAndFooter mListView;

    MenuAdapter adapter;

    private int mPosition;
    String marketId;
    ValueEventListener listener;

    public static Fragment newInstance(int position, String marketId) {
        MenuFragment f = new MenuFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString("marketId", marketId);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);
        marketId = getArguments().getString("marketId");

//        mListItems = new ArrayList<String>();
//
//        for (int i = 1; i <= 100; i++) {
//            mListItems.add(i + ". item - currnet page: " + (mPosition + 1));
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, null);

        adapter = new MenuAdapter(getContext());
        mListView = (GridViewWithHeaderAndFooter) v.findViewById(R.id.menuListView);

        View placeHolderView = inflater.inflate(R.layout.view_header_placeholder, mListView, false);
        placeHolderView.setBackgroundColor(0xFFFFFFFF);
        mListView.addHeaderView(placeHolderView);

        View placefooterView = inflater.inflate(R.layout.view_footer_placeholder, mListView, false);
        placefooterView.setBackgroundColor(0xFFFFFFFF);
        mListView.addFooterView(placefooterView);
        return v;
    }

    @Override
    public void onStop() {
        FirebaseDatabase.getInstance().getReference().child("market").child(marketId).child("menu").removeEventListener(listener);
        super.onStop();
    }

    @Override
    public void onResume() {
        FirebaseDatabase.getInstance().getReference().child("market").child(marketId).child("menu").addValueEventListener(listener);
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView.setOnScrollListener(new OnScroll());
        mListView.setAdapter(adapter);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    adapter.clear();
                    int i = 0;
                    MenuInfo menuInfo;
                    MenuItem items[] = new MenuItem[(int) dataSnapshot.getChildrenCount()];
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        menuInfo = data.getValue(MenuInfo.class);
                        items[i++] = new MenuItem(menuInfo.aTime, menuInfo.menuName, menuInfo.menuPrice, data.getKey(), marketId, menuInfo.isMain, menuInfo.aseq);
                    }
                    //메뉴 순서대로 리스트에 추가
                    for (int iii = 1; iii < dataSnapshot.getChildrenCount() + 1; iii++) {
                        for (int jjj = 0; jjj < dataSnapshot.getChildrenCount(); jjj++) {
                            if (items[jjj].aseq == iii) {
                                adapter.add(items[jjj]);
                                break;
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


//        mListView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item, android.R.id.text1, mListItems));

        if (MainActivity.NEEDS_PROXY) {//in my moto phone(android 2.1),setOnScrollListener do not work well
            mListView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mScrollTabHolder != null)
                        mScrollTabHolder.onScroll(mListView, 0, 0, 0, mPosition);
                    return false;
                }
            });
        }
    }

    @Override
    public void adjustScroll(int scrollHeight) {
        if (scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
            return;
        }
//
//        mListView.setSelectionFromTop(1, scrollHeight);

    }

    public class OnScroll implements OnScrollListener {

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