package com.upwork.cameraproject.util;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class FileUtils {
    public static void closeFileOutputStream(OutputStream fs) {
        if (fs != null) {
            try {
                fs.flush();
                fs.close();
            } catch (IOException e) {
                Logger.e("Can't close FileOutputStream");
                e.printStackTrace();
            }
        }
    }

    public static void closePrintWriter(PrintWriter fs) {
        if (fs != null) {
            fs.flush();
            fs.close();
        }
    }

    public static void closeInputStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                Logger.e("Can't close InputStream");
                e.printStackTrace();
            }
        }
    }

    public static void closeBufferedReader(BufferedReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                Logger.e("Can't close BufferedReader");
                e.printStackTrace();
            }
        }
    }

    public static boolean saveFilesFromAssets(AssetManager assetManager, String... paths) {
        for (String path : paths) {
            if (!copyAssetFile(path, path, assetManager)) {
                return false;
            }
        }
        return true;
    }

    public static boolean copyFilesFromAssetFolder(String fromAssetPath, String toPath, AssetManager assetManager) throws IOException {
        String[] files = assetManager.list(fromAssetPath);
        if (files.length > 0) {
            new File(toPath).mkdirs();
            for (String file : files) {
                if (!copyAssetFile(fromAssetPath + File.separator + file,
                        toPath + File.separator + file, assetManager)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean copyAssetFile(String fromAssetPath, String toPath, AssetManager assetManager) {
        File toPathFile = new File(toPath);
        if (!toPathFile.exists()) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(fromAssetPath);
                toPathFile.createNewFile();
                out = new FileOutputStream(toPath);
                copyFile(in, out);
                return true;
            } catch (Exception e) {
                Logger.e("Exception in copying file " + fromAssetPath + " to " + toPath);
                return false;
            } finally {
                closeInputStream(in);
                closeFileOutputStream(out);
            }
        }
        return true;
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }


    public static int deleteRecursive(String fileOrDirPath) {
        File fileOrDir = new File(fileOrDirPath);
        if (fileOrDir.exists()) {
            if (fileOrDir.isDirectory())
                for (String child : fileOrDir.list()) {
                    if (deleteRecursive(fileOrDirPath + File.separator + child) == -1) {
                        Logger.e("Can't delete some files or folders in root folder " + fileOrDirPath);
                        return -1; // if some files in root folder were not deleted
                    }
                }
            if (!fileOrDir.delete()) {
                Logger.e("Can't delete" + fileOrDirPath);
                return -1; // if folder or some files in this folder were not deleted
            }
            Logger.d("File/folder was deleted " + fileOrDirPath);
            return 0;  // if folder delete fine
        } else {
            Logger.e("Attempt to delete folder, folder not exist " + fileOrDirPath);
            return 1; // if folder not exist
        }
    }

    public static void makeBinaryExecutable(String binaryPath) {
        try {
            Process process = Runtime.getRuntime().exec("/system/bin/chmod 744 " + binaryPath);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static String runBinary(String binaryPath) throws IOException, InterruptedException {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = null;
        try {
            Process process = Runtime.getRuntime().exec(binaryPath);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }

            closeBufferedReader(reader);

            reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }

            process.waitFor();
        } finally {
            closeBufferedReader(reader);
        }
        return output.toString();
    }
}
