<%@ tag import="org.springframework.util.StringUtils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="searchCriteria" required="true" type="com.primovision.lutransport.model.SearchCriteria" rtexprvalue="true"%>
<%@ attribute name="pagedLink" required="true" type="java.lang.String" %>
<%@ attribute name="redirect" required="false" type="java.lang.String" %>
<span class="smalltext">
<c:if test="${searchCriteria.pageCount > 1}">
    Page ${searchCriteria.page+1} of ${searchCriteria.pageCount}&nbsp;&nbsp;
    <c:if test="${!(searchCriteria.page==0)}">
        <a href="<%=pagedLink%>?<%=(redirect == null?"":"redirect="+redirect+"&")%>p=0">First&nbsp;</a>
        <a href="<%=pagedLink%>?<%=(redirect == null?"":"redirect="+redirect+"&")%>p=${searchCriteria.page-1}">Previous&nbsp;</a>
    </c:if>
    <%
    int page = searchCriteria.getPage();
    int pageCount = 0;
    if(page + 20 <= searchCriteria.getPageCount())
    	pageCount = page + 20;
    else {
    	if (searchCriteria.getPageCount() - page < 20) {
    		if (searchCriteria.getPageCount() - 20 > 0)
				page = searchCriteria.getPageCount() - 20;
			else
				page = 0;
    	}
    	pageCount = searchCriteria.getPageCount();
    }
    for (int i = page; i < pageCount; i++) {
    %>
        <a href="<%=pagedLink%>?<%=(redirect == null?"":"redirect="+redirect+"&")%>p=<%=i%>"><%=(i+1)%></a>
    <%
    }
    jspContext.setAttribute("page",page);
    %>
    <c:if test="${page + 20 < searchCriteria.pageCount}">
        ...
    </c:if>
    <c:if test="${!(searchCriteria.page==searchCriteria.pageCount-1)}">
        <a href="<%=pagedLink%>?<%=(redirect == null?"":"redirect="+redirect+"&")%>p=${searchCriteria.page+1}">&nbsp;Next</a>
    </c:if>
    <c:if test="${page + 20 < searchCriteria.pageCount}">
        <a href="<%=pagedLink%>?<%=(redirect == null?"":"redirect="+redirect+"&")%>p=${searchCriteria.pageCount-1}">&nbsp;Last</a>
    </c:if>
</c:if>
</span>