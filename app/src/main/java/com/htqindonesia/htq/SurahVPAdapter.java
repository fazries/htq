package com.htqindonesia.htq;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

/**
 * Created by wahyudhzt on 29/05/2016.
 */
public class SurahVPAdapter extends PagerAdapter {

    Context context;
    String[] card;
    LayoutInflater layoutInflater;

    public SurahVPAdapter(Context context, String[] card){
        this.context = context;
        this.card = card;
    }

    @Override
    public int getCount(){
        return card.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.surah_item, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.surah_card);

        try {
            ((BitmapDrawable)imageView.getDrawable()).getBitmap().recycle();
        } catch (NullPointerException e){
            e.printStackTrace();
        }

//        imageView.setImageResource(card[position]);
        Picasso.with(context).load(card[position]).placeholder(R.drawable.loading).into(imageView);

        ((ViewPager) container).addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}
