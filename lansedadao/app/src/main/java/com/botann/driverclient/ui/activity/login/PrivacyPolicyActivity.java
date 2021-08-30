package com.botann.driverclient.ui.activity.login;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.botann.driverclient.R;
import com.botann.driverclient.databinding.ActivityPrivacyPolicyBinding;

/**
 * created by xuedi on 2020/1/14
 */
public class PrivacyPolicyActivity extends AppCompatActivity {
    private ActivityPrivacyPolicyBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_privacy_policy);
        binding.header.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.header.title.setText("隐私政策");
        String message = "“蓝道能源”隐私政策\n" +
                "用户个人信息保护\n" +
                "1.1  用户在注册帐号或使用本服务的过程中，可能需要填写或提交一些必要的个人信息，如法律法规、规章规范性文件（以下称“法律法规”）规定的需要填写的身份信息。如用户提交的信息不完整或不符合法律法规的规定，则用户可能无法使用本服务或在使用本服务的过程中受到限制。\n" +
                "1.2  蓝道能源在用户注册、使用本服务时收集的个人隐私信息：\n" +
                "1)个人信息：例如手机号、真实姓名、身份证号等信息。\n" +
                "2)支付：用户在“蓝道能源”上支付时，可以选择第三方支付机构（如微信支付、支付宝）所提供的支付服务。支付功能本身并不收集您的个人信息，但我们需要将用户的订单信息及对账信息与这些支付机构共享以确认用户的支付指令并完成支付。\n" +
                "3)客服：当用户与客服取得联系时，系统可能会记录用户与客服之间的通讯记录，以及使用用户的账号信息以便核验身份；当用户需要客服协助修改有关信息时，可能还需要提供上述信息外的其他信息以便完成修改。\n" +
                "4)位置信息：为了便捷用户发现周边的换电站和充电站，蓝道能源会基于用户的地理位置提供服务（LBS）。当用户开启设备定位功能并使用LBS时，蓝道能源可能会收集和使用用户的位置信息，以实现用户换电和充电的目的。\n" +
                "5)设备信息：系统可能会根据用户在软件安装及使用中的具体权限，接收并记录用户在接受服务过程中使用的相关设备信息，例如设备型号、唯一设备标识符、操作系统、分辨率、电信运营商等软硬件信息等。\n" +
                "1.3  蓝道能源未经用户同意不得向任何第三方公开、透露用户个人隐私信息。但以下特定情形除外：\n" +
                "1)蓝道能源根据法律法规规定或有权机关的指示提供用户的个人隐私信息；\n" +
                "2)由于用户将其账户密码告知他人或与他人共享注册帐户与密码，由此导致的任何个人信息的泄漏，或其他非因蓝道能源原因导致的个人隐私信息的泄露；\n" +
                "3)用户自行向第三方公开其个人隐私信息；\n" +
                "4)用户与蓝道能源及合作单位之间就用户个人隐私信息的使用公开达成约定，蓝道能源因此向合作单位公开用户个人隐私信息；\n" +
                "5)任何由于黑客攻击、电脑病毒侵入及其他不可抗力事件导致用户个人隐私信息的泄露；\n" +
                "6)用户个人信息已经经过处理无法识别特定个人且不能复原。\n" +
                "1.4  用户同意蓝道能源可在以下事项中使用用户的个人隐私信息：\n" +
                "1)蓝道能源向用户及时发送重要通知，如软件更新、本协议条款的变更；\n" +
                "2)蓝道能源内部进行审计、数据分析和研究等，以改进蓝道能源的产品、服务和与用户之间的沟通；\n" +
                "3)依本协议约定，蓝道能源管理、审查用户信息及进行处理措施；\n" +
                "4)适用法律法规规定的其他事项。\n";
        binding.textview.setText(message);
    }
}
