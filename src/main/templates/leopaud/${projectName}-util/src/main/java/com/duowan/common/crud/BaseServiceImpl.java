package com.duowan.common.crud;


import com.duowan.leopard.data.Page;

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
	public Data<BEAN> queryByPage(QueryCondition condition, int pageNo, int pageSize) {
		Page<BEAN> beanPage = getBeanDao().queryByPage(condition,pageNo,pageSize);
		com.duowan.common.crud.Page page = new com.duowan.common.crud.Page();
		Integer count = beanPage.getCount();
		page.setCurrPage(pageNo);
		Integer totalPage = (int) Math.ceil((double) count / (double) pageSize);
		page.setTotalPage(totalPage);
		page.setTotalRecord(count);

		Data<BEAN> data = new Data<>();
		data.setResult(beanPage.getData());
		data.setPage(page);

		return data;
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
