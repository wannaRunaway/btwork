package com.botann.driverclient.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.botann.driverclient.R;
import com.botann.driverclient.databinding.ActivityUploadDrivercarinfoBinding;
import com.botann.driverclient.databinding.ActivityUploadDrivercarinfoSuccessBinding;
import com.botann.driverclient.model.User;
import com.botann.driverclient.model.chexing.ChexingModel;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.ui.activity.chongdian.ChongdianZhongActivity;
import com.botann.driverclient.ui.activity.more.PersonalActivity;
import com.botann.driverclient.utils.GsonUtils;
import com.botann.driverclient.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * created by xuedi on 2019/12/24
 */
public class UploadDriverCarInfoActivity extends AppCompatActivity implements View.OnClickListener, CartypeSelect, CitySelect, JiesuanSelectInterface {
    private ActivityUploadDrivercarinfoBinding binding;
    private ActivityUploadDrivercarinfoSuccessBinding successBinding;
    private CityOrCartypeSelectDialogFragment cityOrCartypeSelectDialogFragment;
    private String phone;
    private boolean islogin;
    private DriverinfoBean data;
    private boolean success = false;
    private List<String> jiesuanlist = new ArrayList<>();
    private int chengzuid = -1;
    private int type, id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = (DriverinfoBean) getIntent().getSerializableExtra("data");
        success = getIntent().getBooleanExtra("success", false);
        type = getIntent().getIntExtra(API.type, 0);
        islogin = getIntent().getBooleanExtra(API.islogin, false);
        id = getIntent().getIntExtra("id", 0);
        if (data == null) { //没有提交
            binding = DataBindingUtil.setContentView(this, R.layout.activity_upload_drivercarinfo);
            binding.header.title.setText("上传个人及车辆信息");
            binding.header.back.setOnClickListener(this);
            binding.tvNext.setOnClickListener(this);
            binding.l5.setOnClickListener(this);
            binding.l6.setOnClickListener(this);
            binding.l7.setOnClickListener(this);
            phone = getIntent().getStringExtra("phone");
            jiesuanlist.add("司机个人支付");
            jiesuanlist.add("承租车主统一支付");
            binding.rbYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chengzuid = 1;
                    binding.rbYes.setChecked(true);
                    binding.rbNo.setChecked(false);
                }
            });
            binding.rbNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chengzuid = 0;
                    binding.rbYes.setChecked(false);
                    binding.rbNo.setChecked(true);
                }
            });
            binding.rbXuzhi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.rbXuzhi.setChecked(true);
                    Intent intent = new Intent(UploadDriverCarInfoActivity.this, UserprotocolActivity2.class);
                    startActivity(intent);
                }
            });
            cityOrCartypeSelectDialogFragment = new CityOrCartypeSelectDialogFragment();
        } else { //提交审核
            if (success){//审核成功
                successBinding = DataBindingUtil.setContentView(this, R.layout.activity_upload_drivercarinfo_success);
                successBinding.header.title.setText("上传个人及车辆信息");
                successBinding.header.back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                successBinding.etName.setText(data.getName() + "");
                successBinding.etIdcard.setText(data.getIdentity() + "");
                successBinding.etCarplate.setText(data.getPlateNumber() + "");
                successBinding.etChejia.setText(data.getVin() + "");
                successBinding.tvChushilicheng.setText(data.getFirstMiles() + "");
                successBinding.tvCity.setText(data.getCityName() + "");
                successBinding.tvCartype.setText(data.getCarTypeName() + "");
            }else {//审核失败
                binding = DataBindingUtil.setContentView(this, R.layout.activity_upload_drivercarinfo);
                binding.header.title.setText("上传个人及车辆信息");
                binding.header.back.setOnClickListener(this);
                binding.tvNext.setOnClickListener(this);
                binding.l5.setOnClickListener(this);
                binding.l6.setOnClickListener(this);
                binding.l7.setOnClickListener(this);
                phone = getIntent().getStringExtra("phone");
                jiesuanlist.add("司机个人支付");
                jiesuanlist.add("承租车主统一支付");
                binding.rbYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chengzuid = 1;
                        binding.rbYes.setChecked(true);
                        binding.rbNo.setChecked(false);
                    }
                });
                binding.rbNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chengzuid = 0;
                        binding.rbYes.setChecked(false);
                        binding.rbNo.setChecked(true);
                    }
                });
                binding.rbXuzhi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.rbXuzhi.setChecked(true);
                        Intent intent = new Intent(UploadDriverCarInfoActivity.this, UserprotocolActivity2.class);
                        startActivity(intent);
                    }
                });
                cityOrCartypeSelectDialogFragment = new CityOrCartypeSelectDialogFragment();
                binding.etName.setText(data.getName()+"");
                binding.etIdcard.setText(data.getIdentity()+"");
                binding.etCarplate.setText(data.getPlateNumber()+"");
                binding.etChejia.setText(data.getVin()+"");
                binding.tvCartype.setText(data.getCarTypeName()+"");
                binding.tvCity.setText(data.getCityName()+"");
                binding.tvChushilicheng.setText(data.getFirstMiles()+"");
                /**
                 * intent.putExtra("carTypeId", cartypeid);
                 *         intent.putExtra("provinceId", provinceid);
                 *         intent.putExtra("cityId", cityid);
                 */
                cartypeid = data.getCarTypeId();
                provinceid = data.getProvinceId();
                cityid = data.getCityId();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_next:
                next();
                break;
            case R.id.l5://车型
                if (isFastDoubleClickl5()) {
                    Utils.toast(UploadDriverCarInfoActivity.this, "按钮点击过快");
                    return;
                }
                chexingload();
                break;
            case R.id.l6://城市
                if (isFastDoubleClickl6()) {
                    Utils.toast(UploadDriverCarInfoActivity.this, "按钮点击过快");
                    return;
                }
                cityload();
                break;
            case R.id.l7://结算方式
                if (isFastDoubleClickl7()) {
                    Utils.toast(UploadDriverCarInfoActivity.this, "按钮点击过快");
                    return;
                }
                jiesuan();
                break;
            default:
                break;
        }
    }

    private long lastClickTimel5, lastClickTimel6, lastClickTimel7;
    public boolean isFastDoubleClickl5() {
        long time = System.currentTimeMillis(); // 此方法仅用于Android
        if (time - lastClickTimel5 < 2000) {
            return true;
        }
        lastClickTimel5 = time;
        return false;
    }
    public boolean isFastDoubleClickl6() {
        long time = System.currentTimeMillis(); // 此方法仅用于Android
        if (time - lastClickTimel6 < 2000) {
            return true;
        }
        lastClickTimel6 = time;
        return false;
    }
    public boolean isFastDoubleClickl7() {
        long time = System.currentTimeMillis(); // 此方法仅用于Android
        if (time - lastClickTimel7 < 2000) {
            return true;
        }
        lastClickTimel7 = time;
        return false;
    }
    private void jiesuan() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", (Serializable) jiesuanlist);
        bundle.putBoolean("isjiesuan", true);
        cityOrCartypeSelectDialogFragment.setArguments(bundle);
        cityOrCartypeSelectDialogFragment.show(getSupportFragmentManager(), "data");
    }

    /**
     * "id":1,// 我的-》上传司机车辆信息 修改申请信息
     * "accountId":1,// 我的-》上传司机车辆信息 新增申请信息
     * "phone":"18329112222",// 注册新用户 新增申请信息
     * "businessType":6,// 6自营出租车, 99其他。app/dictionary/getByTypeValue接口获取// businessType=6 以下信息必须填写
     * "name":"张三",// 司机名称
     * "identity":"353100201912171111",// 身份证
     * "plateNumber":"浙A12345",
     * "vin":"12345678901234567",
     * "carTypeId":3,// 车型id。app/getCarType接口获取
     * "provinceId":330000,// 省份id。app/city/getAllCity接口获取
     * "cityId":330100,// 城市id。app/city/getAllCity接口获取
     * "photo":"key;key2;key2;key3"// 图片key
     */
    private void next() {
        if (isEmpty(phone)) {
            phone = User.getInstance().getPhone();
        }
        if (isEmpty(binding.etName.getText().toString())) {
            Utils.toast(UploadDriverCarInfoActivity.this, "请输入姓名");
            return;
        }
        if (!Utils.checkname(binding.etName.getText().toString())){
            Utils.toast(UploadDriverCarInfoActivity.this, "姓名不能包含特殊字符");
            return;
        }
        if (isEmpty(binding.etIdcard.getText().toString())) {
            Utils.toast(UploadDriverCarInfoActivity.this, "请输入身份证号");
            return;
        }
        if (!Utils.IDCardValidate(binding.etIdcard.getText().toString())){
            Utils.toast(UploadDriverCarInfoActivity.this, "身份证号格式不正确");
            return;
        }
        if (isEmpty(binding.etCarplate.getText().toString())) {
            Utils.toast(UploadDriverCarInfoActivity.this, "请输入车牌号");
            return;
        }
        if (binding.etCarplate.getText().toString().length() > 8) {
            Utils.toast(this, "车牌号码位数不能超过8位");
            return;
        }
        if (binding.etCarplate.getText().toString().length() < 7) {
            Utils.toast(this, "车牌号码位数不能小于7位");
            return;
        }
        if (!Utils.checkCarNumber(binding.etCarplate.getText().toString())) {
            Utils.toast(this, "车牌含有特殊字符");
            return;
        }
        if (isEmpty(binding.etChejia.getText().toString())) {
            Utils.toast(UploadDriverCarInfoActivity.this, "请输入车架号");
            return;
        }
        if (isEmpty(String.valueOf(cartypeid))) {
            Utils.toast(UploadDriverCarInfoActivity.this, "请选择车型");
            return;
        }
        if (isEmpty(String.valueOf(provinceid)) || isEmpty(String.valueOf(cityid))) {
            Utils.toast(UploadDriverCarInfoActivity.this, "请选择城市");
            return;
        }
        if (isEmpty(binding.tvChushilicheng.getText().toString())) {
            Utils.toast(UploadDriverCarInfoActivity.this, "请输入初始里程");
            return;
        }
        if (jiesuanid == -1) {
            Utils.toast(UploadDriverCarInfoActivity.this, "请选择结算方式");
            return;
        }
        if (chengzuid == -1) {
            Utils.toast(UploadDriverCarInfoActivity.this, "是否是承租人");
            return;
        }
        if (!binding.rbXuzhi.isChecked()){
            Utils.toast(UploadDriverCarInfoActivity.this, "请阅读并勾选出租车换电须知");
            return;
        }
        Intent intent = new Intent(UploadDriverCarInfoActivity.this, UploadPhotoActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("name", binding.etName.getText().toString());
        intent.putExtra("identity", binding.etIdcard.getText().toString());
        intent.putExtra("plateNumber", binding.etCarplate.getText().toString());
        intent.putExtra("vin", binding.etChejia.getText().toString());
        intent.putExtra("carTypeId", cartypeid);
        intent.putExtra("provinceId", provinceid);
        intent.putExtra("cityId", cityid);
        intent.putExtra("licheng", binding.tvChushilicheng.getText().toString());
        if (id != 0){
            intent.putExtra("id", id);
        }
        if (data != null && data.getId() != 0) {
            intent.putExtra("id", data.getId());
        }
        intent.putExtra("settleType", jiesuanid);
        intent.putExtra("role", chengzuid);
        intent.putExtra(API.islogin, islogin);
        intent.putExtra(API.type, type);
        startActivity(intent);
    }

    private boolean isEmpty(String message) {
        if (message == null || message.equals("")) {
            return true;
        }
        return false;
    }

    private void cityload() {
        final String url = API.BASE_URL + API.GETALLCITY;
        final RequestParams requestParams = new RequestParams();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(persistentCookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                CityModel cityModel = GsonUtils.getInstance().fromJson(json, CityModel.class);
                if (cityModel.isSuccess()) {
//                    initAdapter(chexingModel.getContent());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) cityModel.getContent());
                    bundle.putBoolean("iscity", true);
                    cityOrCartypeSelectDialogFragment.setArguments(bundle);
                    cityOrCartypeSelectDialogFragment.show(getSupportFragmentManager(), "data");
                } else {
                    Utils.toast(UploadDriverCarInfoActivity.this, cityModel.getMsg());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                Utils.toast(UploadDriverCarInfoActivity.this, API.error_internet);
            }
        });
    }

    private void chexingload() {
        final String url = API.BASE_URL + API.GET_CAR_TYPE;
        final RequestParams requestParams = new RequestParams();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(persistentCookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                ChexingModel chexingModel = GsonUtils.getInstance().fromJson(json, ChexingModel.class);
                if (chexingModel.isSuccess()) {
//                    initAdapter(chexingModel.getContent());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) chexingModel.getContent());
                    bundle.putBoolean("ischexing", true);
                    cityOrCartypeSelectDialogFragment.setArguments(bundle);
                    cityOrCartypeSelectDialogFragment.show(getSupportFragmentManager(), "data");
                } else {
                    Utils.toast(UploadDriverCarInfoActivity.this, chexingModel.getMsg());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.toast(UploadDriverCarInfoActivity.this, API.error_internet);
            }
        });
    }

    private int cityid, provinceid, cartypeid;
    private int jiesuanid = -1;

    @Override
    public void cartypeclick(String chexinName, int cartypeid) {
        this.cartypeid = cartypeid;
        if (chexinName!=null) {
            binding.tvCartype.setText(chexinName + "");
        }
    }

    @Override
    public void cityclick(String cityName, int cityid, int provinceId) {
        this.cityid = cityid;
        this.provinceid = provinceId;
        if (cityName != null) {
            binding.tvCity.setText(cityName + "");
        }
    }

    @Override
    public void jiesuaninter(String message) {
        /**
         * jiesuanlist.add("司机个人支付");
         *         jiesuanlist.add("承租车主统一支付");
         */
        binding.tvJiesuan.setText(message + "");
        switch (message) {
            case "司机个人支付":
                jiesuanid = 0;
                break;
            case "承租车主统一支付":
                jiesuanid = 1;
                break;
            default:
                break;
        }
    }
}
