<%@include file="/common/taglibs.jsp"%>
<style type="text/css">
body {	
	color: black;
}

.datagrid td {
	border-right: 1px solid #fff;
	height: 20px;
	padding-left: 5px;
	color: black;
	background-color: #f7f7f7;
	vertical-align: middle
}
</style>

<div style="width: 100%; margin: 0px auto;">
<form id="deleteForm" name="deleteForm" action="delete.do" method="post">
<br>
<c:set var="totalAmount" value="${0}"/>
<c:forEach items="${list}" var="tripsheets">
<c:set var="totalAmount" value="${totalAmount + tripsheets.payRate}" />
</c:forEach>
<table width="100%"><tbody><tr><td width="90">&nbsp;&nbsp;<a href="/trans/driver/tripsheet/create.do"><img src="/trans/images/file_add.png" border="0" title="Add" height="40" width="40" class="toolbarButton"></a>&nbsp;&nbsp;&nbsp;&nbsp;
<a href="/trans/driver/tripsheet/exportdata.do?type=pdf"><img src="/trans/images/pdf-icon.png" border="0" title="Download Pdf" height="40" width="40" class="toolbarButton"></a>
&nbsp;&nbsp;
<a href="/trans/driver/reportleave/create.do"><img src="/trans/images/report leave.png" border="0" title="Report Leave" height="40" width="40" class="toolbarButton"></a>
</td>
<td width="230" align="left"><b>Total Amount: $<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${totalAmount}" /></b></td></tr></tbody></table>
<table style="cursor:pointer" class="datagrid" width="100%" cellspacing="0" cellpadding="2">
<tbody>
<tr>
<th>L#</th>
<th>Tk#</th>
<th>Tr#</th>
<th>Orig.</th>
<th>Orig. Tckt</th>
<th>Load&nbsp; Date</th>
<th>Dest.</th>
<th>Dest. Tckt</th>
<th>UnLoad Date</th>
<th>Amount</th>
<th width="30">Edit</th>
<th width="30">Delete</th>
</tr>
 
<c:forEach items="${list}" var="tripsheet" varStatus="listIndex">
<c:choose>
      <c:when test="${(listIndex.index % 2)=='0'}">
      		<c:choose>
      			<c:when test="${tripsheet.incomplete=='No'}">
      				<tr class="even">
      			</c:when>
      			<c:otherwise>
      				<tr class="evenS">
      			</c:otherwise>
      		</c:choose>
      </c:when>
      <c:otherwise>
      		<c:choose>
      			<c:when test="${tripsheet.incomplete=='No'}">
      				<tr class="odd">
      			</c:when>
      			<c:otherwise>
      				<tr class="oddS">
      			</c:otherwise>
      		</c:choose>
      </c:otherwise>
</c:choose>
<td align="center">${listIndex.index+1}</td>
<td>${tripsheet.truck.unit}</td>
<td>${tripsheet.trailer.unit}</td>
<td>${tripsheet.origin.name}</td>
<td>${tripsheet.originTicket}</td>
<td><fmt:formatDate pattern="MM-dd-yyyy" value="${tripsheet.loadDate}" /></td>
<td>${tripsheet.destination.name}</td>
<td>${tripsheet.destinationTicket}</td>
<td><fmt:formatDate pattern="MM-dd-yyyy" value="${tripsheet.unloadDate}" /></td>
<td>$${tripsheet.payRate}</td>
<td width="30"><a href="/trans/driver/tripsheet/edit.do?id=${tripsheet.id}"><img src="/trans/images/edit.png" border="0" alt="Edit"></a></td>
<td width="30"><a href="javascript:confirmDelete('/trans/driver/tripsheet/delete.do?id=${tripsheet.id}');"><img src="/trans/images/delete.png" border="0" alt="Delete"></a>
</td>
</tr>

</c:forEach>

<tr>
<td colspan="10">
</td>
</tr>
</tbody>
</table>

	
</form>
</div>