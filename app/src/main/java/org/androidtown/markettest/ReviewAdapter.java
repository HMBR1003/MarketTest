package org.androidtown.markettest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-07-26-026.
 */

public class ReviewAdapter extends BaseAdapter {

    ArrayList<MenuItem> list = new ArrayList<MenuItem>();
    Context context;

    public ReviewAdapter(Context context){
        this.context = context;
    }

    public void add(MenuItem item){
        list.add(item);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void clear(){
        list.clear();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return convertView;
    }
}
