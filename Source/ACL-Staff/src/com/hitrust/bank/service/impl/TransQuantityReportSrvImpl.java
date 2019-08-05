/**
 * @(#)TransQuantityReportSrvImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 交易量統計報表Service Impl
 * 
 * Modify History:
 *  v1.00, 2016/06/02, Jimmy
 *   1) First release
 *  
 *  v1.01, 2019/06/19, Organ  
 *   1) 交易量統計增加繳費稅 
 *  
 */

package com.hitrust.bank.service.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.hitrust.acl.common.I18nConverter;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.EcDataDAO;
import com.hitrust.bank.dao.VwTrnsDataDAO;
import com.hitrust.bank.model.CustAcntLink;
import com.hitrust.bank.model.VwTrnsData;
import com.hitrust.bank.service.TransQuantityReportSrv;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public class TransQuantityReportSrvImpl implements TransQuantityReportSrv {

	// ====== Log4j ======
	private static Logger LOG = Logger.getLogger(LoginSettingsSrvImpl.class);

	// ====== DAO injection ======
	private EcDataDAO ecDataDAO;
	private VwTrnsDataDAO vwTrnsDataDAO;

	// ====== injection beans ======
	public void setEcDataDAO(EcDataDAO ecDataDAO) {
		this.ecDataDAO = ecDataDAO;
	}

	public void setVwTrnsDataDAO(VwTrnsDataDAO vwTrnsDataDAO) {
		this.vwTrnsDataDAO = vwTrnsDataDAO;
	}

	// ====== implements ======
	/**
	 * 取得查詢初始化資料(平台代號清單)
	 * 
	 * @return
	 */
	@Override
	public VwTrnsData queryInit() {
		VwTrnsData vwTrnsData = new VwTrnsData();

		// 取得平台代號清單
		vwTrnsData.setEcData(ecDataDAO.getEcDataList());
		return vwTrnsData;
	}

	/**
	 * 平台總表查詢
	 * 
	 * @param vwTrnsData
	 */
	@Override
	public void queryPlatform(VwTrnsData vwTrnsData) {
		// 查詢總表資料
		List<?> rtn = vwTrnsDataDAO.getTrnsPlatformQuantity(vwTrnsData.getStrtDate(), vwTrnsData.getEndDate(),
				vwTrnsData.getqEcId(), vwTrnsData.getqTrnsStts());
		
		TreeMap<String, Object> sortedRtn = new TreeMap<String, Object>();

		// 分類交易資料
		for (int i = 0; i < rtn.size(); i++) {
			Map<Object, Object> m = (Map<Object, Object>) rtn.get(i);
			String EC_ID = (String) m.get("EC_ID");
			String type = (String) m.get("TRNS_TYPE");

			if (sortedRtn.get(EC_ID) == null) {
				sortedRtn.put(EC_ID, new HashMap<String, Object>());
			}
			((HashMap<String, Object>) sortedRtn.get(EC_ID)).put(type, m);
		}
		// 整理excel報表資料
		List<VwTrnsData> report = toReprot(sortedRtn, "platform");

		vwTrnsData.setReport(report);

		// 統計資料
		addUp(rtn, vwTrnsData);
	}

	/**
	 * 月報表查詢
	 * 
	 * @param vwTrnsData
	 */
	@Override
	public void queryMonthly(VwTrnsData vwTrnsData) {
		// 查詢月報資料
		List<?> rtn = vwTrnsDataDAO.getTrnsMonthlyQuantity(vwTrnsData.getStrtDate(), vwTrnsData.getEndDate(),
				vwTrnsData.getqEcId(),vwTrnsData.getqTrnsStts());

		// 分類交易資料
		TreeMap<String, Object> sortedRtn = arrange(rtn);

		// 整理excel報表資料
		List<VwTrnsData> report = toReprot(sortedRtn, "monthly");

		vwTrnsData.setReport(report);

		// 統計資料
		addUp(rtn, vwTrnsData);
	}

	/**
	 * 日報表查詢
	 * 
	 * @param vwTrnsData
	 */
	@Override
	public void queryDaily(VwTrnsData vwTrnsData) {
		// 查詢日報資料
		List<?> rtn = vwTrnsDataDAO.getTrnsDailyQuantity(vwTrnsData.getStrtDate(), vwTrnsData.getEndDate(),
				vwTrnsData.getqEcId(), vwTrnsData.getqTrnsStts());

		// 分類交易資料
		TreeMap<String, Object> sortedRtn = arrange(rtn);

		// 整理excel報表資料
		List<VwTrnsData> report = toReprot(sortedRtn, "daily");

		vwTrnsData.setReport(report);


		// 統計資料
		addUp(rtn, vwTrnsData);
	}

	/**
	 * 明細資料查詢
	 * 
	 * @param vwTrnsData
	 * @param trnsType
	 *            交易類型(A:扣款,B:退款)
	 * @param rptType
	 *            報表類型(platform:平台別，monthly:月報表，daily:日報表)
	 * @param ecId
	 *            平台代號
	 * @param page
	 *            分頁資訊
	 * @return
	 */
	@Override
	public PageQuery queryDetail(VwTrnsData vwTrnsData, String trnsType, String rptType, String ecId, Page page) {
		Date strtDate = DateUtil.formatStrToDate(vwTrnsData.getStrtDate(), "000000"); // 起始日期
		Date endDate = DateUtil.formatStrToDate(vwTrnsData.getEndDate(), "235959"); // 結束日期
		PageQuery rtn = null;

		if ("monthly".equals(rptType)) {
			// 格式化查詢月分
			String trnsMnth = vwTrnsData.getTrnsTime();

			if (!StringUtil.isBlank(trnsMnth)) {
				trnsMnth = trnsMnth.replace('/', '-');
			}
			// 查詢月報表
			rtn = vwTrnsDataDAO.getTrnsDetialQuantity(strtDate, endDate, trnsType, trnsMnth, "", ecId, page, vwTrnsData.getqTrnsStts());
		} else if ("daily".equals(rptType)) {
			// 格式化查詢日期
			String trnsDate = vwTrnsData.getTrnsTime();

			if (!StringUtil.isBlank(trnsDate)) {
				trnsDate = trnsDate.replace('/', '-');
			}
			// 查詢日報表
			rtn = vwTrnsDataDAO.getTrnsDetialQuantity(strtDate, endDate, trnsType, "", trnsDate, ecId, page, vwTrnsData.getqTrnsStts());
		} else if ("platform".equals(rptType) | "".equals(rptType)) {
			// 查詢總表
			rtn = vwTrnsDataDAO.getTrnsDetialQuantity(strtDate, endDate, trnsType, "", "", ecId, page, vwTrnsData.getqTrnsStts());
		}

		return rtn;
	}
	
	/**
	 * 明細資料查詢
	 * 
	 * @param vwTrnsData
	 * @param trnsType
	 *            交易類型(A:扣款,B:退款)
	 * @param rptType
	 *            報表類型(platform:平台別，monthly:月報表，daily:日報表)
	 * @param ecId
	 *            平台代號
	 * @param page
	 *            分頁資訊
	 * @param locale
	 * @return
	 */
	@Override
	public List<VwTrnsData> queryDetail(VwTrnsData vwTrnsData, String trnsType, String rptType, String ecId, Locale locale) {
		Date strtDate = DateUtil.formatStrToDate(vwTrnsData.getStrtDate(), "000000"); // 起始日期
		Date endDate = DateUtil.formatStrToDate(vwTrnsData.getEndDate(), "235959"); // 結束日期
		List<VwTrnsData> list = null;

		if ("monthly".equals(rptType)) {
			// 格式化查詢月分
			String trnsMnth = vwTrnsData.getTrnsTime();

			if (!StringUtil.isBlank(trnsMnth)) {
				trnsMnth = trnsMnth.replace('/', '-');
			}
			// 查詢月報表
			list = vwTrnsDataDAO.getTrnsDetialQuantity(strtDate, endDate, trnsType, trnsMnth, "", ecId, vwTrnsData.getqTrnsStts());
		} else if ("daily".equals(rptType)) {
			// 格式化查詢日期
			String trnsDate = vwTrnsData.getTrnsTime();

			if (!StringUtil.isBlank(trnsDate)) {
				trnsDate = trnsDate.replace('/', '-');
			}
			// 查詢日報表
			list = vwTrnsDataDAO.getTrnsDetialQuantity(strtDate, endDate, trnsType, "", trnsDate, ecId, vwTrnsData.getqTrnsStts());
		} else if ("platform".equals(rptType) | "".equals(rptType)) {
			// 查詢總表
			list = vwTrnsDataDAO.getTrnsDetialQuantity(strtDate, endDate, trnsType, "", "", ecId, vwTrnsData.getqTrnsStts());
		}
		
		for(VwTrnsData reportVwTrnsData : list){
			reportVwTrnsData.setRunDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(reportVwTrnsData.getTrnsDttm()));
			
			if("01".equals(reportVwTrnsData.getTrnsStts())){
				//不明
				reportVwTrnsData.setTrnsType(I18nConverter.i18nMapping("common.result.unknow."+reportVwTrnsData.getTrnsType(), locale));
			}else if("02".equals(reportVwTrnsData.getTrnsStts())){
				//成功
				reportVwTrnsData.setTrnsType(I18nConverter.i18nMapping("common.result.success."+reportVwTrnsData.getTrnsType(), locale));
			}else if("03".equals(reportVwTrnsData.getTrnsStts())){
				//失敗
				reportVwTrnsData.setTrnsType(I18nConverter.i18nMapping("common.result.failure."+reportVwTrnsData.getTrnsType(), locale));
			}
			LOG.debug("===>"+reportVwTrnsData.getTrnsType());
		}
		
		return list;
	}

	/**
	 * 整理查詢結果
	 */
	private TreeMap<String, Object> arrange(List<?> rtn) {
		TreeMap<String, Object> sortedRtn = new TreeMap<String, Object>();

		for (int i = 0; i < rtn.size(); i++) {
			Map<Object, Object> m = (Map<Object, Object>) rtn.get(i);
			String TRNS_TIME = (String) m.get("TRNS_TIME");
			m.put("TRNS_TIME", TRNS_TIME.replace('-', '/'));
			String EC_ID = (String) m.get("EC_ID");
			String type = (String) m.get("TRNS_TYPE");

			if (sortedRtn.get(TRNS_TIME + "_" + EC_ID) == null) {
				sortedRtn.put(TRNS_TIME + "_" + EC_ID, new HashMap<String, Object>());
			}
			((HashMap) sortedRtn.get(TRNS_TIME + "_" + EC_ID)).put(type, m);
		}

		return sortedRtn;
	}

	/**
	 * 統計資料
	 * 
	 * @param rtn
	 * @param vwTrnsData
	 * @param sortedRtn
	 */
	private void addUp(List<?> rtn, VwTrnsData vwTrnsData) {
		int SUMMARY_COUNT_A = 0;
		int SUMMARY_COUNT_B = 0;
		int SUMMARY_COUNT_C = 0;
		int SUMMARY_COUNT_D = 0;
		//20190625 交易量統計增加繳費稅 Begin
		int SUMMARY_COUNT_E = 0;
		//20190625 交易量統計增加繳費稅 End
		int SUMMARY_AMUNT_A = 0;
		int SUMMARY_AMUNT_B = 0;
		int SUMMARY_AMUNT_C = 0;
		int SUMMARY_AMUNT_D = 0;
		//20190625 交易量統計增加繳費稅 Begin
		int SUMMARY_AMUNT_E = 0;
		//20190625 交易量統計增加繳費稅 End
		int SUMMARY_FEE_AMUNT = 0;

		for (int i = 0; i < rtn.size(); i++) {
			Map<Object, Object> m = (Map<Object, Object>) rtn.get(i);
			if ("A".equals(m.get("TRNS_TYPE"))) {
				// 統計扣款筆數
				SUMMARY_COUNT_A += (int) m.get("COUNT");
				// 統計扣款金額
				SUMMARY_AMUNT_A += Integer.parseInt(m.get("TRNS_AMNT").toString());
			} else if("B".equals(m.get("TRNS_TYPE"))) {
				// 統計退款比數
				SUMMARY_COUNT_B += (int) m.get("COUNT");
				// 統計退款金額
				SUMMARY_AMUNT_B += Integer.parseInt(m.get("TRNS_AMNT").toString());
			}else if("C".equals(m.get("TRNS_TYPE"))){
				// 統計提領比數
				SUMMARY_COUNT_C += (int) m.get("COUNT");
				// 統計提領金額
				SUMMARY_AMUNT_C += Integer.parseInt(m.get("TRNS_AMNT").toString());
			//20190625 交易量統計增加繳費稅 Begin	
			}else if("D".equals(m.get("TRNS_TYPE"))){
				// 統計提領比數
				SUMMARY_COUNT_D += (int) m.get("COUNT");
				// 統計提領金額
				SUMMARY_AMUNT_D += Integer.parseInt(m.get("TRNS_AMNT").toString());
			}else {
				// 統計繳費稅筆數
				SUMMARY_COUNT_E += (int) m.get("COUNT");
				// 統計繳費稅金額
				SUMMARY_AMUNT_E += Integer.parseInt(m.get("TRNS_AMNT").toString());
			}
			//20190625 交易量統計增加繳費稅 End
			
			// 統計手續費
			SUMMARY_FEE_AMUNT += Integer.parseInt(m.get("FEE_AMNT").toString());
		}

		NumberFormat formatter = new DecimalFormat("#,###,###");

		// 設定顯示資料
		vwTrnsData.setSUMMARY_COUNT_A(formatter.format(SUMMARY_COUNT_A)); 					// 扣款總筆數
		vwTrnsData.setSUMMARY_COUNT_B(formatter.format(SUMMARY_COUNT_B)); 					// 退款總筆數
		vwTrnsData.setSUMMARY_COUNT_C(formatter.format(SUMMARY_COUNT_C)); 					// 提領總筆數
		vwTrnsData.setSUMMARY_COUNT_D(formatter.format(SUMMARY_COUNT_D)); 					// 儲值總筆數
		//20190625 交易量統計增加繳費稅 Begin
		vwTrnsData.setSUMMARY_COUNT_E(formatter.format(SUMMARY_COUNT_E)); 					// 繳費稅總筆數
		//20190625 交易量統計增加繳費稅 End
		vwTrnsData.setSUMMARY_AMUNT_A(formatter.format(SUMMARY_AMUNT_A)); 					// 扣款總金額
		vwTrnsData.setSUMMARY_AMUNT_B(formatter.format(SUMMARY_AMUNT_B)); 					// 退款總金額
		vwTrnsData.setSUMMARY_AMUNT_C(formatter.format(SUMMARY_AMUNT_C)); 					// 提領總金額
		vwTrnsData.setSUMMARY_AMUNT_D(formatter.format(SUMMARY_AMUNT_D)); 					// 儲值總金額
		//20190625 交易量統計增加繳費稅 Begin
		vwTrnsData.setSUMMARY_AMUNT_E(formatter.format(SUMMARY_AMUNT_E)); 					// 繳費稅總金額
		vwTrnsData.setSUMMARY_AMUNT_T(formatter.format(SUMMARY_AMUNT_A - SUMMARY_AMUNT_B - SUMMARY_AMUNT_C + SUMMARY_AMUNT_D + SUMMARY_AMUNT_E)); 
		//20190625 交易量統計增加繳費稅 End
		vwTrnsData.setSUMMARY_FEE_AMUNT(formatter.format(SUMMARY_FEE_AMUNT)); 				// 總手續費
	}

	/**
	 * 整理excel輸出格式
	 * 
	 * @param sortedRtn
	 * @param rptType
	 * @return
	 */
	private List<VwTrnsData> toReprot(TreeMap<String, Object> sortedRtn, String rptType) {
		List<VwTrnsData> XLS = new ArrayList<VwTrnsData>();
		for (Object key : sortedRtn.keySet()) {
			HashMap<String, Object> elements = (HashMap<String, Object>) sortedRtn.get(key);
			Map elementA = (Map) elements.get("A");
			Map elementB = (Map) elements.get("B");
			Map elementC = (Map) elements.get("C");
			Map elementD = (Map) elements.get("D");
			//20190625 交易量統計增加繳費稅 Begin
			Map elementE = (Map) elements.get("E");
			//20190625 交易量統計增加繳費稅 End 
			VwTrnsData XLS_Report = new VwTrnsData();

			NumberFormat formatter = new DecimalFormat("#,###,###");

			int formattedN_A = 0;
			int formattedN_B = 0;
			int formattedN_C = 0;
			int formattedN_D = 0;
			//20190625 交易量統計增加繳費稅 Begin
			int formattedN_E = 0;
			//20190625 交易量統計增加繳費稅 End 
			
			int feeamtN_A = 0;
			int feeamtN_B = 0;
			int feeamtN_C = 0;
			int feeamtN_D = 0;
			//20190625 交易量統計增加繳費稅 Begin
			int feeamtN_E = 0;
			//20190625 交易量統計增加繳費稅 End 
			
			if (elementA != null) {
				if(!"platform".equals(rptType)){
					XLS_Report.setTrnsTime(elementA.get("TRNS_TIME").toString());
				}
				XLS_Report.setEcId(elementA.get("EC_ID").toString());
				XLS_Report.setEcNameCh(elementA.get("EC_NAME_CH").toString());
				XLS_Report.setCountA(elementA.get("COUNT").toString());
				formattedN_A = Integer.parseInt(elementA.get("TRNS_AMNT").toString());
				feeamtN_A = Integer.parseInt(elementA.get("FEE_AMNT").toString());
				XLS_Report.setAmuntA(formatter.format(formattedN_A));
				XLS_Report.setCurrentFeeType(ccurrentFee(elementA));
			}else {
				XLS_Report.setCountA("0");
				XLS_Report.setAmuntA("0");
			}
			
			if (elementB != null) {
				if(!"platform".equals(rptType)){
					XLS_Report.setTrnsTime(elementB.get("TRNS_TIME").toString());
				}
				XLS_Report.setEcId(elementB.get("EC_ID").toString());
				XLS_Report.setEcNameCh(elementB.get("EC_NAME_CH").toString());
				XLS_Report.setCountB(elementB.get("COUNT").toString());
				formattedN_B = Integer.parseInt(elementB.get("TRNS_AMNT").toString());
				feeamtN_B = Integer.parseInt(elementB.get("FEE_AMNT").toString());
				XLS_Report.setAmuntB(formatter.format(formattedN_B));
				XLS_Report.setCurrentFeeType(ccurrentFee(elementB));
			}else {
				XLS_Report.setCountB("0");
				XLS_Report.setAmuntB("0");
			}
			
			if (elementC != null) {
				if(!"platform".equals(rptType)){
					XLS_Report.setTrnsTime(elementC.get("TRNS_TIME").toString());
				}
				XLS_Report.setEcId(elementC.get("EC_ID").toString());
				XLS_Report.setEcNameCh(elementC.get("EC_NAME_CH").toString());
				XLS_Report.setCountC(elementC.get("COUNT").toString());
				formattedN_C = Integer.parseInt(elementC.get("TRNS_AMNT").toString());
				feeamtN_C = Integer.parseInt(elementC.get("FEE_AMNT").toString());
				XLS_Report.setAmuntC(formatter.format(formattedN_C));
				XLS_Report.setCurrentFeeType(ccurrentFee(elementC));
			}else {
				XLS_Report.setCountC("0");
				XLS_Report.setAmuntC("0");
			}
			
			if (elementD != null) {
				if(!"platform".equals(rptType)){
					XLS_Report.setTrnsTime(elementD.get("TRNS_TIME").toString());
				}
				XLS_Report.setEcId(elementD.get("EC_ID").toString());
				XLS_Report.setEcNameCh(elementD.get("EC_NAME_CH").toString());
				XLS_Report.setCountD(elementD.get("COUNT").toString());
				formattedN_D = Integer.parseInt(elementD.get("TRNS_AMNT").toString());
				feeamtN_D = Integer.parseInt(elementD.get("FEE_AMNT").toString());
				XLS_Report.setAmuntD(formatter.format(formattedN_D));
				XLS_Report.setCurrentFeeType(ccurrentFee(elementD));
			}else {
				XLS_Report.setCountD("0");
				XLS_Report.setAmuntD("0");
			}
			
			 //20190625 交易量統計增加繳費稅 Begin
			if (elementE != null) {
				if(!"platform".equals(rptType)){
					XLS_Report.setTrnsTime(elementE.get("TRNS_TIME").toString());
				}
				XLS_Report.setEcId(elementE.get("EC_ID").toString());
				XLS_Report.setEcNameCh(elementE.get("EC_NAME_CH").toString());
				XLS_Report.setCountE(elementE.get("COUNT").toString());
				formattedN_E = Integer.parseInt(elementE.get("TRNS_AMNT").toString());
				feeamtN_E = Integer.parseInt(elementE.get("FEE_AMNT").toString());
				XLS_Report.setAmuntE(formatter.format(formattedN_E));
				XLS_Report.setCurrentFeeType(ccurrentFee(elementE));
			}else {
				XLS_Report.setCountE("0");
				XLS_Report.setAmuntE("0");
			}
			 //20190625 交易量統計增加繳費稅 End
			
			XLS_Report.setAmuntT(formatter.format(formattedN_A+formattedN_D+formattedN_E-formattedN_B-formattedN_C));
			XLS_Report.setFeeAmount(formatter.format(feeamtN_A+feeamtN_B+feeamtN_C+feeamtN_D+feeamtN_E));
			XLS.add(XLS_Report);
		}
		return XLS;
	}
	
	/**
	 * 整理"目前收費方式"顯式內容
	 * 
	 * @param element
	 * @return String 
	 */
	private String ccurrentFee(Map element){
		String fee="";
		if ("A".equals(element.get("FEE_TYPE").toString())) {
			DecimalFormat formatter2 = new DecimalFormat("#");
			fee = "定額收費:" + formatter2.format(element.get("FEE_RATE"));
		} else {
			fee = "比率收費:" + element.get("FEE_RATE").toString() + "%";
		}
		
		//20190625 交易量統計增加繳費稅 Begin
		if (null != element.get("TAX_RATE")) {
			if ("C".equals(element.get("TAX_TYPE").toString())) {
				DecimalFormat formatter2 = new DecimalFormat("#");
				fee = fee + "　繳費稅定額收費:" + formatter2.format(element.get("TAX_RATE"));
			} else if ("D".equals(element.get("TAX_TYPE").toString())) {
				fee = fee + "　繳費稅比率收費:" + element.get("TAX_RATE").toString() + "%";
			}
		}
		//20190625 交易量統計增加繳費稅 End
		return fee;
	}
	
	
	
}