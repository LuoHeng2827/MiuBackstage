package com.luoheng.miu;

import java.util.UUID;

public class Util {
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    public static String getRealFilePath(String path) {
        return path.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
    }

    public static String getHttpURLPath(String path) {
        return path.replace("\\", "/");
    }
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
