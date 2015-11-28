package com.primovision.lutransport.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A backing bean for the main search form. Encapsulates the criteria needed to perform a search.
 */
@SuppressWarnings("unchecked")
public class SearchCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The user-provided search criteria.
     */
    private Map searchMap;
    private Map requestParams;

    public SearchCriteria() {
	searchMap = new HashMap();
    }

    /**
     * The maximum page size
     */
    private int pageSize = 25;

    private int recordCount = 0;

    @SuppressWarnings("unused")
    private int pageCount = 0;

    /**
     * The current page.
     */
    private int page;

    /**
     * @return the searchMap
     */
    public Map getSearchMap() {
	return searchMap;
    }

    /**
     * @return the requestParams
     */
    public Map getRequestParams() {
	return requestParams;
    }

    /**
     * @param requestParams
     *            the requestParams to set
     */
    public void setRequestParams(Map requestParams) {
	this.requestParams = requestParams;
    }

    /**
     * @param searchMap
     *            the searchMap to set
     */
    public void setSearchMap(Map searchMap) {
	this.searchMap = searchMap;
    }

    public int getPageSize() {
	return pageSize;
    }

    public void setPageSize(int pageSize) {
	this.pageSize = pageSize;
    }

    public int getPage() {
	return page;
    }

    public void setPage(int page) {
	this.page = page;
    }

    public void nextPage() {
	page++;
    }

    public void previousPage() {
	page--;
    }

    public void resetPage() {
	page = 0;
    }

    /**
     * @return the pageCount
     */
    public int getPageCount() {
	return ((recordCount % pageSize) == 0) ? (recordCount / pageSize) : (recordCount / pageSize) + 1;
    }

    /**
     * @return the recordCount
     */
    public int getRecordCount() {
	return recordCount;
    }

    /**
     * @param recordCount
     *            the recordCount to set
     */
    public void setRecordCount(int recordCount) {
	this.recordCount = recordCount;
    }

    /**
     * @param pageCount
     *            the pageCount to set
     */
    public void setPageCount(int pageCount) {
	this.pageCount = pageCount;
    }

    @Override
    public String toString() {
	StringBuffer paramString = new StringBuffer("");
	if (getSearchMap() != null) {
	    for (Object param : getSearchMap().keySet()) {
		if (getSearchMap().get(param.toString()) != null)
		    paramString.append(param + "=" + getSearchMap().get(param.toString())).append("&");
	    }
	}
	return paramString.toString();
    }

}