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
<table width="100%"><tbody><tr><td>&nbsp;&nbsp;<a href="/trans/driver/fuellog/create.do"><img src="/trans/images/file_add.png" border="0" title="Add" class="toolbarButton" height="40" width="40"></a>&nbsp;</td><td width="90"></td></tr></tbody></table>
<table style="cursor:pointer" class="datagrid" width="100%" cellspacing="0" cellpadding="2">
<tbody>
<tr>
<th>Tk#</th>
<th>Trans. Date</th>
<th>Gal</th>
<th>Fuel Card</th>
<th width="30">Edit</th>
<th width="30">Delete</th>
</tr>
<c:forEach items="${list}" var="fuelloglist" varStatus="listIndex">
<c:choose>
      <c:when test="${(listIndex.index % 2)=='0'}">
      		<tr class="even">      			
      </c:when>
      <c:otherwise>
      	<tr class="odd">      			
      </c:otherwise>
</c:choose>
<td>${fuelloglist.truck.unit}</td>
<td><fmt:formatDate pattern="MM-dd-yyyy" value="${fuelloglist.transactionDate}" /></td>
<td>${fuelloglist.gallons}</td>
<td>${fuelloglist.driverFuelCard.fuelcard.fuelcardNum}</td>
<td width="30"><a href="/trans/driver/fuellog/edit.do?id=${fuelloglist.id}"><img src="/trans/images/edit.png" border="0" alt="Edit"></a></td>
<td width="30"><a href="javascript:confirmDelete('/trans/driver/fuellog/delete.do?id=${fuelloglist.id}');"><img src="/trans/images/delete.png" border="0" alt="Delete"></a>
</td>
</tr>
</c:forEach>

<tr>
<td colspan="6">
</td>
</tr>
</tbody>
</table>

	
</form>
</div>