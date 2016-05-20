package io.github.the28awg.buckler.system;

import android.content.res.AssetManager;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by the28awg on 23.04.16.
 */
public class AssetUtils {

    public static boolean exists(String fileName, String path, AssetManager assetManager) throws IOException {
        for (String currentFileName : assetManager.list(path)) {
            if (currentFileName.equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    public static String[] list(String path, AssetManager assetManager) throws IOException {
        String[] files = assetManager.list(path);
        Arrays.sort(files);
        return files;
    }
}
