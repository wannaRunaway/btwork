package com.btkj.chongdianbao.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.FragmentImgBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.concurrent.ExecutionException;

import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.ZOOM_FOCUS_CENTER_IMMEDIATE;

/**
 * Created by Administrator on 2018/12/13.
 */

public class PhotoFragment extends Fragment {
    private String url;
    //private SubsamplingScaleImageView imageView;
    private FragmentImgBinding binding;
    /**
     * 获取这个fragment需要展示图片的url
     * @param url
     * @return
     */
    public static PhotoFragment newInstance(String url) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString("url");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_img, container, false);
        binding=FragmentImgBinding.bind(view);
        //设置缩放类型，默认ScaleType.CENTER（可以不设置）
        binding.photoview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    getActivity().finish();
                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
        binding.photoview.setMaxScale(15);
        binding.photoview.setZoomEnabled(true);
        binding.photoview.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);

        Glide.with(getContext()).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                int sWidth = resource.getWidth();
                int sHeight = resource.getHeight();
                WindowManager wm = (WindowManager) getContext()
                        .getSystemService(Context.WINDOW_SERVICE);
                int height = wm.getDefaultDisplay().getHeight();
                if (sHeight >= height
                        && sHeight / sWidth >=3) {
                    binding.photoview.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                    binding.photoview.setImage(ImageSource.bitmap(resource), new ImageViewState(2.0F, new PointF(0, 0), 0));
                }else {
                    binding.photoview.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
                    binding.photoview.setImage(ImageSource.bitmap(resource));
                    binding.photoview.setDoubleTapZoomStyle(ZOOM_FOCUS_CENTER_IMMEDIATE);
                }
            }
        });

        /*Glide.with(getContext())
                .asBitmap()
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        //图片加载错误，没研究怎么判断异常类型，没有服务器配合研究
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        //resource就是得到的bitmap
                        int sWidth = resource.getWidth();
                        int sHeight = resource.getHeight();
                        WindowManager wm = (WindowManager) getContext()
                                .getSystemService(Context.WINDOW_SERVICE);
                        int height = wm.getDefaultDisplay().getHeight();
                        if (sHeight >= height
                                && sHeight / sWidth >=3) {
                            binding.photoview.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                            binding.photoview.setImage(ImageSource.bitmap(resource), new ImageViewState(2.0F, new PointF(0, 0), 0));
                        }else {
                            binding.photoview.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
                            binding.photoview.setImage(ImageSource.bitmap(resource));
                            binding.photoview.setDoubleTapZoomStyle(ZOOM_FOCUS_CENTER_IMMEDIATE);
                        }
                        return false;
                    }

                })
                .load(url)
                .preload(500, 500);*/

        return view;
    }

}
