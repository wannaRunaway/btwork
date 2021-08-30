package com.botann.charing.pad.views;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import com.botann.charging.pad.R;


/**
 * 创建自定义的dialog，主要学习其实现原理
 * Created by chengguo on 2016/3/22.
 */
public class SelfDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Button yes;//确定按钮
    private Button no;//取消按钮
    private TextView titleTv;//消息标题文本
    private TextView messageTv;//消息提示文本
    private EditText startDateTv;
    private EditText endDateTv;
    private EditText inputTv;
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    private String startDateStr;
    private String endDateStr;
    private String inputStr;
    public static String kDefaultFlag = "selfDialog";

    private Button btnScan;



    //确定文本和取消文本的显示内容
    private String yesStr, noStr;

    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器
    private onStartDateOnclickListener startDateOnclickListener;
    private onEndDateOnclickListener endDateOnclickListener;
    private onScanOnClickListener onScanOnClickListener;

    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param str
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        if (str != null) {
            noStr = str;
        }
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
        if (str != null) {
            yesStr = str;
        }
        this.yesOnclickListener = onYesOnclickListener;
    }

    public void setStartDateOnclickListener(onStartDateOnclickListener startDateOnclickListener) {
        this.startDateOnclickListener = startDateOnclickListener;
    }

    public void setEndDateOnclickListener(onEndDateOnclickListener endDateOnclickListener) {
        this.endDateOnclickListener = endDateOnclickListener;
    }

    public void setOnScanOnClickListener(onScanOnClickListener onScanOnClickListener) {
        this.onScanOnClickListener = onScanOnClickListener;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.MyDialog);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.free_exercise_sure_dialog_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        //初始化界面控件
        initView(dialog);
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
        return dialog;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.free_exercise_sure_dialog_layout);
//        //按空白处不能取消动画
//        setCanceledOnTouchOutside(false);
//
//        //初始化界面控件
//        initView();
//        //初始化界面数据
//        initData();
//        //初始化界面控件的事件
//        initEvent();
//
//    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
        startDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startDateOnclickListener != null) {
                    startDateOnclickListener.onStartDateClick();
                }
            }
        });
        endDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endDateOnclickListener != null) {
                    endDateOnclickListener.onEndDateClick();
                }
            }
        });
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onScanOnClickListener != null) {
                    onScanOnClickListener.onScanClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (titleStr != null) {
            titleTv.setText(titleStr);
        }
        if (messageStr != null) {
            messageTv.setText(messageStr);
        }
        //如果设置按钮的文字
        if (yesStr != null) {
            yes.setText(yesStr);
        }
        if (noStr != null) {
            no.setText(noStr);
        }
        if(startDateStr != null) {
            startDateTv.setText(startDateStr);
        }
        if(endDateStr != null) {
            endDateTv.setText(endDateStr);
        }
        if(inputStr != null) {
            inputTv.setText(inputStr);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView(Dialog dialog) {
        yes = (Button) dialog.findViewById(R.id.yes);
        no = (Button) dialog.findViewById(R.id.no);
        titleTv = (TextView) dialog.findViewById(R.id.title);
        messageTv = (TextView) dialog.findViewById(R.id.message);
        startDateTv = (EditText) dialog.findViewById(R.id.etStartDate);
        endDateTv = (EditText) dialog.findViewById(R.id.etEndDate);
        inputTv = (EditText) dialog.findViewById(R.id.etInput);
        btnScan = (Button) dialog.findViewById(R.id.btn_scan);
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
    }

    public String getStartDateStr() {
        return startDateTv.getText().toString();
    }

    public void setStartDateStr(String dateStr) {
        startDateStr = dateStr;
    }

    public String getEndDateStr() {
        return endDateTv.getText().toString();
    }

    public void setEndDateStr(String dateStr) {
        endDateStr = dateStr;
    }

    public String getInputStr() {
        return inputTv.getText().toString();
    }

    public void setInputStr(String noStr) {
        inputTv.setText(noStr);
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        void onYesClick();
    }

    public interface onNoOnclickListener {
        void onNoClick();
    }

    public interface onStartDateOnclickListener {
        void onStartDateClick();
    }

    public interface onEndDateOnclickListener {
        void onEndDateClick();
    }

    public interface onScanOnClickListener {
        void onScanClick();
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }

    public void showDatePicker(int isStart, Activity context) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd;
        switch (isStart) {
            case 0:
                dpd = DatePickerDialog.newInstance(
                        SelfDialog.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(context.getFragmentManager(), "startDate");
                dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String date = year + "-" + formatMonth(monthOfYear) + "-" + formatDay(dayOfMonth);
                        startDateTv.setText(date);
                    }
                });
                break;
            case 1:
                dpd = DatePickerDialog.newInstance(
                        SelfDialog.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(context.getFragmentManager(), "endDate");
                dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String date = year + "-" + formatMonth(monthOfYear) + "-" + formatDay(dayOfMonth);
                        endDateTv.setText(date);
                    }
                });
                break;
            default:
                break;
        }
    }

    private String formatMonth(int monthOfYear) {
        if(monthOfYear < 9) {
            return "0" + (++monthOfYear);
        } else {
            return Integer.toString(++monthOfYear);
        }
    }

    private String formatDay(int dayOfMonth) {
        if(dayOfMonth < 9) {
            return "0" + (dayOfMonth);
        } else {
            return Integer.toString(dayOfMonth);
        }
    }

}
