package com.primovision.lutransport.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.model.SearchCriteria;

public class DateUpdateServiceImpl implements DateUpdateService {

	@Override
	public void updateDate(HttpServletRequest request, String dateField,
			String dataField) {
		SearchCriteria criteria = (SearchCriteria) request.getSession()
		.getAttribute("searchCriteria");
		if (!StringUtils.isEmpty((String)criteria.getSearchMap().get(dateField))) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			try {
				Date billBatch = dateFormat.parse((String)criteria.getSearchMap().get(dateField));
				criteria.getSearchMap().put(dataField,ReportDateUtil.oracleFormatter.format(billBatch));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		criteria.getSearchMap().remove(dateField);
	}

}
