package com.duol.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Duolaimon
 * 18-8-1 下午7:36
 */
public class BaseVOUtil {
    private static final Logger logger = LoggerFactory.getLogger(BaseVOUtil.class);

    /**
     * 将目标对象的字段值设置为和源对象同名字段值
     *
     * @param source      源对象
     * @param targetClass 目标类
     * @param <S>   源对象类型
     * @param <T>   目标对象类型
     * @return      目标对象
     */
    public static <S, T> T parse(S source, Class<T> targetClass){
        //获取target的全部属性值
        Field[] fields = targetClass.getDeclaredFields();//获取所有域名
        //并创建一个T对象
        T target = null;
        try {
            target = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("获取对象失败",e);
        }

        //获取source的全部属性名
        Field[] fieldsTb = source.getClass().getDeclaredFields();
        List<String> fieldNameList = new ArrayList<>();
        for (Field field : fieldsTb) {
            fieldNameList.add(field.getName());
        }


        for (Field field : fields) {
            //获取target类里面的写方法
            PropertyDescriptor targetPropDesc;
            try {
                targetPropDesc = new PropertyDescriptor(field.getName(), targetClass);
                Method methodWrite = targetPropDesc.getWriteMethod();
                //如果source里面存在target里面的字段值，就会自动copy
                if (fieldNameList.contains(field.getName())) {
                    //获取tb里面的读方法
                    PropertyDescriptor sourcePropDesc = new PropertyDescriptor(field.getName(), source.getClass());
                    Method methodRead = sourcePropDesc.getReadMethod();
                    methodWrite.invoke(target, methodRead.invoke(source));
                }
            } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                logger.error("获取方法并调用失败",e);
            }

        }
        //返回一个target
        return target;
    }

}
