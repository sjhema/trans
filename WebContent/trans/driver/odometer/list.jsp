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
<table width="100%"><tbody><tr><td>&nbsp;&nbsp;<a href="/trans/driver/odometer/create.do"><img src="/trans/images/file_add.png" border="0" title="Add" class="toolbarButton" height="40" width="40"></a>&nbsp;</td><td width="90"></td></tr></tbody></table>
<table style="cursor:pointer" class="datagrid" width="100%" cellspacing="0" cellpadding="2">
<tbody>
<tr>
<th>Tk#</th>
<th>&nbsp;Date&nbsp;</th>
<th>Start</th>
<th>Finish</th>
<th>Miles</th>
<th width="30">Edit</th>
<th width="30">Delete</th>
</tr>
<c:forEach items="${list}" var="odolist" varStatus="listIndex">
<c:choose>
      <c:when test="${(listIndex.index % 2)=='0'}">
      		<c:choose>
      			<c:when test="${odolist.inComplete=='No'}">
      				<tr class="even">
      			</c:when>
      			<c:otherwise>
      				<tr class="evenS">
      			</c:otherwise>
      		</c:choose>
      </c:when>
      <c:otherwise>
      		<c:choose>
      			<c:when test="${odolist.inComplete=='No'}">
      				<tr class="odd">
      			</c:when>
      			<c:otherwise>
      				<tr class="oddS">
      			</c:otherwise>
      		</c:choose>
      </c:otherwise>
</c:choose>
<td>${odolist.truck.unit}</td>
<td><fmt:formatDate pattern="MM-dd-yyyy" value="${odolist.recordDate}" /></td>
<td>${odolist.startReading}</td>
<td>${odolist.endReading}</td>
<td>${odolist.miles}</td>
<td width="30"><a href="/trans/driver/odometer/edit.do?id=${odolist.id}"><img src="/trans/images/edit.png" border="0" alt="Edit"></a></td>
<td width="30"><a href="javascript:confirmDelete('/trans/driver/odometer/delete.do?id=${odolist.id}');"><img src="/trans/images/delete.png" border="0" alt="Delete"></a>
</td>
</tr>
</c:forEach>

<tr>
<td colspan="7">
</td>
</tr>
</tbody>
</table>

	
</form>
</div>