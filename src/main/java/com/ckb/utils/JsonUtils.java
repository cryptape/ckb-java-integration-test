package com.ckb.utils;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JsonUtils {
    public static <T> T jsonFile2Object(String finalPath, Class<T> targetClass) {
        String jsonString;
        File file = new File(finalPath);
        try{
            FileInputStream inputStream = new FileInputStream(file);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonString = new String(buffer, StandardCharsets.UTF_8);
            T object = JSON.parseObject(jsonString, targetClass);
            return object;
        }catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IO exception");
        }
    }
}
