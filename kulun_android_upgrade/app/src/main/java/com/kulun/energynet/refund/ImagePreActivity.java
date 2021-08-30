package com.kulun.energynet.refund;

import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityImagepreBinding;

import java.util.ArrayList;

public class ImagePreActivity extends AppCompatActivity {

    ArrayList<String> urlList;
    private int position;
    private String url;
    private ActivityImagepreBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_imagepre);
        if(getIntent()!=null){
            urlList=getIntent().getStringArrayListExtra("imgList");
            position=getIntent().getIntExtra("position",0);
            url=urlList.get(position);
        }
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                View decorView = getWindow().getDecorView();
                int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                decorView.setSystemUiVisibility(option);
                getWindow().setNavigationBarColor(Color.BLACK);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        binding.tvPosition.setText("1/"+urlList.size());
        binding.vp.setAdapter(new PictureSlidePagerAdapter(getSupportFragmentManager()));
        binding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                url=urlList.get(position);
                binding.tvPosition.setText((position+1)+"/"+urlList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.vp.setCurrentItem(position);

    }

    private  class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {

        public PictureSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PhotoFragment.newInstance(urlList.get(position));//<span style="white-space: pre;">返回展示不同网络图片的PictureSlideFragment</span>
        }

        @Override
        public int getCount() {
            return urlList.size();
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
