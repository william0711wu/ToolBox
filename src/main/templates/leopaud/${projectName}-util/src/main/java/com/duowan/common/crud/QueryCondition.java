package com.duowan.common.crud;

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *  简单组合查询条件动态构建类。<p>
 *      字段file使用相应mode中的字段名，例如userName；工具类会自行将userName转换成user_name
 *
 */
public class QueryCondition {

    private List<CondInfo> condition = Lists.newArrayList();
    private List<OrderInfo> orderInfos = Lists.newArrayList();


    /**
     * sql 相待条件  例如：  name = '?'
     * @param field
     * @param value
     * @return
     */
    public QueryCondition eq(String field,Object value){
        condition.add(new CondInfo("=", field, value));
        return this;
    }

    /**
     * sql 大于条件  例如：  name > '?'
     * @param field
     * @param value
     * @return
     */
    public QueryCondition gt(String field,Object value){
        condition.add(new CondInfo(">", field, value));
        return this;
    }

    /**
     * sql 大于等于条件  例如：  name >= '?'
     * @param field
     * @param value
     * @return
     */
    public QueryCondition ge(String field,Object value){
        condition.add(new CondInfo(">=", field, value));
        return this;
    }

    /**
     * sql 小于条件  例如：  name < '?'
     * @param field
     * @param value
     * @return
     */
    public QueryCondition lt(String field,Object value){
        condition.add(new CondInfo("<", field, value));
        return this;
    }

    /**
     * sql 小于等于条件  例如：  name <= '?'
     * @param field
     * @param value
     * @return
     */
    public QueryCondition le(String field,Object value){
        condition.add(new CondInfo("<=", field, value));
        return this;
    }


    /**
     * sql like条件  例如：  name like '%?%'
     * @param field
     * @param value
     * @return
     */
    public QueryCondition like(String field,Object value){
        condition.add(new CondInfo("like", field, "%" + value + "%"));
        return this;
    }

    /**
     * sql llike条件  例如：  name like '%?'
     * @param field
     * @param value
     * @return
     */
    public QueryCondition llike(String field,Object value){
        condition.add(new CondInfo("like", field, "%" + value));
        return this;
    }

    /**
     * sql rlike条件  例如：  name like '?%'
     * @param field
     * @param value
     * @return
     */
    public QueryCondition rlike(String field,Object value){
        condition.add(new CondInfo("like", field, value + "%"));
        return this;
    }

    public QueryCondition in(String field,Collection<?> value){
        condition.add(new CondInfo("in", field, value));
        return this;
    }


    /**
     * 升序排序字段
     * @param field
     * @param fields
     * @return
     */
    public QueryCondition ascOrder(String field,String ... fields){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setAsc(true);
        orderInfo.setFields(Lists.asList(field,fields));

        orderInfos.add(orderInfo);

        return this;
    }

    /**
     * 降序排序字段
     * @param field
     * @param fields
     * @return
     */
    public QueryCondition descOrder(String field,String ... fields){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setAsc(false);
        orderInfo.setFields(Lists.asList(field,fields));

        orderInfos.add(orderInfo);

        return this;
    }

    public List<OrderInfo> getOrderInfos() {
        return orderInfos;
    }

    public List<CondInfo>  getCondition(){
        return condition;
    }

    /**
     * 查询条件封闭类
     */
    public static class  CondInfo{
        private String operator;//操作
        private String field;//字段
        private  Object value;//值

        public CondInfo(String operator, String field, Object value) {
            this.operator = operator;
            this.field = field;
            this.value = value;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        /**
         * 相应的sql 语句
         * @return
         */
        public String getSqlScript(){
            if("in".equals(operator)){
                Collection<?> collection = (Collection<?>) value;
                List<String> questionMarks = Lists.newArrayList();
                for(Object o : collection){
                    questionMarks.add("?");
                }
                return String.format(" and `%s` %s (%s)", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,field),operator,Joiner.on(",").join(questionMarks));
            }else {
                return String.format(" and `%s` %s ? ", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,field),operator);
            }
        }
    }

    public static class OrderInfo{
        private List<String> fields = new ArrayList<>();
        private boolean isAsc;

        public List<String> getFields() {
            return fields;
        }

        public void setFields(List<String> fields) {
            for(String field : fields){
                this.fields.add(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,field));
            }
        }

        public boolean isAsc() {
            return isAsc;
        }

        public void setAsc(boolean isAsc) {
            this.isAsc = isAsc;
        }

        /**
         * 相应的sql 语句
         * @return
         */
        public String getSqlScript(){
            return String.format(Joiner.on(" , ").join(fields)+(isAsc?" asc ":" desc "));
        }
    }
}


