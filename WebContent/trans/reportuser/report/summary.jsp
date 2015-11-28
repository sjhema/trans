<%@include file="/common/taglibs.jsp"%>
<table width="50%" bordercolor="#5c91d5" border="1" cellpadding="5">
	<tr  >
			<th style="background:#5c91d5 " >Origin</th>
			<th style="background:#5c91d5 " >Destination</th>
			<th  style="background:#5c91d5 " >Load Count</th>
			
		</tr>
		
		<c:if test="${fn:length(list) eq 0}"><table border="0.5"><tr><tr ><td><b>No record</b></td></tr></table></c:if>
		

     <c:set var="Total" value="${0}" />
    <c:forEach items="${list}" var="item">
<% int i=0; %>
<tr >
    <c:if test="${fn:length(list) eq 0}">no record</c:if>

     <c:forEach items="${item}" var="ob">

     <% if (i==2){ %>
       <%--  <%= i %> --%>
<c:set var="Total" value="${Total + ob}" />
          <% } %>
       <% i++; %>
   <td width="50%" style="color: black;">${ob}</td>

 </c:forEach>

 </tr>
 
</c:forEach>
<%--  ${Total} --%>
</table>
<table width="50%">
  <tr ><td align="center" style="color: black;">Total Load count<td style="color: black;" align="right"><c:if test="${fn:length(list) ne 0}"><b>${Total}</b></c:if><td></tr>
</table>


 <%-- <form:form action="delete.do" id="deleteForm" name="deleteForm"> 
	<primo:datatable urlContext="reportuser/report/billinghistory" deletable="false"
		editable="false" exportPdf="false" exportXls="false"
		exportCsv="false" insertable="false" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="true" searcheable="false">
		<primo:textcolumn headerText="origin" dataField="origin" />
		<primo:textcolumn headerText="destination" dataField="destination" />
		<primo:textcolumn headerText="count" dataField="count" />
		</primo:datatable>
		</form:form>  --%>