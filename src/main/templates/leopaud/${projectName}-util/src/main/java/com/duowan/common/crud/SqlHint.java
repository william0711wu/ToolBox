package com.duowan.common.crud;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标签注释，用于po辅助生成sql
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE, ElementType.FIELD })
public @interface SqlHint {
    boolean insert() default true;// 默认返回true false时不在插入语句中

    boolean update() default  true;// 默认返回true false时不在更新语句中

    String fieldName() default "";// 字段名称，默认空，使用自动影射
}
