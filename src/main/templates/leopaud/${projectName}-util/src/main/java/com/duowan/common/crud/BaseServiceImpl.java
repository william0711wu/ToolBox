package com.duowan.common.crud;


import java.util.Date;
import java.util.List;

/**
 *
 */
public abstract class BaseServiceImpl<BEAN, KEYTYPE> implements BaseService<BEAN, KEYTYPE> {

	@Override
	public boolean update(BEAN bean) {
		return getBeanDao().update(bean);
	}

	@Override
	public boolean delete(KEYTYPE key, String op, Date date) {
		return getBeanDao().delete(key,op,date);
	}

	@Override
	public BEAN get(KEYTYPE key) {
		return getBeanDao().get(key);
	}

	@Override
	public boolean add(BEAN bean) {
		return getBeanDao().add(bean);
	}


	@Override
	public DataPage<BEAN> queryByPage(QueryCondition condition, int pageNo, int pageSize) {
		return getBeanDao().queryByPage(condition,pageNo,pageSize);
	}

	@Override
	public List<BEAN> query(QueryCondition condition) {
		return getBeanDao().query(condition);
	}

	@Override
	public Integer countOfQuery(QueryCondition condition) {
		return getBeanDao().countOfQuery(condition);
	}

	/**
	 * 由子类实现，返回相应的Bean的Dao对象
	 * @return
	 */
	public abstract BaseDao<BEAN, KEYTYPE> getBeanDao();
}
