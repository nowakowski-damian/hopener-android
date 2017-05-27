package com.thirteendollars.hopener.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thirteendollars.hopener.R;

import butterknife.ButterKnife;

/**
 * Created by Damian Nowakowski on 02/05/2017.
 * mail: thirteendollars.com@gmail.com
 */

public class DevicePageAdapter extends PagerAdapter {

    private Context mContext;

    public DevicePageAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.page_item,container,false);
        ImageView image = ButterKnife.findById(view,R.id.pager_image);
        TextView text = ButterKnife.findById(view,R.id.pager_text);
        if(position==0) {
            image.setImageResource(R.drawable.ic_fence);
            text.setText(R.string.pager_fence);
            view.setBackground( ContextCompat.getDrawable(mContext,R.drawable.fence_pager_bg) );
        }
        else {
            image.setImageResource(R.drawable.ic_garage);
            text.setText(R.string.pager_garage);
            view.setBackground(ContextCompat.getDrawable(mContext,R.drawable.garage_pager_bg));
        }
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
