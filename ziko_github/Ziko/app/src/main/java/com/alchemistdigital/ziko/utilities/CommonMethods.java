package com.alchemistdigital.ziko.utilities;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by user on 4/29/2016.
 */
public class CommonMethods {
    private static String path;

    public static File getExternalSdCardPath() {
        File file = new File("/system/etc/vold.fstab");
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (fr != null) {
                br = new BufferedReader(fr);
                String s = br.readLine();
                while (s != null) {
                    if (s.startsWith("dev_mount")) {
                        String[] tokens = s.split("\\s");
                        path = tokens[2]; //mount_point
                        if (!Environment.getExternalStorageDirectory().getAbsolutePath().equals(path)) {
                            break;
                        }
                    }
                    s = br.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File extFilePath = new File(path);
        return extFilePath;
    }
}
