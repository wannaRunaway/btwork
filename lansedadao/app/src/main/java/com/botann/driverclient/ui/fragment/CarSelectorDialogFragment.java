package com.botann.driverclient.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.botann.driverclient.R;
import com.botann.driverclient.databinding.FragmentCarSelectorBinding;
import com.botann.driverclient.db.SharedPreferencesHelper;
import com.botann.driverclient.inter.PromotionCarConfirm;
import com.botann.driverclient.inter.PromotionCarSelect;
import com.botann.driverclient.model.BindCar;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.ui.activity.chongdian.ChongdianZhongActivity;
import com.botann.driverclient.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * created by xuedi on 2019/1/22
 */
public class CarSelectorDialogFragment extends DialogFragment implements View.OnClickListener {
    private FragmentCarSelectorBinding binding;
    private int id;
    private String plate;
    private boolean isChongdian;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_NoActionBar_Fullscreen);
        isChongdian = getArguments().getBoolean(API.isChongdian);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_selector, container);
        binding = FragmentCarSelectorBinding.bind(view);
        binding.tvBack.setOnClickListener(this);
        binding.tvConfirm.setOnClickListener(this);
        String bindCarlist = "";
        if (isChongdian) {
            bindCarlist = SharedPreferencesHelper.getInstance(getContext()).getAccountString("chargeBindCarList", "");
        } else {
            bindCarlist = SharedPreferencesHelper.getInstance(getContext()).getAccountString("bindCarList", "");
        }
        List<BindCar> bindCars = GsonUtils.getInstance().fromJson(bindCarlist, new TypeToken<List<BindCar>>() {
        }.getType());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(new CarSelectorAdapter(getActivity(), bindCars, new PromotionCarSelect() {
            @Override
            public void click(String plateNum, int bindId) {
                plate = plateNum;
                id = bindId;
            }
        }));
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                dismissAllowingStateLoss();
                break;
            case R.id.tv_confirm:
                if (isChongdian) {
                    ((ChongdianZhongActivity) getActivity()).click(plate, id);
                } else {
                    ((PromotionCarConfirm) getActivity()).click(plate, id);
                }
                dismissAllowingStateLoss();
                break;
            default:
                break;
        }
    }

    private class CarSelectorAdapter extends RecyclerView.Adapter<CarSelectorAdapter.CarSelectorViewHolder> {
        private PromotionCarSelect promotionCarSelect;
        private Context context;
        private List<BindCar> bindCars;
        private int selectedPos = -1;

        public CarSelectorAdapter(Context context, List<BindCar> bindCars, PromotionCarSelect promotionCarSelect) {
            this.context = context;
            this.bindCars = bindCars;
            this.promotionCarSelect = promotionCarSelect;
        }

        @Override
        public CarSelectorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_carident, null);
            CarSelectorViewHolder holder = new CarSelectorViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(CarSelectorViewHolder holder, final int position) {
            final BindCar bindCar = bindCars.get(position);
            if (selectedPos == position) {
                holder.img_selected.setVisibility(View.VISIBLE);
            } else {
                holder.img_selected.setVisibility(View.INVISIBLE);
            }
            holder.re_contain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    promotionCarSelect.click(bindCar.getPlate_number(), bindCar.getId());
                    selectedPos = position;
                    notifyDataSetChanged();
                }
            });
            holder.car_num.setText(bindCar.getPlate_number());
        }

        @Override
        public int getItemCount() {
            return bindCars.size();
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
}
