package com.primovision.lutransport.core.tags;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.service.AuthenticationService;
import com.primovision.lutransport.web.SpringAppContext;

/**
 * A tag class to render a html table.
 * 
 */
public final class Datatable extends BodyTagSupport {
	private static final long serialVersionUID = -1092966441298785603L;
	private static Logger log = Logger.getLogger(Datatable.class);
	public static final String DEFAULT_NULLTEXT = "&nbsp;";

	private int border = 0;
	private int cellPadding = 0;
	private int cellSpacing = 0;
	private int width = 100;
	private boolean supportSorting = false;
	
	private boolean insertable;
	private boolean searcheable;
	private boolean editable;
	private boolean deletable;
	private boolean multipleDelete;
	private boolean exportPdf;
	private boolean exportXls;
	private boolean exportCsv;
	private boolean displayPrint;
	
	
	private String cssClass = null;
	private String bgColor = null;
	private String foreColor = null;
	private String id = null;
	private String name = null;
	private String dataMember = null;
	private SearchCriteria searchCriteria = null;
	private List columns = null;
	private List<AbstractBaseModel> baseObjects = null;
	private Object currItem = null;
	private String pagingLink=null;
	private String urlContext=null;
	private int additionalColumn=0;
	
	public Datatable() {
		super();
		this.border = 0;
		this.cellPadding = 0;
		this.cellSpacing = 0;
		this.width = 100;
		columns = new ArrayList();
		baseObjects = new ArrayList<AbstractBaseModel>();
	}
	
	public String getUrlContext() {
		return urlContext;
	}



	public void setUrlContext(String urlContext) {
		this.urlContext = urlContext;
	}



	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}


	/**
	 * @return the columns
	 */
	public List getColumns() {
		return columns;
	}


	/**
	 * @param columns the columns to set
	 */
	public void setColumns(List columns) {
		this.columns = columns;
	}


	/**
	 * @return the currItem
	 */
	public Object getCurrItem() {
		return currItem;
	}


	/**
	 * @param currItem the currItem to set
	 */
	public void setCurrItem(Object currItem) {
		this.currItem = currItem;
	}


	/**
	 * @return the baseObjects
	 */
	public List<AbstractBaseModel> getBaseObjects() {
		return baseObjects;
	}


	/**
	 * @param baseObjects the baseObjects to set
	 */
	public void setBaseObjects(List<AbstractBaseModel> baseObjects) {
		this.baseObjects = baseObjects;
	}


	/**
	 * @return the searchCriteria
	 */
	public SearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

	/**
	 * @param searchCriteria
	 *            the searchCriteria to set
	 */
	public void setSearchCriteria(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	/**
	 * @return the supportSorting
	 */
	public boolean isSupportSorting() {
		return supportSorting;
	}

	/**
	 * @param supportSorting
	 *            the supportSorting to set
	 */
	public void setSupportSorting(boolean supportSorting) {
		this.supportSorting = supportSorting;
	}

	
	/**
	 * @return the editable
	 */
	public boolean isEditable() {
		return editable;
	}


	/**
	 * @param editable the editable to set
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}


	/**
	 * @return the deletable
	 */
	public boolean isDeletable() {
		return deletable;
	}


	/**
	 * @param deletable the deletable to set
	 */
	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}


	/**
	 * @return the multipleDelete
	 */
	public boolean isMultipleDelete() {
		return multipleDelete;
	}


	/**
	 * @param multipleDelete the multipleDelete to set
	 */
	public void setMultipleDelete(boolean multipleDelete) {
		this.multipleDelete = multipleDelete;
	}


	/**
	 * @return the exportPdf
	 */
	public boolean isExportPdf() {
		return exportPdf;
	}


	/**
	 * @param exportPdf the exportPdf to set
	 */
	public void setExportPdf(boolean exportPdf) {
		this.exportPdf = exportPdf;
	}

	

	/**
	 * @return the exportXls
	 */
	public boolean isExportXls() {
		return exportXls;
	}


	/**
	 * @param exportXls the exportXls to set
	 */
	public void setExportXls(boolean exportXls) {
		this.exportXls = exportXls;
	}


	public boolean isExportCsv() {
		return exportCsv;
	}


	public void setExportCsv(boolean exportCsv) {
		this.exportCsv = exportCsv;
	}


	/**
	 * @return the displayPrint
	 */
	public boolean isDisplayPrint() {
		return displayPrint;
	}


	/**
	 * @param displayPrint the displayPrint to set
	 */
	public void setDisplayPrint(boolean displayPrint) {
		this.displayPrint = displayPrint;
	}


	/*------------------------------------------------------------------------------
	 * Getters
	 *----------------------------------------------------------------------------*/
	/**
	 * Returns grid width.
	 * 
	 * @return grid width.
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Returns border width of the grid.
	 * 
	 * @return border width
	 */
	public int getBorder() {
		return this.border;
	}

	/**
	 * Returns cellspacing between table cells.
	 * 
	 * @return cellsapcing between table cells.
	 */
	public int getCellSpacing() {
		return this.cellSpacing;
	}

	/**
	 * Returns amount of padding to be applied for a table cell.
	 * 
	 * @return padding for a table cell.
	 */
	public int getCellPadding() {
		return this.cellPadding;
	}

	/**
	 * Returns background color to be used for entire table.
	 * 
	 * @return background color value.
	 */
	public String getBgColor() {
		return this.bgColor;
	}

	/**
	 * Returns foreground color to be used for entire table.
	 * 
	 * @return foreground color value.
	 */
	public String getForeColor() {
		return this.foreColor;
	}

	/**
	 * Returns the id to be used to identify this table in HTML DOM.
	 * 
	 * @return table identifier.
	 */
	public String getID() {
		return this.id;
	}

	/**
	 * Returns the short name to be used to identify this table in HTML DOM.
	 * 
	 * @return table's short name identifier.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns either the SQL statement used for data retrieval or the class
	 * name of the item in list.
	 * 
	 * @return data member either SQL statement of class name.
	 */
	public String getDataMember() {
		return this.dataMember;
	}

	/**
	 * Returns CSS class to be associated with the table.
	 * 
	 * @return CSS class for table.
	 */
	public String getCssClass() {
		return this.cssClass;
	}

	/**
	 * @return the insertable
	 */
	public boolean isInsertable() {
		return insertable;
	}


	/**
	 * @param insertable the insertable to set
	 */
	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}


	/**
	 * @return the searcheable
	 */
	public boolean isSearcheable() {
		return searcheable;
	}


	/**
	 * @param searcheable the searcheable to set
	 */
	public void setSearcheable(boolean searcheable) {
		this.searcheable = searcheable;
	}


	/*------------------------------------------------------------------------------
	 * Setters
	 *----------------------------------------------------------------------------*/
	/**
	 * Sets grid width.
	 * 
	 * @param pintWidth
	 *            new grid width.
	 */
	public void setWidth(int pintWidth) {
		if (pintWidth >= 0)
			this.width = pintWidth;
	}

	/**
	 * Sets border width of the grid.
	 * 
	 * @param pintBorder
	 *            border width
	 */
	public void setBorder(int pintBorder) {
		if (pintBorder >= 0)
			this.border = pintBorder;
	}

	/**
	 * Sets cellspacing between table cells.
	 * 
	 * @param pintCellSpacing
	 *            cellsapcing between table cells.
	 */
	public void setCellSpacing(int pintCellSpacing) {
		if (pintCellSpacing >= 0)
			this.cellSpacing = pintCellSpacing;
	}

	/**
	 * Sets amount of padding to be applied for a table cell.
	 * 
	 * @param pintCellPadding
	 *            padding for a table cell.
	 */
	public void setCellPadding(int pintCellPadding) {
		if (pintCellPadding >= 0)
			this.cellPadding = pintCellPadding;
	}

	/**
	 * Sets background color to be used for entire table.
	 * 
	 * @param pstrBgColor
	 *            background color value.
	 */
	public void setBgColor(String pstrBgColor) {
		this.bgColor = pstrBgColor;
	}

	/**
	 * Sets foreground color to be used for entire table.
	 * 
	 * @param color
	 *            foreground color value.
	 */
	public void setForeColor(String color) {
		this.foreColor = color;
	}

	/**
	 * Sets the id to be used to identify this table in HTML DOM.
	 * 
	 * @param pstrID
	 *            table identifier.
	 */
	public void setID(String pstrID) {
		this.id = pstrID;
		this.name = pstrID;
	}

	
	/**
	 * @return the pagingLink
	 */
	public String getPagingLink() {
		return pagingLink;
	}


	/**
	 * @param pagingLink the pagingLink to set
	 */
	public void setPagingLink(String pagingLink) {
		this.pagingLink = pagingLink;
	}


	/**
	 * Sets the short name to be used to identify this table in HTML DOM.
	 * 
	 * @param pstrName
	 *            table's short name identifier.
	 */
	public void setName(String pstrName) {
		this.name = pstrName;
		this.id = pstrName;
	}

	/**
	 * Sets either the SQL statement used for data retrieval or the class name
	 * of the item in list.
	 * 
	 * @param pstrDataMember
	 *            data member either SQL statement of class name.
	 */
	public void setDataMember(String pstrDataMember) {
		this.dataMember = pstrDataMember;
	}

	/**
	 * Sets CSS class to be associated with the table.
	 * 
	 * @param cssClass
	 *            CSS class for table.
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}


	/*------------------------------------------------------------------------------
	 * Overridden Methods
	 * @see javax.servlet.jsp.tagext.Tag
	 *----------------------------------------------------------------------------*/
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		// If columns are not defined then report an error
		if (this.columns.size() == 0)
			throw new JspException("Error: No columns defined for the table!");
		drawGrid();
		return EVAL_PAGE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() {
		return EVAL_BODY_INCLUDE;
	}

	/**
	 * Helper method used by contained column tags to let know the grid about
	 * the columns to be displayed.
	 * 
	 * @param pobjCol
	 *            The column object.
	 */
	public void addColumn(IColumnTag pobjCol) {
		this.columns.add(pobjCol);
	}

	/**
	 * Helper method for retrieving value of a particular column.
	 * 
	 * @param col
	 *            The name of the column whose value is to be retrieved.
	 * @return The column value.
	 */
	public Object getColumnValue(String col) {
		Object objRet = null;
		try {
			if (col != null) {
				objRet = PropertyUtils.getProperty(this.currItem, col);
			}
		} catch (IllegalAccessException IAEx) {
			IAEx.printStackTrace();
			log.warn("Illegal Access Exception :"+IAEx);
		} catch (InvocationTargetException ITargetEx) {
			ITargetEx.printStackTrace();
			log.warn("Invocation Target Exception :"+ITargetEx);
		} catch (NoSuchMethodException NSMEx) {
			NSMEx.printStackTrace();
			log.warn("No SuchMethod Exception :"+NSMEx);
		} catch (NestedNullException NSMEx) {
			NSMEx.printStackTrace();
			log.warn("Nested Null Exception :"+NSMEx);
		}
		return objRet;
	}

	/*------------------------------------------------------------------------------
	 * Helpers
	 *----------------------------------------------------------------------------*/
	private void drawGrid() throws JspException {
		String strFld = null;
		IColumnTag objCol = null;
		JspWriter objOut = null;
		Iterator iterCol = null;
		ImageColumn editColumn = null;
		ImageColumn deleteColumn = null;
		TextColumn checkBoxColumn=null;
		User user = (User)pageContext.getSession().getAttribute("userInfo");
		AuthenticationService authenticationService = (AuthenticationService)SpringAppContext.getBean("authenticationService");
		try {
			drawToolbar();
			drawTableStart();
			drawHeaderRow();
			
			if (baseObjects ==null || baseObjects.size() == 0) {
				drawEmptyRow();
			}
			else {
				objOut = this.pageContext.getOut();
				if (editable) {
					String url = "/"+urlContext+"/edit.do";
					if (authenticationService.hasUserPermission(user, url)) {
						editColumn = new ImageColumn();
						editColumn.setImageSrc(pageContext.getAttribute("resourceCtx")+"/images/edit.png");
						editColumn.setImageBorder(0);
						editColumn.setWidth("30");
						editColumn.setPageContext(this.pageContext);
						this.columns.add(editColumn);
					}
				}
				if (deletable) {
					String url = "/"+urlContext+"/delete.do";
					if (authenticationService.hasUserPermission(user, url)) {
						deleteColumn = new ImageColumn();
						deleteColumn.setImageSrc(pageContext.getAttribute("resourceCtx")+"/images/delete.png");
						deleteColumn.setPageContext(this.pageContext);
						deleteColumn.setImageBorder(0);
						deleteColumn.setWidth("30");
						this.columns.add(deleteColumn);
					}
				}
				if (multipleDelete) {
					checkBoxColumn = new TextColumn();
					checkBoxColumn.setPageContext(this.pageContext);
					this.columns.add(0, checkBoxColumn);
				}
				for (int i = 0; i < baseObjects.size(); i++) {
					if ((i % 2) == 0)
						objOut.println("<tr class=\"even\">");
					else
						objOut.println("<tr>");
					iterCol = null;
					this.currItem = baseObjects.get(i);
					if (editColumn!=null)
						editColumn.setLinkUrl(pageContext.getAttribute("ctx")+"/"+urlContext+"/edit.do?id="+PropertyUtils.getProperty(currItem, "id"));
					if (deleteColumn!=null)
						deleteColumn.setLinkUrl("javascript:confirmDelete('"+pageContext.getAttribute("ctx")+"/"+urlContext+"/delete.do?id="+PropertyUtils.getProperty(currItem, "id")+"');");
//						deleteColumn.setLinkUrl(pageContext.getAttribute("ctx")+"/"+urlContext+"/delete.do?id="+PropertyUtils.getProperty(currItem, "id"));
					if (multipleDelete) {
						checkBoxColumn.setBodyContent("<input type=\"checkbox\" id=\"objId\" name=\"id\"	value=\""+PropertyUtils.getProperty(currItem, "id")+"\" />");
					}
					for (iterCol = this.columns.iterator(); iterCol.hasNext();) {
						objCol = null;
						objCol = (IColumnTag) iterCol.next();
						if (objCol instanceof RowNumColumn)
							objCol.renderDetail(new Integer(i + 1));
						else {
							strFld = objCol.getDataField();
							Object value = getColumnValue(strFld);
							if (value !=null)
								objCol.renderDetail(value);
							else
								objCol.renderDetail(objCol.getBodyContent());
						}
					}
					objCol = null;
					objOut.println("</tr>");
				}
				if (this.searchCriteria != null) {
					objOut.println("<tr>");
					objOut.println("<td colspan=" + this.columns.size() + ">");
					drawTablePaging();
					objOut.println("</td>");
					objOut.println("</tr>");
				}
				objOut.println("</table>");
			}
		} catch (IOException IOEx) {
			IOEx.printStackTrace();
			log.warn("Unable to write grid contents :"+IOEx);
			throw new JspException("Error: Unable to write grid contents!",
					IOEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.warn("Unknown error occured!"+ex);
			throw new JspException("Error: Unknown error occured!", ex);
		} finally {
			if (objCol != null)
				objCol = null;
			if (objOut != null)
				objOut = null;
			if (iterCol != null)
				iterCol = null;
			if (columns!=null)
				columns.clear();
		}
	}

	private void drawEmptyRow() throws JspException {
		int intCntr = 0;
		JspWriter objOut = null;

		try {
			objOut = this.pageContext.getOut();
			for (intCntr = 0; intCntr < 1; intCntr++) {
				if ((intCntr % 2) == 0)
					objOut.println("<tr CLASS=\"gridRowEven\">");
				else
					objOut.println("<tr CLASS=\"gridRowOdd\">");
				objOut.print("<td COLSPAN=");
				objOut.print(this.columns.size()+additionalColumn);
				if (intCntr == 0)
					objOut.println(">No records to display!</td>");
				else
					objOut.println(">&nbsp;</td>");
				objOut.println("</tr>");
			}
			objOut.println("</table>");
		} catch (IOException IOEx) {
			throw new JspException("Error: Exception while writing to client!",
					IOEx);
		} finally {
			if (objOut != null)
				objOut = null;
		}
	}

	private void drawTableStart() throws JspException {
		StringBuffer objBuf = null;

		objBuf = new StringBuffer();
		objBuf.append("<table style=\"cursor:pointer\"");
		if (this.cssClass != null)
			objBuf.append(" class=\"" + this.cssClass + "\"");
		else
			objBuf.append(" class=\"datagrid\"");
		objBuf.append(" width=\"" + this.width + "%\"");
		objBuf.append(" cellspacing=" + this.cellSpacing);
		objBuf.append(" cellpadding=" + this.cellPadding);
		if (this.id != null)
			objBuf.append(" id=\"" + this.id + "\"");
		if (this.name != null)
			objBuf.append(" name=\"" + this.name + "\"");
		if (this.foreColor != null)
			objBuf.append(" color=\"" + this.foreColor + "\"");
		if (this.bgColor != null)
			objBuf.append(" BGCOLOR=\"" + this.bgColor + "\"");
		objBuf.append(">\r\n");
		try {
			this.pageContext.getOut().println(objBuf.toString());
		} catch (IOException IOEx) {
			throw new JspException("Error: Error while writing to client!",
					IOEx);
		}
		objBuf = null;
	}

	/**
	 * Helper function to render the header row of the Datatable.
	 * 
	 * @throws JspException
	 */
	private void drawHeaderRow() throws JspException {
		JspWriter objOut = null;
		IColumnTag objCol = null;
		Iterator iterCol = null;

		try {
			objOut = this.pageContext.getOut();
			objOut.println("<tr>");
			if (multipleDelete) {
				objOut.println("<th width=\"30\"><input type=\"checkbox\" id=\"checkId\" onclick=\"javascript:checkUncheck(this,'id');\" /></th>");
				additionalColumn++;
			}
			
			List<IColumnTag> columnPropertyList = new ArrayList<IColumnTag>();
			for (iterCol = this.columns.iterator(); iterCol.hasNext();) {
				objCol = null;
				objCol = (IColumnTag) iterCol.next();
				objCol.renderHeader();
				//populating columnPropertyList with IColumnTag to be exported to jasper report
				if (!(objCol instanceof ImageColumn))
					columnPropertyList.add(objCol);
			}
			this.pageContext.setAttribute("columnPropertyList", columnPropertyList);
			User user = (User)pageContext.getSession().getAttribute("userInfo");
			AuthenticationService authenticationService = (AuthenticationService)SpringAppContext.getBean("authenticationService");
			String locale=(String)pageContext.getSession().getAttribute("lang");
			if (editable) {
				String url = "/"+urlContext+"/edit.do";
				if (authenticationService.hasUserPermission(user, url)) {
					objOut.println("<th width=\"30\">"+CacheUtil.getText("messageResourceCache","label_Edit_"+locale)+"</th>");
					additionalColumn++;
				}
			}
			if (deletable) {
				String url = "/"+urlContext+"/delete.do";
				if (authenticationService.hasUserPermission(user, url)){
					objOut.println("<th  width=\"30\">"+CacheUtil.getText("messageResourceCache","label_Delete_"+locale)+"</th>");
					additionalColumn++;
				}
			}
			objCol = null;
			objOut.println("</tr>");
		} catch (IOException IOEx) {
			throw new JspException("Error: Unable to render grid header!", IOEx);
		} finally {
			if (objCol != null)
				objCol = null;
			if (iterCol != null)
				iterCol = null;
			if (objOut != null)
				objOut = null;
		}
	}

	private void drawToolbar() throws JspException {
		JspWriter objOut = null;
		User user = (User)pageContext.getSession().getAttribute("userInfo");
		AuthenticationService authenticationService = (AuthenticationService)SpringAppContext.getBean("authenticationService");
		try {
			objOut = this.pageContext.getOut();
			objOut.write("<table width=\"100%\"><tr><td>");
			if (insertable) {
				String url = "/"+urlContext+"/create.do";
				if (authenticationService.hasUserPermission(user, url))
					objOut.write("<a href=\""+pageContext.getAttribute("ctx")+"/"+urlContext+"/create.do\"><img src=\""+pageContext.getAttribute("resourceCtx")+"/images/add.png\" border=\"0\" title=\"Add\" class=\"toolbarButton\"/></a>&nbsp;");
			}
			if (multipleDelete) {
				String url = "/"+urlContext+"/bulkdelete.do";
				if (authenticationService.hasUserPermission(user, url))
					objOut.write("<a href=\"#\" onclick=\"javascript:removeData();\"><img src=\""+pageContext.getAttribute("resourceCtx")+"/images/delete.png\" border=\"0\" title=\"Delete\" class=\"toolbarButton\"/></a>&nbsp;");
			}
			if (searcheable) {
				String url = "/"+urlContext+"/search.do";
				if (authenticationService.hasUserPermission(user, url))
					objOut.write("<a href=\"javascript:showDiv('search');\"><img src=\""+pageContext.getAttribute("resourceCtx")+"/images/search.png\" border=\"0\" title=\"Search\" class=\"toolbarButton\"/></a>&nbsp;");
			}
			objOut.write("</td><td width=\"90\">");
			if (displayPrint) {		
				objOut.write("<a href=\"print.do\" onClick=\"smallPopup(this.href);return false;\"><img src=\""+pageContext.getAttribute("resourceCtx")+"/images/print.png\" border=\"0\" class=\"toolbarButton\"/></a>&nbsp;");
			}
			String url = "/"+urlContext+"/export.do";
			if (authenticationService.hasUserPermission(user, url)) {
				if (exportPdf) {
					objOut.write("<a href=\""+pageContext.getAttribute("ctx")+"/"+urlContext+"/export.do?type=pdf\"><img src=\""+pageContext.getAttribute("resourceCtx")+"/images/pdf.png\" border=\"0\" class=\"toolbarButton\"/></a>&nbsp;");
				}
				if (exportXls) {
					objOut.write("<a href=\""+pageContext.getAttribute("ctx")+"/"+urlContext+"/export.do?type=xls\"><img src=\""+pageContext.getAttribute("resourceCtx")+"/images/excel.png\" border=\"0\" class=\"toolbarButton\"/></a>");
				}
				if (exportCsv) {
					objOut.write("<a href=\""+pageContext.getAttribute("ctx")+"/"+urlContext+"/export.do?type=csv\"><img src=\""+pageContext.getAttribute("resourceCtx")+"/images/csv.png\" border=\"0\" class=\"toolbarButton\"/></a>");
				}
			}
			objOut.write("</td></tr></table>");
		} catch (IOException IoEx) {
			throw new JspException("Error: Writing empty paging!", IoEx);
		} finally {
			if (objOut != null)
				objOut = null;
		}

	}
	
	private void drawTablePaging() throws JspException {
		JspWriter objOut = null;
		try {
			objOut = this.pageContext.getOut();
			if (searchCriteria.getPageCount() > 1) {
				objOut.write("Page " + (searchCriteria.getPage() + 1) + " of "
						+ searchCriteria.getPageCount() + "&nbsp; &nbsp;");
			}
			if (!(searchCriteria.getPage() == 0)) {
				objOut.write("<a href=\"" + pagingLink
						+ "?" 
						+ "&p=0\">First&nbsp;</a>");
				objOut.write("<a href=\"" + pagingLink
						+ "?" + "&p="
						+ (searchCriteria.getPage() - 1)
						+ "\">Previous&nbsp;</a>");
			}
			if(searchCriteria.getPageCount()!=1) {
				if (searchCriteria.getPageCount()<30) {
					for (int i = 0; i < searchCriteria.getPageCount(); i++) {
						objOut.write("<a href=\"" + pagingLink+ "?" + "&p=" + i + "\">"	+ (i + 1) + "</a>&nbsp;");
					}
				}
				else {
					if (searchCriteria.getPage()<10){
						for (int i = 0; i < searchCriteria.getPage(); i++)
							objOut.write("<a href=\"" + pagingLink+ "?" + "&p=" + i + "\">"	+ (i + 1) + "</a>&nbsp;");
						for (int i = searchCriteria.getPage(); i < searchCriteria.getPage()+5; i++)
							objOut.write("<a href=\"" + pagingLink+ "?" + "&p=" + i + "\">"	+ (i + 1) + "</a>&nbsp;");
						objOut.write("..........");
						for (int i = searchCriteria.getPageCount()-5; i < searchCriteria.getPageCount(); i++)
							objOut.write("<a href=\"" + pagingLink+ "?" + "&p=" + i + "\">"	+ (i + 1) + "</a>&nbsp;");
					}
					if (searchCriteria.getPage()>=10 && searchCriteria.getPage()<15) {
						for (int i = 0; i < 5; i++)
							objOut.write("<a href=\"" + pagingLink+ "?" + "&p=" + i + "\">"	+ (i + 1) + "</a>&nbsp;");
						objOut.write("..........");
						for (int i = searchCriteria.getPage(); i < searchCriteria.getPage()+5; i++)
							objOut.write("<a href=\"" + pagingLink+ "?" + "&p=" + i + "\">"	+ (i + 1) + "</a>&nbsp;");
						objOut.write("..........");
						for (int i = searchCriteria.getPageCount()-5; i < searchCriteria.getPageCount(); i++)
							objOut.write("<a href=\"" + pagingLink+ "?" + "&p=" + i + "\">"	+ (i + 1) + "</a>&nbsp;");
					}
					if (searchCriteria.getPage()>=15) {
						for (int i = 0; i < 5; i++)
							objOut.write("<a href=\"" + pagingLink+ "?" + "&p=" + i + "\">"	+ (i + 1) + "</a>&nbsp;");
						objOut.write("..........");
						for (int i = searchCriteria.getPage()-5; i < searchCriteria.getPage(); i++)
							objOut.write("<a href=\"" + pagingLink+ "?" + "&p=" + i + "\">"	+ (i + 1) + "</a>&nbsp;");
						if (searchCriteria.getPageCount()-searchCriteria.getPage()>10) {
							for (int i = searchCriteria.getPage(); i < searchCriteria.getPage()+5; i++)
								objOut.write("<a href=\"" + pagingLink+ "?" + "&p=" + i + "\">"	+ (i + 1) + "</a>&nbsp;");
							objOut.write("..........");
							for (int i = searchCriteria.getPageCount()-5; i < searchCriteria.getPageCount(); i++)
								objOut.write("<a href=\"" + pagingLink+ "?" + "&p=" + i + "\">"	+ (i + 1) + "</a>&nbsp;");
						}
						else {
							for (int i = searchCriteria.getPage(); i < searchCriteria.getPageCount(); i++)
								objOut.write("<a href=\"" + pagingLink+ "?" + "&p=" + i + "\">"	+ (i + 1) + "</a>&nbsp;");
						}
					}
				}
			}
			if (!(searchCriteria.getPage() == searchCriteria.getPageCount() - 1)) {
				objOut.write("<a href=\"" + pagingLink
						+ "?" + "&p="
						+ (searchCriteria.getPage() + 1) + "\">&nbsp;Next</a>");
				objOut.write("<a href=\"" + pagingLink
						+ "?" + "&p="
						+ (searchCriteria.getPageCount() - 1)
						+ "\">&nbsp;Last</a>");
			}
		} catch (IOException IoEx) {
			throw new JspException("Error: Writing empty paging!", IoEx);
		} finally {
			if (objOut != null)
				objOut = null;
		}

	}

}
