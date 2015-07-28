package com.duowan.common.crud;

import com.duowan.leopard.core.datatype.OnlyDate;
import com.duowan.leopard.data.jdbc.builder.AbstractSqlBuilder;
import com.duowan.leopard.data.jdbc.builder.UpdateBuilder;
import com.google.common.base.CaseFormat;
import javassist.Modifier;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;

/**
 * sql builder 工具类
 * 
 * @author william
 */
public class SqlBuilderUtils {
    private static final Logger log = LoggerFactory.getLogger(SqlBuilderUtils.class);

    private static final SqlHintTester insertTester = new SqlHintTester() {
        @Override
        public boolean test(SqlHint hint) {
            return hint.insert();
        }
    };

    public static final SqlHintTester updateTester = new SqlHintTester() {
        @Override
        public boolean test(SqlHint hint) {
            return hint.update();
        }
    };

    /**
     * <pre>
     * 构建update语句。 使用SqlHint注解来生成相应插入字段。
     * 默认情况是生成的，如果在插入时需要忽略或使用指定的字段名称，在相应字段上添加注解
     * @SqlHint(update=false,fieldName='test')
     * </pre>
     * 
     * @param builder
     * @param data
     * @param <T>
     */
    public static <T> void buildUpdate(UpdateBuilder builder, T data) {
        build(builder, data, updateTester);
    }

    /**
     * <pre>
     * 构建insert语句。 使用SqlHint注解来生成相应插入字段。
     * 默认情况是生成的，如果在插入时需要忽略或使用指定的字段名称，在相应字段上添加注解
     * @SqlHint(insert=false,fieldName='test')
     * </pre>
     * 
     * @param builder
     * @param data
     */
    public static <T> void buildInsert(AbstractSqlBuilder builder, T data) {
        build(builder, data, insertTester);
    }

    private static <T> void build(AbstractSqlBuilder builder, T data,
            SqlHintTester tester) {
        try {
            Class<?> clz = data.getClass();
            Field[] fields = clz.getDeclaredFields();
            for (Field f : fields) {
                int m = f.getModifiers();
                if(Modifier.isFinal(m) || Modifier.isStatic(m) || Modifier.isEnum(m) || Modifier.isTransient(m)) {
                    continue;
                }
                
                SqlHint hint = f.getAnnotation(SqlHint.class);
                String tableFieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
                                                                  f.getName());
                //
                if (hint != null && !tester.test(hint)) {
                    continue;
                }

                if (hint != null && StringUtils.isNotEmpty(hint.fieldName())) {
                    tableFieldName = hint.fieldName();
                }
                
                
                //如果字段没有值，不生成相应的UPDATE SQL语句
                if(!isHasVal(data, f)){
                    continue;
                }

                Class<?> ftype = f.getType();
                f.setAccessible(true);
                
                if (ftype == String.class) {
                    builder.setString(tableFieldName, (String) f.get(data));
                } else if (ftype == Timestamp.class) {
                    builder.setTimestamp(tableFieldName,
                                         (Timestamp) f.get(data));
                } else if (ftype == OnlyDate.class) {
                    builder.setOnlyDate(tableFieldName,
                                        (OnlyDate) f.get(data));
                } else if (ftype == Boolean.class || ftype == boolean.class) {
                    builder.setBool(tableFieldName, (Boolean) f.get(data));
                } else if (ftype == Integer.class || ftype == int.class) {
                    builder.setInt(tableFieldName, (Integer) f.get(data));
                } else if (ftype == Date.class) {
                    builder.setDate(tableFieldName, (Date) f.get(data));
                } else if (ftype == Long.class || ftype == long.class) {
                    builder.setLong(tableFieldName, (Long) f.get(data));
                } else if (ftype == Float.class || ftype == float.class) {
                    builder.setFloat(tableFieldName, (Float) f.get(data));
                } else if (ftype == Double.class || ftype == double.class) {
                    builder.setDouble(tableFieldName, (Double) f.get(data));
                } else {
                    log.warn("未知类型：" + ftype + "  请进行处理");
                }
            }
        } catch (Exception e) {
            log.error("构建sql出错", e);
        }
    }

    /**
     * 检查字段值是否为空
     * @param obj
     * @param field
     * @return
     * @author mzm
     */
    private static boolean isHasVal(Object obj, Field field) {
        boolean result = false;
        field.setAccessible(true);
        try {
            Object value = field.get(obj);
            if (null != value) {
                result = true;
            }
        } catch (Exception e) {
            log.warn("获取字段值出错：" + field.getName() + "  请进行处理"
                    + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 标签测试接口，测试相应SqlHint注解值是否满足条件
     */
    static interface SqlHintTester {
        /**
         * 测试方法由子类实现
         * 
         * @param hint
         * @return
         */
        boolean test(SqlHint hint);
    }

}
