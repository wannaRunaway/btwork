package com.botann.driverclient.ui.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.botann.driverclient.R;
import com.botann.driverclient.databinding.FragmentSelectorBinding;
import com.botann.driverclient.model.CouponUserModel;
import java.util.ArrayList;
import java.util.List;
/**
 * created by xuedi on 2019/9/9
 */
public class StationSelectFragment extends DialogFragment {
    private FragmentSelectorBinding binding;
    private List<String> list = new ArrayList<>();
    private CouponUserModel.ContentBean.DataBean dataBean;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_NoActionBar_Fullscreen);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selector, container);
        binding = FragmentSelectorBinding.bind(view);
        binding.title.setText("可用站点");
        binding.left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAllowingStateLoss();
            }
        });
        dataBean = (CouponUserModel.ContentBean.DataBean) getArguments().getSerializable("data");
        if (dataBean.getStationNames() != null){
            list = dataBean.getStationNames();
        }else {
            list.clear();
        }
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(new CarSelectorAdapter());
        return view;
    }

    private class CarSelectorAdapter extends RecyclerView.Adapter<CarSelectorAdapter.CarSelectorViewHolder> {

        @Override
        public CarSelectorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_carselectdialog, null);
            CarSelectorViewHolder holder = new CarSelectorViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(CarSelectorViewHolder holder, final int position) {
            if (list.size() == 0){
                holder.car_num.setText("全部站点都可用");
            }else {
                holder.car_num.setText(list.get(position));
            }
        }

        @Override
        public int getItemCount() {
            if (list.size() == 0){
                return 1;
            }
            return list.size();
        }

        class CarSelectorViewHolder extends RecyclerView.ViewHolder {
            private TextView car_num;
            public CarSelectorViewHolder(View itemView) {
                super(itemView);
                car_num = itemView.findViewById(R.id.car_num);
            }
        }
    }
}
