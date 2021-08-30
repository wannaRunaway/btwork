package com.kulun.kulunenergy.main;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.FragmentCarSelectorBinding;
import com.kulun.kulunenergy.modelnew.StationInfo;
import com.kulun.kulunenergy.utils.API;

import java.util.ArrayList;
import java.util.List;

/**
 * created by xuedi on 2019/9/9
 */
public class StationSelectFragment extends DialogFragment {
    private FragmentCarSelectorBinding binding;
    private List<StationInfo> list;
    private List<StationInfo> changelist = new ArrayList<>();
    private boolean ismain = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_NoActionBar_Fullscreen);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_selector, container);
        binding = FragmentCarSelectorBinding.bind(view);
        ismain = getArguments().getBoolean(API.ismain);
        binding.header.title.setText("搜索站点");
        binding.header.left.setOnClickListener(view1 -> {
            if (ismain) {
                ((MainActivity) getActivity()).click(null);
            }else {
                ((StationListActivity) getActivity()).click(null);
            }
            dismissfragment();
        });
        list = (List<StationInfo>) getArguments().getSerializable(API.station);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(new CarSelectorAdapter(getContext(), list));
        binding.tvSearch.setOnClickListener(view1 -> {
            String text = binding.etStation.getText().toString();
            if (text.isEmpty()){
                binding.recyclerView.setAdapter(new CarSelectorAdapter(getContext(), list));
                return;
            }
            changelist.clear();
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getStationName().indexOf(text) != -1){
                    changelist.add(list.get(j));
                }
            }
            binding.recyclerView.setAdapter(new CarSelectorAdapter(getContext(), changelist));
        });
        return view;
    }

    /**
     * 获取状态栏高度（单位：px）
     */
    /*public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId) == 0 ? 60 : resources.getDimensionPixelSize(resourceId);
    }*/
    private void dismissfragment(){
        binding.etStation.setText("");
        binding.recyclerView.setAdapter(new CarSelectorAdapter(getContext(), list));
        super.dismissAllowingStateLoss();
    }

    private class CarSelectorAdapter extends RecyclerView.Adapter<CarSelectorAdapter.CarSelectorViewHolder> {
        private Context context;
        private List<StationInfo> stationList;
        public CarSelectorAdapter(Context context, List<StationInfo> list) {
            this.context = context;
            this.stationList = list;
        }
        @Override
        public CarSelectorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_carselectdialog, null);
            CarSelectorViewHolder holder = new CarSelectorViewHolder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(CarSelectorViewHolder holder, int position) {
            holder.re_contain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ismain) {
                        ((MainActivity) getActivity()).click(stationList.get(position));
                    }else {
                        ((StationListActivity) getActivity()).click(stationList.get(position));
                    }
                    dismissfragment();
                }
            });
            holder.car_num.setText(stationList.get(position).getStationName());
        }
        @Override
        public int getItemCount() {
            return stationList.size();
        }
        class CarSelectorViewHolder extends RecyclerView.ViewHolder {
            private TextView car_num;
            private LinearLayout re_contain;
            public CarSelectorViewHolder(View itemView) {
                super(itemView);
                car_num = itemView.findViewById(R.id.tv_station);
                re_contain = itemView.findViewById(R.id.re_contain);
            }
        }
    }
}
