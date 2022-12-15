package com.ckb.utils;

import java.util.*;

public class RandomUtils {
    /**
     * @return 获取随机数
     */
    public static Integer getRandom() {
        Random random = new Random();
        return random.nextInt(99999999);
    }

    public static String createRandomStr(int length){
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(3);
            long result = 0;
            switch (number) {
                case 0:
                    result = Math.round(Math.random()*25+65);
                    stringBuffer.append(String.valueOf((char) result));
                    break;
                case 1:
                    result = Math.round(Math.random()*25+97);
                    stringBuffer.append(String.valueOf((char)result));
                    break;
                case 2:
                    stringBuffer.append(String.valueOf(new Random().nextInt(10)));
                    break;
            }
        }
        return stringBuffer.toString();
    }

    public static String toHexString(String s)
    {
        StringBuilder str= new StringBuilder();
        for (int i=0;i<s.length();i++)
        {
            int ch = s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str.append(s4);
        }
        return str.toString();
    }
}













