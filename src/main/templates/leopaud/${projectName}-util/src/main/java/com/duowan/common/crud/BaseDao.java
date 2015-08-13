
package com.duowan.common.crud;

import com.duowan.leopard.data.api.IUpdate;

import java.util.List;

/**
 *
 */
public interface BaseDao<BEAN, KEYTYPE> extends IUpdate<BEAN, KEYTYPE> {

	/**
	 * 默认分页查询方法
	 * @param condition 查询条件
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	DataPage<BEAN> queryByPage(QueryCondition condition, int pageNo, int pageSize);

	/**
	 * 根据条件查询相应记录
	 * @param condition
	 * @return
	 */
	public List<BEAN> query(QueryCondition condition);

	/**
	 * 查询符合条件的记录数
	 * @param condition
	 * @return
	 */
	public Integer countOfQuery(QueryCondition condition);

	/**
	 * 插入新对象，并返回id
	 * @param bean
	 * @return
	 */
	long insertForId(BEAN bean);
}
