package com.upwork.cameraproject.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.upwork.cameraproject.R;
import com.upwork.cameraproject.ui.fragment.CameraFragment;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        CameraFragment cameraFragment = new CameraFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, cameraFragment, CameraFragment.CAMERA_FRAGMENT_TAG).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
