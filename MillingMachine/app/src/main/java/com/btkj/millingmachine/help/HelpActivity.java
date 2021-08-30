package com.btkj.millingmachine.help;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivityHelpBinding;
import com.btkj.millingmachine.homepage.BaseActivity;
import com.btkj.millingmachine.homepage.MainActivity;
import com.btkj.millingmachine.model.config.CompanyHelpList;
import com.btkj.millingmachine.model.config.Config;
import com.btkj.millingmachine.model.config.ConfigModel;
import com.btkj.millingmachine.network.GsonUtils;
import com.btkj.millingmachine.util.TimeFinishInterface;
import com.btkj.millingmachine.util.Utils;
import com.btkj.millingmachine.viewutils.SharePref;
import com.btkj.millingmachine.viewutils.TimeCount;
import java.util.List;
/**
 * created by xuedi on 2019/5/4
 */
public class HelpActivity extends BaseActivity implements View.OnClickListener, HelpInterface, TimeFinishInterface {
    private ActivityHelpBinding binding;
    private List<CompanyHelpList> list;
    private ConfigModel configModel;
    private MyAdapter myAdapter;
    private TimeCount timeCount;
    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_help);
        configModel = (ConfigModel) getIntent().getSerializableExtra("config");
        if (configModel == null) {
            configModel = GsonUtils.getInstance().fromJson((String) SharePref.get(HelpActivity.this, "config", ""), ConfigModel.class);
        }
//        Glide.with(this).load(configModel.getData().getWxPlatformUrl()).into(binding.imgLogo1);
//        Glide.with(this).load(configModel.getData().getWxMiniprogramUrl()).into(binding.imgLogo2);
        list = configModel.getData().getCompanyHelpList();
        binding.center.back.setOnClickListener(this);
        binding.liaojie.setOnClickListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(HelpActivity.this));
        myAdapter = new MyAdapter(list, HelpActivity.this, this);
        binding.recyclerView.setAdapter(myAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeCount = new TimeCount(60000, 1000, binding.center.time, this);
        timeCount.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.liaojie:
                Intent intent = new Intent(HelpActivity.this, HelpDetailsActivity.class);
                intent.putExtra("config", configModel);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void change(int position, boolean isme) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setIsshow(false);
        }
        list.get(position).setIsshow(!isme);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void timeFinish() {
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timeCount.cancel();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<CompanyHelpList> list;
        private Context context;
        private HelpInterface helpInterface;

        public MyAdapter(List<CompanyHelpList> list, HelpActivity helpActivity, HelpInterface helpInterface) {
            this.list = list;
            this.context = helpActivity;
            this.helpInterface = helpInterface;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_help, null);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int i) {
            final CompanyHelpList companyHelpList = list.get(i);
            holder.tv_title.setText(companyHelpList.getTitle());
            if (companyHelpList.isIsshow()) {
                holder.tv_content.setVisibility(View.VISIBLE);
            } else {
                holder.tv_content.setVisibility(View.GONE);
            }
            String string = companyHelpList.getContent().replace("\\n", "\n");
            holder.tv_content.setText(string);
            holder.tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!companyHelpList.getContent().isEmpty()) {
                        helpInterface.change(i, companyHelpList.isIsshow());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
        private class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_title, tv_content;
            public MyViewHolder(View itemView) {
                super(itemView);
                tv_title = (TextView)itemView.findViewById(R.id.tv_title);
                tv_content = (TextView)itemView.findViewById(R.id.tv_content);
            }
        }
    }
    //初始化头部和底部
    @Override
    public void initHeaderBottom() {
        Config config = configModel.getData();
        if (config != null){
            if (config.getIsShowDeviceLogo() == 0) {
                Utils.Glide(this, config.getDeviceLogoImgUrl(), binding.center.imgTopIcon);
            }
//            Utils.Glide(this, config.getLogoImgUrl(), binding.layoutBottom.imgLogoBottom);
//            binding.layoutBottom.tvTitle.setText(config.getLogoTitle());
            binding.center.tvPhone.setText("服务电话："+config.getServiceCall());
        }
    }
}
