<%@include file="/common/taglibs.jsp"%>
<form action="${ctx}/billbatchdate/save.do" name="billBatchDate" method="post">	
	<br></br>
	<table id="form-table" cellspacing="1" cellpadding="5">	
		<tr class="table-heading">
			<td align="${left}" align="${left}" colspan="4"><b><primo:label code="Batch Date"/></b></td>
		</tr>
		<tr>
			<td class="form-left"><b><primo:label code="Batch Date"/></b><span
				class="errorMessage">*</span>
			</td>
			<c:if test="${sessionScope.billbatchdate ne null}">
			<td align="${left}">
				<input id="datepicker" name="billBatchDate" value="${sessionScope.billbatchdate}" readonly="readonly"/>
			</td>
			</c:if>
			<c:if test="${sessionScope.billbatchdate eq null}">
			<td align="${left}">
				<input id="datepicker" name="billBatchDate" value="" readonly="readonly"/>
			</td>
			</c:if>
		</tr>
		<tr height="50px">
			<td width="150">&nbsp;</td>
			<td><input type="submit" value="Submit"/> </td>
		</tr>
	</table>
</form>