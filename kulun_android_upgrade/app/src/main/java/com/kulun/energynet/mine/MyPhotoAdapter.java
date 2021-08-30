package com.kulun.energynet.mine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.kulun.energynet.R;

import java.util.ArrayList;

public class MyPhotoAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list;
    LayoutInflater layoutInflater;
    private ImageView mImageView;
    private ImageView deleteView;

    public MyPhotoAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size() + 1;//注意此处
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.photo_item, null);
        mImageView = (ImageView) convertView.findViewById(R.id.photo_item);
        deleteView = (ImageView) convertView.findViewById(R.id.photo_delete);
        if (position < list.size()) {
            showImage(mImageView, list.get(position));
        }else{
            deleteView.setVisibility(View.INVISIBLE);
            mImageView.setBackgroundResource(R.mipmap.img_refundadd);          //最后一个显示加号图片
        }
        return convertView;
    }

    private void showImage(ImageView view, String imaePath){
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imaePath, opts);

        opts.inSampleSize = computeSampleSize(opts, -1, 56*56);
        //这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inJustDecodeBounds = false;
        try {
            Bitmap bmp = BitmapFactory.decodeFile(imaePath, opts);
            view.setImageBitmap(bmp);
        } catch (OutOfMemoryError err) {
        }
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) &&
                (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

}
