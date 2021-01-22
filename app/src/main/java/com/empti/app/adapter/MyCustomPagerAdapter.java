package com.empti.app.adapter;

import android.content.Context;

import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.empti.app.R;

public class MyCustomPagerAdapter extends PagerAdapter {
    Context context;
    int images[];
    int description[];
    int title[];
    LayoutInflater layoutInflater;
    private ImageView ivWelcomeScreen;
    private TextView tvTitle, tvDescrip;

    // constructor
    public MyCustomPagerAdapter(Context context, int images[], int description[], int title[]) {
        this.context = context;
        this.images = images;
        this.description = description;
        this.title = title;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.welcom_screens, container, false);
        ivWelcomeScreen = itemView.findViewById(R.id.ivWelcomeScreen);
        ivWelcomeScreen.setImageResource(images[position]);
        tvDescrip = itemView.findViewById(R.id.tvDescrip);
        tvDescrip.setText(description[position]);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvTitle.setText(title[position]);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
