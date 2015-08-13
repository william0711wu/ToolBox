package com.duowan.common.crud;

import com.duowan.leopard.data.Page;
import com.duowan.leopard.data.jdbc.Jdbc;
import com.duowan.leopard.data.jdbc.StatementParameter;
import com.duowan.leopard.data.jdbc.builder.InsertBuilder;
import com.duowan.leopard.data.jdbc.builder.UpdateBuilder;
import com.google.common.base.CaseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class BaseDaoMysqlImpl<BEAN, KEYTYPE> implements BaseDao<BEAN, KEYTYPE> {
    private static final Logger log = LoggerFactory.getLogger(BaseDaoMysqlImpl.class);

    //查询主键SQL
    public static final String PK_SQL = "SELECT COLUMN_NAME  FROM information_schema.KEY_COLUMN_USAGE where CONSTRAINT_NAME = 'PRIMARY' and TABLE_NAME = '%s' and TABLE_SCHEMA = '%s'";
    //根据主键返回记录
    public static final String GET_BY_ID = "SELECT * FROM %s WHERE %s = ? ";
    //根据主键删除记录
    public static final String DEL_BY_ID = "DELETE FROM %s WHERE %s = ? ";

    @Autowired
    protected Jdbc jdbc;

    private MysqlHerper mysqlHerper;

    public Class<BEAN> entityClass;

    private String tableName;//表名
    private String pkField;//主键字段
    private Field pkFieldInfo;//主键字段对应的Field信息


    @SuppressWarnings("unchecked")
    @PostConstruct
    private void init() {
        this.mysqlHerper = new MysqlHerper(jdbc);
        //确定bean class
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class<BEAN>) params[0];
        //表名
        tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,getBeanClass().getSimpleName());
        //数据库主键名
        pkField = jdbc.queryForString(String.format(PK_SQL,tableName,mysqlHerper.getSchemaName()));

        pkFieldInfo = getFieldInfo(entityClass, CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,pkField));
    }

    @Override
    public DataPage<BEAN> queryByPage(QueryCondition condition, int pageNo, int pageSize) {
        StatementParameter param = new StatementParameter();
        StringBuffer sb = new StringBuffer("select * from " + getTableName() + " where 1 =1 ");
        parseCondition(condition, param, sb);
        parseOrder(condition,sb);
        if(log.isDebugEnabled()){
            log.debug("sql==>"+sb);
        }
        int start = (pageNo - 1) * pageSize;
        Page<BEAN> beanPage = jdbc.queryForPage(sb.toString(), entityClass, param, start, pageSize);

        DataPage dataPage = new DataPage();
        dataPage.setPageNo(pageNo);
        dataPage.setPageSize(pageSize);
        dataPage.setTotalRecord(beanPage.getCount());
        dataPage.setResults(beanPage.getData());

        return dataPage;
    }

    @Override
    public List<BEAN> query(QueryCondition condition) {
        StatementParameter param = new StatementParameter();
        StringBuffer sb = new StringBuffer("select * from " + getTableName() + " where 1 =1 ");
        parseCondition(condition, param, sb);
        parseOrder(condition,sb);
        return jdbc.queryForList(sb.toString(), entityClass, param);
    }

    @Override
    public Integer countOfQuery(QueryCondition condition) {
        StatementParameter param = new StatementParameter();
        StringBuffer sb = new StringBuffer("select count(*) from " + getTableName()
                + " where 1 =1 ");
        parseCondition(condition, param, sb);
        return  jdbc.queryForInt(sb.toString(), param);
    }


    @Override
    public boolean update(BEAN bean) {
        UpdateBuilder updateBuilder = new UpdateBuilder(getTableName());
        SqlBuilderUtils.buildUpdate(updateBuilder, bean);
        Class<?> pkFieldClass = pkFieldInfo.getType();
        Object value = getFieldValue(getPkFieldInfo(),bean);
        if(pkFieldClass == String.class){
            updateBuilder.where.setString(getPkField(), (String) value);
        }else if(pkFieldClass == Long.class || pkFieldClass == long.class){
            updateBuilder.where.setLong(getPkField(),
                    (Long) value);
        }else if(pkFieldClass == Integer.class|| pkFieldClass == int.class){
            updateBuilder.where.setInt(getPkField(),
                    (Integer) value);
        }else{
            throw new UnsupportedOperationException("未知主键类型");
        }
        return jdbc.updateForBoolean(updateBuilder);
    }

    @Override
    public boolean delete(KEYTYPE key, String opusername, Date lmodify) {
        log.info(String.format("用户%s从表%s删除记录%s",opusername,getTableName(),key));
        return jdbc.updateForBoolean(String.format(DEL_BY_ID, getTableName(), getPkField()), key);
    }

    @Override
    public BEAN get(KEYTYPE key) {
        return jdbc.query(String.format(GET_BY_ID, getTableName(), getPkField()), entityClass, key);
    }

    @Override
    public boolean add(BEAN bean) {
        InsertBuilder builder = new InsertBuilder(getTableName());
        SqlBuilderUtils.buildInsert(builder, bean);
        return jdbc.insertForBoolean(builder);
    }

    @Override
    public long insertForId(BEAN bean) {
        InsertBuilder builder = new InsertBuilder(getTableName());
        SqlBuilderUtils.buildInsert(builder, bean);
        return jdbc.insertForLastId(builder.getSql(),builder.getParam());
    }

    /**
     * 根据model名称，改为小写下划线分隔。子类有特殊情况，请覆盖。
     * @return
     */
    protected String getTableName() {
        return tableName;
    }

    /**
     * 数据库表主键名，如有需要，子类可覆盖。
     * @return
     */
    protected  String getPkField(){
        return pkField;
    }

    /**
     * 返回主键对应java class
     * @return
     */
    protected Field getPkFieldInfo(){
        return pkFieldInfo;
    }

    protected Class<BEAN> getBeanClass() {
        return entityClass;
    }


    private Field getFieldInfo(Class<BEAN> entityClass,String field){
        try{
            return entityClass.getDeclaredField(field);
        }catch(Exception e){
            log.warn("出现异常：",e);
        }
        return null;
    }

    private Object getFieldValue(Field field,Object bean){
        try{
            field.setAccessible(true);
            return field.get(bean);
        }catch(Exception e){
            log.warn("出现异常：",e);
        }
        return null;
    }


    public Class<?> getFieldType(Class<BEAN> entityClass,String field){
        try{
            return getBeanClass().getDeclaredField(field).getType();
        }catch(Exception e){
            log.warn("出现异常：",e);
        }
        return null;
    }

    protected void parseCondition(QueryCondition condition, StatementParameter param, StringBuffer sql) {
        List<QueryCondition.CondInfo>  condInfos = condition.getCondition();
        for(QueryCondition.CondInfo condInfo : condInfos){
            if("in".equals(condInfo.getOperator())){
                sql.append(condInfo.getSqlScript());
                for(Object o : (Collection<?>) condInfo.getValue()){
                    param.setObject(getFieldType(entityClass,condInfo.getField()),o);
                }
            }else {
                sql.append(condInfo.getSqlScript());
                param.setObject(getFieldType(entityClass,condInfo.getField()),condInfo.getValue());
            }
        }
    }

    protected void parseOrder(QueryCondition condition,  StringBuffer sql){
        List<QueryCondition.OrderInfo> orderInfos = condition.getOrderInfos();
        if(orderInfos.size()<1) return;
        sql.append(" order by ");
        int i = 1;
        for(QueryCondition.OrderInfo orderInfo : orderInfos){
            if(i>1){
                sql.append(" ,");
            }
            sql.append(orderInfo.getSqlScript());
            i++;
        }
    }

}
