<%@include file="/common/taglibs.jsp"%>
<h1>Payroll Rate Expiration Alert</h1>
<table border="0" style="margin-left: 20px; margin-top: 20px;">
<tr>
<c:if test="${driverPayRateExprdCount != null && driverPayRateExprdCount ne 0 
		&& driverPayRateExprngCount != null && driverPayRateExprngCount ne 0}">
	<td><img src="${ctx}/images/SignWarning.png" alt="test" width="22" height="22"/></td>
	<td style="color:grey;">
		<h3>
			<a href="${ctx}/hr/payrollratealert/list.do?type=driverPayRate" style="color:grey">${driverPayRateExprdCount} Driver pay rate(s) have expired and ${driverPayRateExprngCount} more expiring within 30 days.</a>
		</h3>
	</td>
</c:if>
<c:if test="${driverPayRateExprdCount != null && driverPayRateExprdCount ne 0 
		&& driverPayRateExprngCount != null && driverPayRateExprngCount eq 0}">
	<td><img src="${ctx}/images/SignWarning.png" alt="test" width="22" height="22"/></td>
	<td style="color:grey;">
		<h3>
			<a href="${ctx}/hr/payrollratealert/list.do?type=driverPayRate" style="color:grey">${driverPayRateExprdCount} Driver pay rate(s) have expired.</a>
		</h3>
	</td>
</c:if>
<c:if test="${driverPayRateExprngCount != null && driverPayRateExprngCount ne 0 
		&& driverPayRateExprdCount != null && driverPayRateExprdCount eq 0}">
	<td><img src="${ctx}/images/SignWarning.png" alt="test" width="22" height="22"/></td>
	<td style="color:grey;">
		<h3>
			<a href="${ctx}/hr/payrollratealert/list.do?type=driverPayRate" style="color:grey">${driverPayRateExprngCount} Driver pay rate(s) expiring within 30 days.</a>
		</h3>
	</td>
</c:if>
</tr>
</table>