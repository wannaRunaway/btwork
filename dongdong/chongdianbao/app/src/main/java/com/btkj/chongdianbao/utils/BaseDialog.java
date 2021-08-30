package com.btkj.chongdianbao.utils;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.model.User;

/**
 * created by xuedi on 2019/8/16
 */
public class BaseDialog {
    public static void showDialog(Context context, String name, View.OnClickListener confirmListener){
        AlertDialog alertDialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_base, null);
        builder.setView(view);
        alertDialog = builder.create();
        TextView title = view.findViewById(R.id.tv_title);
        ImageView cancel = view.findViewById(R.id.img_cancel);
        ImageView confirm = view.findViewById(R.id.img_confirm);
        title.setText(name);
        AlertDialog finalAlertDialog = alertDialog;
        cancel.setOnClickListener(view1 -> {
            finalAlertDialog.dismiss();
        });
        AlertDialog finalAlertDialog1 = alertDialog;
        confirm.setOnClickListener(view1 -> {
            confirmListener.onClick(view1);
            finalAlertDialog1.dismiss();
        });
        alertDialog.show();
    }

    public static void showImageDialog(Context context){
        AlertDialog alertDialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_image, null);
        builder.setView(view);
        alertDialog = builder.create();
        ImageView imageView = view.findViewById(R.id.image);
        imageView.setImageBitmap(QRCodeUtil.createQRCode(API.qrinfo+String.valueOf(User.getInstance().getAccountNo()), 300, 300));
        alertDialog.show();
    }
}
