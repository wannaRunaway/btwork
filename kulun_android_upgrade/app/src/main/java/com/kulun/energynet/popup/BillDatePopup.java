package com.kulun.energynet.popup;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.kulun.energynet.R;
import com.kulun.energynet.databinding.PopupBillDateBinding;
import com.kulun.energynet.utils.DateUtils;
import com.kulun.energynet.utils.Utils;
import java.util.ArrayList;
import razerdp.basepopup.BasePopupWindow;

public class BillDatePopup extends BasePopupWindow {
    private Popdateclick popclick;
    private ArrayList<String> yearlist;
    private ArrayList<String> monthlist;
    private String year = "", month = "";
    private Activity activity;

    public BillDatePopup(Activity context, Popdateclick popclick) {
        super(context);
        setPopupGravity(Gravity.TOP);
        this.popclick = popclick;
        this.activity = context;
    }

    public interface Popdateclick {
        void datepopclick(String time);
    }

    /**
     * 设置点击事件
     */
    public PopupBillDateBinding binding;

    // 必须实现，这里返回您的contentView
    // 为了让库更加准确的做出适配，强烈建议使用createPopupById()进行inflate
    @Override
    public View onCreateContentView() {
        View view = createPopupById(R.layout.popup_bill_date);
        binding = PopupBillDateBinding.bind(view);
        binding.header.title.setText("我的账单");
        binding.header.left.setOnClickListener(v -> dismiss());
        year = String.valueOf(DateUtils.getYear());
        month = String.valueOf(DateUtils.getMonth());
        initYearWheel();
        initMonthWheel();
        binding.all.setOnClickListener(v -> {
            popclick.datepopclick("");
            dismiss();
        });
        binding.time.setText(year + "年" + month + "月");
        binding.chaxun.setOnClickListener(v -> {
            if (binding.time.getText().toString().indexOf("年") == -1 || binding.time.getText().toString().indexOf("月") == -1) {
                Utils.snackbar(activity, "请选择日期");
                return;
            }
            popclick.datepopclick(Utils.getDate(binding.time.getText().toString()));
            dismiss();
        });
        return view;
    }

    private void initMonthWheel() {
        binding.month.setCyclic(true);
        monthlist = new ArrayList<>();
        monthlist.add("1月");
        monthlist.add("2月");
        monthlist.add("3月");
        monthlist.add("4月");
        monthlist.add("5月");
        monthlist.add("6月");
        monthlist.add("7月");
        monthlist.add("8月");
        monthlist.add("9月");
        monthlist.add("10月");
        monthlist.add("11月");
        monthlist.add("12月");
        binding.month.setAdapter(new ArrayWheelAdapter(monthlist));
        binding.month.setCurrentItem(getPosition(month+"月", monthlist));
        binding.month.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (year == null || year.equals("")) {
                    year = DateUtils.getYear()+"年";
                }
                month = monthlist.get(index);
                binding.time.setText(year + month);
            }
        });
    }

    private void initYearWheel() {
        binding.year.setCyclic(true);
        yearlist = new ArrayList<>();
        yearlist.add("2015年");
        yearlist.add("2016年");
        yearlist.add("2017年");
        yearlist.add("2018年");
        yearlist.add("2019年");
        yearlist.add("2020年");
        yearlist.add("2021年");
        yearlist.add("2022年");
        yearlist.add("2023年");
        yearlist.add("2024年");
        yearlist.add("2025年");
        binding.year.setAdapter(new ArrayWheelAdapter(yearlist));
        binding.year.setCurrentItem(getPosition(year+"年", yearlist));
        binding.year.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (month == null || month.equals("")) {
                    month = DateUtils.getMonth()+"月";
                }
                year = yearlist.get(index);
                binding.time.setText(year + month);
            }
        });
    }

    private int getPosition(String value, ArrayList<String> list){
        int position = 0;
        for (int i = 0; i < list.size(); i++) {
            if (value.equals(list.get(i))){
                position = i;
            }
        }
        return position;
    }
}
