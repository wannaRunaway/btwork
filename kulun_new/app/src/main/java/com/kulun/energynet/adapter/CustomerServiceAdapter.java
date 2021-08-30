package com.kulun.energynet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kulun.energynet.R;
import com.kulun.energynet.inter.CustomerServiceInterface;
import com.kulun.energynet.model.customerservice.CustomerServicebean;
import com.kulun.energynet.ui.activity.more.CustomerServiceActivity;

import java.util.List;

/**
 * created by xuedi on 2018/11/12
 */
public class CustomerServiceAdapter extends RecyclerView.Adapter<CustomerServiceAdapter.CustomerServiceViewHolder> {
    private Context context;
    private List<CustomerServicebean> list;
    private CustomerServiceInterface anInterface;
    public CustomerServiceAdapter(CustomerServiceActivity customerServiceActivity, List<CustomerServicebean> customerServicebeanList, CustomerServiceInterface anInterface) {
        context = customerServiceActivity;
        list = customerServicebeanList;
        this.anInterface = anInterface;
    }

    @Override
    public CustomerServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_customerservice, null, false);
        CustomerServiceViewHolder holder = new CustomerServiceViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomerServiceViewHolder holder, int position) {
        final CustomerServicebean servicebean = list.get(position);
        holder.tv_name.setText(servicebean.getDescription()+"     "+servicebean.getPhone());
        holder.tv_date.setText(servicebean.getWorkTime());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anInterface.click(servicebean.getDescription(), servicebean.getPhone(), servicebean.getWorkTime());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CustomerServiceViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout relativeLayout;
        private TextView tv_name, tv_date;
        public CustomerServiceViewHolder(View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.re);
            tv_name = itemView.findViewById(R.id.customer_name);
            tv_date = itemView.findViewById(R.id.customer_time);
        }
    }
}
