package com.botann.charing.pad.activity.Main;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import com.botann.charging.pad.R;
import com.botann.charing.pad.model.MainGrid;

/**
 * Created by mengchenyun on 2017/1/16.
 */

public class MainGridAdapter extends ArrayAdapter<MainGrid> {
//    private Context context;
//    private List<MainGrid> mainGrids;
    public MainGridAdapter(Activity context, List<MainGrid> mainGrids) {
        super(context, 0, mainGrids);
//        this.context = context;
//        this.mainGrids = mainGrids;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MainGrid mainGrid = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.main_grid_item, null, false);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.background_image);
            try {
                imageView.setImageBitmap(BitmapFactory.decodeStream(getContext().getAssets().open(mainGrid.getBackground())));
            } catch (IOException e) {
                e.printStackTrace();
            }

            TextView name = (TextView) convertView.findViewById(R.id.tv_name);
            name.setText(mainGrid.getName());
        }
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        convertView.setLayoutParams(new GridView.LayoutParams(params));
        return convertView;
    }


}
