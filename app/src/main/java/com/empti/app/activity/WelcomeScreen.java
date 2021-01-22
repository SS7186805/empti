package com.empti.app.activity;

import android.content.Intent;

import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.empti.app.R;
import com.empti.app.adapter.MyCustomPagerAdapter;
import com.viewpagerindicator.LinePageIndicator;

/*
 * setup walkthrough screen
 * */
public class WelcomeScreen extends AppCompatActivity {

    private ViewPager viewPager;
    private ImageView ivMovingSign;
    private LinePageIndicator indicator;
    private int currentPage = 0;
    private TextView tvSkip;
    private MyCustomPagerAdapter myCustomPagerAdapter;
    private int img[] = {R.drawable.intro_one, R.drawable.intro_two, R.drawable.intro_four};
    private int description[] = {R.string.first_intro_description, R.string.welcome_two_descrip, R.string.welcom_fourth_descrip};
    private int title[] = {R.string.welcome_to_takai, R.string.create_a_profile, R.string.ready_to_use};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //call initialize method
        init();
        // printhashkey();
        listener();
    }

    //initialize
    public void init() {
        viewPager = findViewById(R.id.viewpager);
        ivMovingSign = findViewById(R.id.ivMovingSign);
        indicator = findViewById(R.id.indicator);
        tvSkip = findViewById(R.id.tvSkip);
        myCustomPagerAdapter = new MyCustomPagerAdapter(WelcomeScreen.this, img, description, title);
        viewPager.setAdapter(myCustomPagerAdapter);
        viewPager.setCurrentItem(currentPage);
        indicator.setViewPager(viewPager);
    }

    /*  public void printhashkey(){
          try {
              PackageInfo info = getPackageManager().getPackageInfo(
                      "com.app.takai",
                      PackageManager.GET_SIGNATURES);
              for (Signature signature : info.signatures) {
                  MessageDigest md = MessageDigest.getInstance("SHA");
                  md.update(signature.toByteArray());
                  Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
              }
          } catch (PackageManager.NameNotFoundException e) {
          } catch (NoSuchAlgorithmException e) {}
      }
  */
    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    //on click lisner
    public void listener() {
        ivMovingSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage == 2) {
                    Intent intent = new Intent(WelcomeScreen.this, SignUp.class);
                    startActivity(intent);
                } else {
                    viewPager.setCurrentItem(getItem(1), true);
                }
            }
        });
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeScreen.this, SignUp.class);
                startActivity(i);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                currentPage = i;
                if (currentPage == 3) {
                    tvSkip.setVisibility(View.INVISIBLE);
                } else {
                    tvSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int i) {
                currentPage = i;
                if (currentPage == 3) {
                    tvSkip.setVisibility(View.INVISIBLE);
                } else {
                    tvSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }
}
