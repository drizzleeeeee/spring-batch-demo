package com.drizzle.job.scheduler.springbatch.jobs.demo.step;

import org.springframework.batch.item.database.AbstractPagingItemReader;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author zhiqiang.wang@marketin.cn
 * @create 2019-06-17 4:53 PM
 * @since MA - 2.6
 */
public abstract class CommonReader<T> extends AbstractPagingItemReader<T> {

	@Override
	protected void doJumpToPage(int itemIndex) {

	}

	@Override
	protected void doReadPage() {

		try {
			// 清除数据，API请求可能失败
			results = Collections.emptyList();

			// 获取数据
			if (isSupportPaging()) {

				results = paging(getOffSet(), getPageSize());

			} else {

				// 不支持分页查询，只调用一次查询
				if (getPage() == 0) {
					results = paging(getOffSet(), getPageSize());
				} else {
					results = Collections.emptyList();
				}
			}
		} catch (Exception e) {

			logger.error("Failed to read by AbstractPagingItemReader，by e", e);

			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}

			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取offset
	 *
	 * @return
	 */
	protected Integer getOffSet() {
		return getPage() * getPageSize();
	}

	/**
	 * 是否支持分页查询，默认支持，可以在具体子类中重写改方法
	 *
	 * @return
	 */
	protected boolean isSupportPaging() {
		return Boolean.TRUE;
	}

	/**
	 * 从offset开始的limit数量的T数据取得
	 *
	 * @param offset
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	protected abstract List<T> paging(Integer offset, Integer limit)
			throws Exception;

}
