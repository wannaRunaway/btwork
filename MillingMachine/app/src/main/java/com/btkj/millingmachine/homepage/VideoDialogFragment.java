package com.btkj.millingmachine.homepage;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.DialogFragmentVideoBinding;
import com.btkj.millingmachine.util.Utils;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * created by xuedi on 2019/5/28
 */
public class VideoDialogFragment extends Fragment implements View.OnClickListener {
    private DialogFragmentVideoBinding binding;
    private List<Uri> list = new ArrayList<>();
    private int position = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_video, container, false);
        binding = DialogFragmentVideoBinding.bind(view);
        binding.re.setOnClickListener(this);
//        Utils.Glide(getContext(), R.drawable.dianjigoumi,binding.imageview);
        Glide.with(getContext()).load(R.drawable.dianjigoumi).into(binding.imageview);
        List<String> nameList = getFilesAllName(getActivity().getExternalFilesDir("me").getPath() + "/video");
        for (int i = 0; i < nameList.size(); i++) {
            list.add(Uri.parse(nameList.get(i)));
        }
        if (nameList.size() > 0) {
            binding.videoview.setVideoURI(Uri.parse(nameList.get(0)));
        }
//        Uri uri1 = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.video2);
//        Uri uri2 = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.video1);
//        list.add(uri1);
//        list.add(uri2);
//        binding.videoview.setVideoURI(uri1);
        load();
        return view;
    }

    public List<String> getFilesAllName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            Log.e("error", "空目录");
            return null;
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            s.add(files[i].getAbsolutePath());
        }
        return s;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            load();
        } else {
            binding.videoview.pause();
        }
    }

    private void load() {
        binding.videoview.start();
        binding.videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                position = position + 1;
                if (position > list.size() - 1) {
                    position = 0;
                }
                binding.videoview.setVideoURI(list.get(position));
                binding.videoview.start();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.re:
//                ((MainActivity) getActivity()).cuntRestart();
                ((MainActivity) getActivity()).hideVideoFragment();
                break;
            default:
                break;
        }
    }
}
