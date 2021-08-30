package com.botann.driverclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.botann.driverclient.R;
import com.botann.driverclient.inter.PromotionsInterface;
import com.botann.driverclient.model.promotions.Promotions;
import com.botann.driverclient.model.promotions.PromotionsList;
import com.botann.driverclient.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * created by xuedi on 2019/1/17
 */
public class PromotionsAdapter extends RecyclerView.Adapter<PromotionsAdapter.PromotionsViewHolder> {
    private Context context;
    private List<Promotions> list;
    private PromotionsInterface promotionsInterface;

    public PromotionsAdapter(List<Promotions> list, Context promotionsActivity, PromotionsInterface promotionsInterface) {
        this.list = list;
        this.context = promotionsActivity;
        this.promotionsInterface = promotionsInterface;
    }

    @Override
    public PromotionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_promotions, null);
        PromotionsViewHolder holder = new PromotionsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PromotionsViewHolder holder, int position) {
        final Promotions promotions = list.get(position);//活动列表增加活动类型type=10  按次套餐购买
        String type = "";
        switch (promotions.getType()) {
            case 1:
                type = "充值送";
                break;
            case 2:
                type = "消费送";
                break;
            case 3:
                type = "累计消费送";
                break;
            case 4:
                type = "排队送";
                break;
            case 5:
                type = "充电宝";
                break;
            case 6:
                type = "定时任务";
                break;
            case 7:
                type = "里程套餐";
                break;
            case 10:
                type = "按次套餐";
                break;
            case 11:
                type = "费用套餐";
                break;
            default:
                break;
        }
        holder.tv_type.setText(type);
        holder.tv_name.setText(promotions.getName());
        holder.tv_time.setText("活动时间:" + DateUtils.stampToYear(promotions.getStartTime()) + "~~" + DateUtils.stampToYear(promotions.getEndTime()));
        final String finalType = type;
        holder.re_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promotionsInterface.click(promotions, finalType);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PromotionsViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_time, tv_type;
        private RelativeLayout re_content;

        public PromotionsViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            re_content = itemView.findViewById(R.id.re_content);
            tv_type = itemView.findViewById(R.id.tv_type);
        }
    }
}
