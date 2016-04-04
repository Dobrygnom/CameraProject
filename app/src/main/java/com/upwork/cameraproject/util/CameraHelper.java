package com.upwork.cameraproject.util;

import android.hardware.Camera;
import android.util.Pair;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.upwork.cameraproject.app.App;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("deprecation")
public class CameraHelper {
    private static final int MIN_PREVIEW_PIXELS = 320 * 480;
    private static final double MAX_ASPECT_DISTORTION = 0.15;

    public static Camera.Size determineBestPreviewResolution(Camera camera, boolean flipWH) {
        Camera.Parameters cameraParameters = camera.getParameters();
        Camera.Size defaultPreviewResolution = cameraParameters.getPreviewSize();

        List<Camera.Size> rawSupportedSizes = cameraParameters.getSupportedPreviewSizes();
        if (rawSupportedSizes == null) {
            return defaultPreviewResolution;
        }

        List<Camera.Size> supportedPreviewResolutions = new ArrayList<>(rawSupportedSizes);
        Collections.sort(supportedPreviewResolutions, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });

        Pair<Integer, Integer> displaySize = App.getDisplayMetrics();
        double screenAspectRatio = displaySize.first / (double) displaySize.second;

        Iterator<Camera.Size> it = supportedPreviewResolutions.iterator();
        while (it.hasNext()) {
            Camera.Size supportedPreviewResolution = it.next();
            if (supportedPreviewResolution.height * supportedPreviewResolution.width < MIN_PREVIEW_PIXELS) {
                it.remove();
                continue;
            }

            int width = flipWH ? supportedPreviewResolution.height : supportedPreviewResolution.width;
            int height = flipWH ? supportedPreviewResolution.width : supportedPreviewResolution.height;

            if (Math.abs(width / (double) height - screenAspectRatio) > MAX_ASPECT_DISTORTION) {
                it.remove();
                continue;
            }

            if (width == displaySize.first && height == displaySize.second) {
                return supportedPreviewResolution;
            }
        }

        if (!supportedPreviewResolutions.isEmpty()) {
            return supportedPreviewResolutions.get(0);
        }

        return defaultPreviewResolution;
    }

    public static Camera.Size determineBestPictureResolution(Camera camera, boolean flipWH) {
        Camera.Parameters cameraParameters = camera.getParameters();
        Camera.Size defaultPictureResolution = cameraParameters.getPictureSize();

        List<Camera.Size> rawSupportedSizes = cameraParameters.getSupportedPictureSizes();
        if (rawSupportedSizes == null) {
            return defaultPictureResolution;
        }

        List<Camera.Size> supportedPictureResolutions = new ArrayList<>(rawSupportedSizes);
        Collections.sort(supportedPictureResolutions, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });

        Pair<Integer, Integer> displaySize = App.getDisplayMetrics();
        double screenAspectRatio = displaySize.first / (double) displaySize.second;

        Iterator<Camera.Size> it = supportedPictureResolutions.iterator();
        while (it.hasNext()) {
            Camera.Size supportedPictureResolution = it.next();
            int width = supportedPictureResolution.width;
            int height = supportedPictureResolution.height;

            if (width * height < MIN_PREVIEW_PIXELS) {
                it.remove();
                continue;
            }

            boolean isCandidatePortrait = width > height;
            int maybeFlippedWidth = isCandidatePortrait ? height : width;
            int maybeFlippedHeight = isCandidatePortrait ? width : height;
            double aspectRatio = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
            double distortion = Math.abs(aspectRatio - screenAspectRatio);
            if (distortion > MAX_ASPECT_DISTORTION) {
                it.remove();
                continue;
            }

            if (maybeFlippedWidth == displaySize.first && maybeFlippedHeight == displaySize.second) {
                return supportedPictureResolution;
            }
        }

        if (!supportedPictureResolutions.isEmpty()) {
            return supportedPictureResolutions.get(0);
        }

        return defaultPictureResolution;
    }
}
