package com.kulun.energynet.utils;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.kulun.energynet.R;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * created by xuedi on 2019/8/16
 */
public class BaseDialog {
    public static void showDialog(Context context, String name, View.OnClickListener confirmListener){
        AlertDialog alertDialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.mydialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_base, null);
        builder.setView(view);
        alertDialog = builder.create();
        TextView title = view.findViewById(R.id.tv_title);
        TextView cancel = view.findViewById(R.id.img_cancel);
        TextView confirm = view.findViewById(R.id.img_confirm);
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

    public static void showcodeDialog(Context context, String name, View.OnClickListener confirmListener){
        AlertDialog alertDialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.mydialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_code, null);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        TextView title = view.findViewById(R.id.tv_title);
        TextView confirm = view.findViewById(R.id.img_confirm);
        title.setText(name);
        AlertDialog finalAlertDialog1 = alertDialog;
        confirm.setOnClickListener(view1 -> {
            confirmListener.onClick(view1);
            finalAlertDialog1.dismiss();
        });
        alertDialog.show();
    }

    public static void showQrDialog(Activity context, String carplate){
        AlertDialog alertDialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.mydialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_image, null);
        builder.setView(view);
        alertDialog = builder.create();
        ImageView imageView = view.findViewById(R.id.image);
        TextView textView = view.findViewById(R.id.carplate);
        textView.setText(carplate);
        if (carplate.equals("")){
            textView.setVisibility(View.GONE);
        }else {
            textView.setVisibility(View.VISIBLE);
        }
        createQRCodeWithLogo(imageView,context, Utils.getAccount(context)+"&"+carplate, 300);
        alertDialog.show();
    }

    public static void createQRCodeWithLogo(ImageView imageView, Context context, String accountNo, int size) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
//                Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_car);
                return QRCodeEncoder.syncEncodeQRCode(accountNo, size, Color.parseColor("#0788FD"));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
//                    Toast.makeText(context, "生成二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}
