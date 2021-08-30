package com.btkj.millingmachine.util;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import com.btkj.millingmachine.R;
/**
 * Created by Administrator on 2018/1/19.
 */
public class SoundPlayUtils {
    // SoundPool对象
    public static SoundPool mSoundPlayer = new SoundPool(10,
            AudioManager.STREAM_SYSTEM, 5);
    public static SoundPlayUtils soundPlayUtils;
    // 上下文
    static Context mContext;

    /**
     * 初始化
     *
     * @param context
     */
    public static SoundPlayUtils init(Context context) {
        if (soundPlayUtils == null) {
            soundPlayUtils = new SoundPlayUtils();
        }
        // 初始化声音
        mContext = context;
        mSoundPlayer.load(mContext, R.raw.goumishu, 1);// 请选择购米数量
        mSoundPlayer.load(mContext, R.raw.zhifufang, 1);// 支付方式
        mSoundPlayer.load(mContext, R.raw.zhengzainianmi, 1);// 正在碾米，请稍候
        mSoundPlayer.load(mContext, R.raw.chongzhijin, 1);// 充值金额
        mSoundPlayer.load(mContext, R.raw.yuechaxun, 1);// 余额查询
        mSoundPlayer.load(mContext, R.raw.nianmiwancheng, 1);// 碾米完成
        mSoundPlayer.load(mContext, R.raw.chongzhijine, 1); //输出充值金额
        mSoundPlayer.load(mContext, R.raw.chongzhixuanze, 1); //充值方式选择
        return soundPlayUtils;
    }

    /**
     * 播放声音
     *
     * @param soundID
     */
    public static void play(int soundID) {
        mSoundPlayer.play(soundID, 1, 1, 0, 0, 1);
    }
}
