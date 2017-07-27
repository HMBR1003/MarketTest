package org.androidtown.markettest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.androidtown.markettest.databinding.ActivityMainBinding;

@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity implements ScrollTabHolder, ViewPager.OnPageChangeListener {

    public static final boolean NEEDS_PROXY = Integer.valueOf(Build.VERSION.SDK_INT).intValue() < 11;

    private View mHeader;

    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    private int mMinHeaderHeight;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;

    private TextView info;
    private int mLastY;
    int rowWidth;
    public static String marketTel;

    String uid;
    ActivityMainBinding binding;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgress(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("데이터를 불러오는 중입니다...");
        dialog.setCancelable(false);
        dialog.show();

        DisplayMetrics mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
        rowWidth = (mMetrics.widthPixels) / 2;

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.min_header_height);
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mMinHeaderHeight;


        mHeader = findViewById(R.id.header);
        info = (TextView) findViewById(R.id.info);

        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(2);

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.setTabHolderScrollingContent(this);

        mViewPager.setAdapter(mPagerAdapter);

        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(this);
        mLastY = 0;

        uid = "slwVsecqtTO3RDjzPxBWrFekbEd2";
        FirebaseDatabase.getInstance().getReference().child("market").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MarketList market = dataSnapshot.getValue(MarketList.class);

                binding.marketNameText.setText(market.marketName);
                marketTel = market.marketTel;
                binding.tellText.setText(market.marketTel);
                binding.minPriceText.setText(numToWon(Integer.parseInt(market.minPrice))+"원");
                String address1 = market.marketAddress1.substring(7)+ "\n" + market.marketAddress2;
                binding.marketAdressText.setText(address1);
                StorageReference ref = FirebaseStorage.getInstance().getReference().child("market").child(uid).child(uid + ".jpg");

                try {
                    Glide
                            .with(MainActivity.this)
                            .using(new FirebaseImageLoader())
                            .load(ref)
                            .override(300, 300)
                            .signature(new StringSignature(market.aTime))
                            .placeholder(R.drawable.jamsil)
                            .thumbnail(0.1f)
                            .into(binding.marketImage);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "데이터 가져오기 실패 에러 내용 : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public String numToWon(int num){
        String tmp = num+"";
        String won;
        if(tmp.length()>3){
            int a = tmp.length()%3;
            int b = tmp.length()/3;
            if(a!=0) {
                String first = tmp.substring(0, a);
                won = first;
                for(int i =0; i<b; i++){
                    won = won+","+ tmp.substring(a,a+3);
                    a=a+3;
                }
            }
            else{
                a=3;
                String first = tmp.substring(0, a);
                won = first;
                for(int i =0; i<b-1; i++){
                    won = won+","+ tmp.substring(a,a+3);
                    a=a+3;
                }
            }
        }
        else{
            won = tmp;
        }
        return won;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // nothing
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (positionOffsetPixels > 0) {
            int currentItem = mViewPager.getCurrentItem();

            SparseArrayCompat<ScrollTabHolder> scrollTabHolders = mPagerAdapter.getScrollTabHolders();
            ScrollTabHolder currentHolder;

            if (position < currentItem) {
                currentHolder = scrollTabHolders.valueAt(position);
            } else {
                currentHolder = scrollTabHolders.valueAt(position + 1);
            }

            if (NEEDS_PROXY) {
                // TODO is not good
                currentHolder.adjustScroll(mHeader.getHeight() - mLastY);
                mHeader.postInvalidate();
            } else {
                currentHolder.adjustScroll((int) (mHeader.getHeight() + mHeader.getTranslationY()));
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        SparseArrayCompat<ScrollTabHolder> scrollTabHolders = mPagerAdapter.getScrollTabHolders();
        ScrollTabHolder currentHolder = scrollTabHolders.valueAt(position);
        if (NEEDS_PROXY) {
            //TODO is not good
            currentHolder.adjustScroll(mHeader.getHeight() - mLastY);
            mHeader.postInvalidate();
        } else {
            currentHolder.adjustScroll((int) (mHeader.getHeight() + mHeader.getTranslationY()));
        }
    }

    @Override
    public void onScroll(View view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount, int pagePosition) {
        if (mViewPager.getCurrentItem() == pagePosition) {
            int scrollY;
            if (view instanceof AbsListView) {
                scrollY = getScrollY((AbsListView) view);
            } else {
                scrollY = view.getScrollY();
            }
            if (NEEDS_PROXY) {
                //TODO is not good
                mLastY = -Math.max(-scrollY, mMinHeaderTranslation);
                info.setText(String.valueOf(scrollY));
                mHeader.scrollTo(0, mLastY);
                mHeader.postInvalidate();
            } else {
                mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
            }
        }
    }

    @Override
    public void adjustScroll(int scrollHeight) {
        // nothing
    }

    public int getScrollY(AbsListView view) {
        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mHeaderHeight;
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders;
        private final String[] TITLES = {"메뉴","리뷰"};
        private ScrollTabHolder mListener;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            mScrollTabHolders = new SparseArrayCompat<ScrollTabHolder>();
        }

        public void setTabHolderScrollingContent(ScrollTabHolder listener) {
            mListener = listener;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            ScrollTabHolderFragment fragment;
            switch (position) {
                case 0:
                    fragment = (ScrollTabHolderFragment) MenuFragment.newInstance(position,uid,rowWidth);
                    break;
                case 1:
                    fragment = (ScrollTabHolderFragment) ReviewFragment.newInstance(position,uid);
                    break;
                default:
                    fragment = null;
                    break;

            }


            mScrollTabHolders.put(position, fragment);
            if (mListener != null) {
                fragment.setScrollTabHolder(mListener);
            }

            return fragment;
        }

        public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders() {
            return mScrollTabHolders;
        }

    }
}
