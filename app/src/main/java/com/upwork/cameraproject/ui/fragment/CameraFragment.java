package com.upwork.cameraproject.ui.fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.upwork.cameraproject.R;
import com.upwork.cameraproject.util.CameraHelper;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class CameraFragment extends Fragment implements SurfaceHolder.Callback {
    public static final String CAMERA_FRAGMENT_TAG = "cam";
    private static final String CAMERA_ID_KEY = "camera_id";
    private static final int REQUEST_PERMISSION_ID = 1;

    private int currentCameraId = -1;

    private boolean hasPermissions = false;
    private Camera camera;
    private SurfaceHolder cameraSurfaceHolder;

    public CameraFragment() {
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            currentCameraId = arguments.getInt(CAMERA_ID_KEY);
        } else {
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }

        RelativeLayout rootCameraLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_camera, null);
        SurfaceView cameraSurfaceView = (SurfaceView) rootCameraLayout.findViewById(R.id.camera_surface);
        cameraSurfaceView.getHolder().addCallback(this);

        hasPermissions = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (!hasPermissions) {
            FragmentCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_ID);
        }

        return rootCameraLayout;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_ID:
                if (permissions.length == 1 && grantResults.length == 1
                        && permissions[0].equals(Manifest.permission.CAMERA)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasPermissions = true;
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasPermissions) {
            synchronized (this) {
                if (cameraSurfaceHolder != null) {
                    camera = Camera.open(currentCameraId);
                    initCamera();
                    try {
                        camera.setPreviewDisplay(cameraSurfaceHolder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    camera.startPreview();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getArguments().putInt(CAMERA_ID_KEY, currentCameraId);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        synchronized (this) {
            cameraSurfaceHolder = holder;
            if (camera != null) {
                try {
                    camera.setPreviewDisplay(holder);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopCamera();
    }

    private void initCamera() {
        int displayRotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        switch (displayRotation) {
            case Surface.ROTATION_0:
                displayRotation = 0;
                break;
            case Surface.ROTATION_90:
                displayRotation = 90;
                break;
            case Surface.ROTATION_180:
                displayRotation = 180;
                break;
            case Surface.ROTATION_270:
                displayRotation = 270;
                break;
        }

        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(currentCameraId, info);
        int orientation;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            orientation = (info.orientation + displayRotation) % 360;
            orientation = (360 - orientation) % 360; // compensate the mirror
        } else { // back-facing
            orientation = (info.orientation - displayRotation + 360) % 360;
        }
        camera.setDisplayOrientation(orientation);

        boolean flipWH = info.orientation == 90 || info.orientation == 270;
        Camera.Size previewSize = CameraHelper.determineBestPreviewResolution(camera, flipWH);
        Camera.Size pictureSize = CameraHelper.determineBestPictureResolution(camera, flipWH);

        Camera.Parameters cameraParams = camera.getParameters();
        cameraParams.setPreviewSize(previewSize.width, previewSize.height);
        cameraParams.setPictureSize(pictureSize.width, pictureSize.height);

        if (cameraParams.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            cameraParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }

        camera.setParameters(cameraParams);
    }

    private synchronized void stopCamera() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}
