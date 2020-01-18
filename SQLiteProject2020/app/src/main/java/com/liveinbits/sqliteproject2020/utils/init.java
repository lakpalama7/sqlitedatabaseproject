package com.liveinbits.sqliteproject2020.utils;

import android.Manifest;

public class init {

    public init(){}

    public static final String[] camera_permission={
            Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final String[] phone_permission={Manifest.permission.CALL_PHONE};

    public static final int CAMERA_REQUESTCODE=110;
    public static final int IMAGE_FROMMEMORY=120;
}
