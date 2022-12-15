package com.ckb.utils;

import java.lang.reflect.Field;
import java.util.*;

public class ObjectUtils {

    /**
     * 判断对象是否为空（根据一般常用类型区分）
     * @param object
     * @return: boolean
     */
    public static boolean isEmpty(Object object){

        //判断对象是否为null
        if (null == object){
            return true;
        }

        //判断对象是否为String类型
        if (object instanceof String){
            if (object.toString().length() == 0){
                return true;
            }
        }

        //判断对象是否为Map
        if (object instanceof Map){
            Map map = (Map)object;
            if (map.size() == 0){
                return true;
            }
        }

        //判断对象是否为List
        if (object instanceof List){
            List list = (List)object;
            if (list.size() == 0){
                return true;
            }
        }

        //普通的类对象
        Field[] fields = object.getClass().getDeclaredFields();

        //先假设全部属性都是空的，所以只要出现一个属性不为空的就不需要在循环判断
        boolean flag = true;

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (!ObjectUtils.isEmpty(field.get(object))) {
                    flag = false;
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return flag;
    }

    /**
     * 判断obejct对象中除了names里面的字段，其他字段都为null（已知对象类型）
     * @param object
     * @param names
     * @return
     */
    public static boolean isEmpty(Object object,String... names){

        Field[] fields = object.getClass().getDeclaredFields();
        //用于判断所有属性是否为空,如果参数为空则不查询
        boolean flag = true;

        for (Field field : fields) {
            //不检查 直接取值
            field.setAccessible(true);
            try {
                String fieldName = field.getName();
                List<String> nameList = new ArrayList<>();
                if (null != names && names.length != 0){
                    nameList = Arrays.asList(names);
                }

                if (!nameList.contains(fieldName) && !Objects.isNull(field.get(object))) {
                    //不为空
                    flag = false;
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return flag;
    }

    /**
     * 判断object对象中除了names里面的字段，其他字段都为null或者是""（已知对象类型）
     * @param object
     * @param names
     * @return
     */
    public static boolean isBlank(Object object,String... names){

        Field[] fields = object.getClass().getDeclaredFields();
        //用于判断所有属性是否为空,如果参数为空则不查询
        boolean flag = true;

        for (Field field : fields) {
            //不检查 直接取值
            field.setAccessible(true);
            try {
                String fieldName = field.getName();
                List<String> nameList = new ArrayList<>();
                if (null != names && names.length != 0){
                    nameList = Arrays.asList(names);
                }
                Object value = field.get(object);
                if (!nameList.contains(fieldName) && !isEmpty(value)) {
                    //不为空
                    flag = false;
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return flag;
    }


}
