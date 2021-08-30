package com.botann.driverclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.botann.driverclient.R;
import com.botann.driverclient.adapter.adapterinter.CarIdentInterface;
import com.botann.driverclient.model.BindCar;

import java.util.List;

/**
 * created by xuedi on 2018/10/17
 * 显示车牌的adapter
 */
public class CarIdentAdapter extends RecyclerView.Adapter<CarIdentAdapter.CarIdentHolder> {
    private List<BindCar> list;
    private Context context;
    private CarIdentInterface carIdentInterface;
    private int selectedPos = -1;

    public CarIdentAdapter(Context context, List<BindCar> list, CarIdentInterface carIdentInterface) {
        this.list = list;
        this.context = context;
        this.carIdentInterface = carIdentInterface;
    }

    @Override
    public CarIdentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_carident, null);
        CarIdentHolder holder = new CarIdentHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CarIdentAdapter.CarIdentHolder holder, final int position) {
        holder.car_num.setText(list.get(position).getPlate_number());
        holder.car_identstatus.setVisibility(list.get(position).getStatus() == 3 ? View.VISIBLE : View.INVISIBLE);
        if (selectedPos == position){
            holder.img_selected.setVisibility(View.VISIBLE);
        }else {
            holder.img_selected.setVisibility(View.INVISIBLE);
        }
        holder.re_contain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carIdentInterface.itemClick(list.get(position).getId(), list.get(position).getPlate_number(), list.get(position).getVin());
                selectedPos = position;
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class CarIdentHolder extends RecyclerView.ViewHolder {
        private TextView car_num, car_identstatus;
        private RelativeLayout re_contain;
        private ImageView img_selected;

        public CarIdentHolder(View itemView) {
            super(itemView);
            car_num = itemView.findViewById(R.id.car_num);
            car_identstatus = itemView.findViewById(R.id.car_bindstatus);
            re_contain = itemView.findViewById(R.id.re_contain);
            img_selected = itemView.findViewById(R.id.img_selected);
        }
    }
}
