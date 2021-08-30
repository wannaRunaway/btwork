package com.kulun.energynet.ui.activity.login;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kulun.energynet.R;
import com.kulun.energynet.databinding.FragmentCarorcitySelectBinding;
import com.kulun.energynet.model.chexing.ChexingContent;

import java.util.List;

/**
 * created by xuedi on 2019/12/24
 */
public class CityOrCartypeSelectDialogFragment extends DialogFragment implements View.OnClickListener {
    private FragmentCarorcitySelectBinding binding;
    private List<CityModel.ContentBean> cityList;
    private List<ChexingContent> chexingList;
    private boolean iscity, ischexing, isjiesuan;
    private int cityId, provinceId, cartypeId;
    private String chexingName = "", cityName = "", messageages="";
    private List<String> jiesuanlist;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_NoActionBar_Fullscreen);
        iscity = getArguments().getBoolean("iscity");
        ischexing = getArguments().getBoolean("ischexing");
        isjiesuan = getArguments().getBoolean("isjiesuan");
        if (iscity) {
            cityList = (List<CityModel.ContentBean>) getArguments().getSerializable("data");
        }
        if (ischexing){
            chexingList = (List<ChexingContent>) getArguments().getSerializable("data");
        }
        if (isjiesuan){
            jiesuanlist = (List<String>) getArguments().getSerializable("data");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carorcity_select, container);
        binding = FragmentCarorcitySelectBinding.bind(view);
        binding.back.setOnClickListener(this);
        binding.right.setOnClickListener(this);
        binding.right.setText("确定");
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //  "plateNumber":"浙A12345",
        //    "vin":"12345678901234567",
        //    "carTypeId":3,// 车型id。app/getCarType接口获取
        //    "provinceId":330000,// 省份id。app/city/getAllCity接口获取
        //    "cityId":330100,// 城市id。app/city/getAllCity接口获取
        if (iscity) {
            binding.title.setText("请选择城市");
            binding.recyclerView.setAdapter(new CarSelectorAdapter());
        }
        if (ischexing){
            binding.title.setText("请选择车型");
            binding.recyclerView.setAdapter(new CarSelectorAdapter());
        }
        if (isjiesuan){
            binding.title.setText("请选择结算方式");
            binding.recyclerView.setAdapter(new JiesuanAdapter());
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                dismissAllowingStateLoss();
                break;
            case R.id.right:
                if (iscity){
                    ((UploadDriverCarInfoActivity) getActivity()).cityclick(cityName ,cityId, provinceId);
                }
                if(ischexing){
                    ((UploadDriverCarInfoActivity) getActivity()).cartypeclick(chexingName, cartypeId);
                }
                if (isjiesuan){
                    ((UploadDriverCarInfoActivity) getActivity()).jiesuaninter(messageages);
                }
                dismissAllowingStateLoss();
                break;
            default:
                break;
        }
    }

    private class CarSelectorAdapter extends RecyclerView.Adapter<CarSelectorAdapter.CarSelectorViewHolder> {
        private int selectedPos = -1;

        @Override
        public CarSelectorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_carident, null);
            CarSelectorAdapter.CarSelectorViewHolder holder = new CarSelectorAdapter.CarSelectorViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(CarSelectorAdapter.CarSelectorViewHolder holder, final int position) {
            if (iscity) {
                final CityModel.ContentBean city = cityList.get(position);
                if (selectedPos == position) {
                    holder.img_selected.setVisibility(View.VISIBLE);
                } else {
                    holder.img_selected.setVisibility(View.INVISIBLE);
                }
                holder.re_contain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cityId = city.getId();
                        provinceId = city.getParentId();
                        cityName = city.getName();
                        selectedPos = position;
                        notifyDataSetChanged();
                    }
                });
                holder.car_num.setText(city.getName());
            } else if (ischexing){
                final ChexingContent chexing = chexingList.get(position);
                if (selectedPos == position) {
                    holder.img_selected.setVisibility(View.VISIBLE);
                } else {
                    holder.img_selected.setVisibility(View.INVISIBLE);
                }
                holder.re_contain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chexingName = chexing.getName();
                        cartypeId = chexing.getId();
                        selectedPos = position;
                        notifyDataSetChanged();
                    }
                });
                holder.car_num.setText(chexing.getName());
            }
        }

        @Override
        public int getItemCount() {
            if (iscity) {
                return cityList.size();
            }
            return chexingList.size();
        }

        class CarSelectorViewHolder extends RecyclerView.ViewHolder {
            private TextView car_num;
            private ImageView img_selected;
            private RelativeLayout re_contain;

            public CarSelectorViewHolder(View itemView) {
                super(itemView);
                car_num = itemView.findViewById(R.id.car_num);
                img_selected = itemView.findViewById(R.id.img_selected);
                re_contain = itemView.findViewById(R.id.re_contain);
            }
        }
    }

    private class JiesuanAdapter extends RecyclerView.Adapter<JiesuanAdapter.JiesuanViewHolder> {
        private int selectedPos = -1;

        @Override
        public JiesuanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_carident, null);
            JiesuanViewHolder holder = new JiesuanViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(JiesuanViewHolder holder, final int position) {
                final String message = jiesuanlist.get(position);
                if (selectedPos == position) {
                    holder.img_selected.setVisibility(View.VISIBLE);
                } else {
                    holder.img_selected.setVisibility(View.INVISIBLE);
                }
                holder.re_contain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        messageages= message;
                        selectedPos = position;
                        notifyDataSetChanged();
                    }
                });
                holder.car_num.setText(message);
        }

        @Override
        public int getItemCount() {
           return jiesuanlist.size();
        }

        class JiesuanViewHolder extends RecyclerView.ViewHolder {
            private TextView car_num;
            private ImageView img_selected;
            private RelativeLayout re_contain;

            public JiesuanViewHolder(View itemView) {
                super(itemView);
                car_num = itemView.findViewById(R.id.car_num);
                img_selected = itemView.findViewById(R.id.img_selected);
                re_contain = itemView.findViewById(R.id.re_contain);
            }
        }
    }

}
