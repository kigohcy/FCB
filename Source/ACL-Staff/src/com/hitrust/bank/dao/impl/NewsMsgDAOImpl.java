/**
 * @(#) NewsMsgDAO.java
 *
 * Directions: 
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 * v1.00, 2016年06月06日, Jimmy Yen
 * 1) JIRA-Number, First release
 *
 */
package com.hitrust.bank.dao.impl;

import java.util.Date;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.NewsMsgDAO;
import com.hitrust.bank.model.NewsMsg;
import com.hitrust.framework.dao.impl.BaseDAOImpl;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public class NewsMsgDAOImpl extends BaseDAOImpl implements NewsMsgDAO {

	@Override
	/**
	 * 查詢公告資訊
	 * 
	 * @param beginDate
	 *            公告起日
	 * @param endDate
	 *            公告迄日
	 * @param type
	 *            公告類型
	 * @param title
	 *            公告標題
	 * @param page
	 *            分頁資訊
	 * @return PageQuery
	 */
	public PageQuery queryMsgs(Date beginDate, Date endDate, String type, String title, Page page) {
		DetachedCriteria dc = DetachedCriteria.forClass(NewsMsg.class);

		if (endDate != null & beginDate != null) {
			dc.add(
					Restrictions.and(
							Restrictions.le("bgnDate", endDate),
							Restrictions.ge("endDate", beginDate)
					)
			);
		}

		if (!StringUtil.isBlank(type)) {
			dc.add(Restrictions.eq("type", type));
		}

		if (!StringUtil.isBlank(title)) {
			dc.add(Restrictions.like("title", "%" + title + "%"));
		}

		dc.addOrder(Order.asc("serl"));
		dc.addOrder(Order.desc("bgnDate"));

		return this.query(dc, page);
	}
	
	/**
	 * 根據公告序號刪除公告圖檔
	 * 
	 * @param seq 公告序號
	 */
	@Override
	public void deleteImagBySeq(String seq) {
		String hql = "delete from NewsImag where SEQ = '" + seq + "'";
		bulkUpdate(hql);
	}
	
	/**
	 * 根據公告序號異動公告圖檔
	 * 
	 * @param seq 公告序號
	 * @param userId 操作人員代號
	 * @param dttm 系統時間
	 */
	@Override
	public void updateImagBySeq(String seq, String userId, String dttm) {
		String hql = "update NewsImag set deltFlag='Y', mdfyUser='" + userId +
				     "', mdfyDttm='" + dttm + "' where SEQ = '" + seq + "'";
		bulkUpdate(hql);
	}
}
