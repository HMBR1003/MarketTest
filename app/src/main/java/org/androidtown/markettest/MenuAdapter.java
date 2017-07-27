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

public class MenuAdapter extends BaseAdapter {

    ArrayList<MenuItem> list = new ArrayList<MenuItem>();
    Context context;
    int imageWidth;

    public  MenuAdapter(Context context,int imageWidth){
        this.context = context;
        this.imageWidth = imageWidth;
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
        if(convertView ==null) {
            MenuItem item = list.get(position);
            MenuItemView view = new MenuItemView(context);
            view.setMenuName(item.menuName);
            view.setMenuPrice(item.menuPrice);
            if (item.isMain) {
                view.setIsMainText();
            }
            view.getItemContainer().setLayoutParams(new LinearLayout.LayoutParams(imageWidth - 50, ViewGroup.LayoutParams.WRAP_CONTENT));
//        view.getMenuName().setLayoutParams(new LinearLayout.LayoutParams(imageWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
//        view.getMenuPrice().setLayoutParams(new LinearLayout.LayoutParams(imageWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.getMenuImage().setLayoutParams(new LinearLayout.LayoutParams(imageWidth - 50, imageWidth - 50));

            //이미지 url
            StorageReference ref = FirebaseStorage.getInstance().getReference().child("market").child(item.marketId).child("menu").child(item.menuKey + ".jpg");

            try {
                Glide
                        .with(context)
                        .using(new FirebaseImageLoader())
                        .load(ref)
                        .override(300, 300)
                        .signature(new StringSignature(item.aTime))
                        .placeholder(R.drawable.jamsil)
                        .thumbnail(0.1f)
                        .into(view.getMenuImage());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;
        }
        else{
            return convertView;
        }
    }
}
