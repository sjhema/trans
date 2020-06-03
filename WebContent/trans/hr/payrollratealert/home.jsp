<%@include file="/common/taglibs.jsp"%>
<h1>Payroll Rate Expiration Alert</h1>
<c:set var="payrollRateAlertListPg" value="${ctx}/hr/payrollratealert/list.do"/>
<c:set var="driverPayrollRateAlertListPg" value="${payrollRateAlertListPg}?type=driverPayRate"/>
<c:set var="hourlyPayrollRateAlertListPg" value="${payrollRateAlertListPg}?type=hourlyPayRate"/>
<table border="0" style="margin-left: 20px; margin-top: 20px;">
	<c:if test="${expiredDriverPayRateList == null
			&& expiredHourlyPayRateList == null}">
		<tr>
			<td style="color:grey;">
				<h3>
					No alerts.
				</h3>
			</td>
		</tr>
	</c:if>			
	<c:if test="${(driverPayRateExprdCount != null && driverPayRateExprdCount ne 0)
				|| (driverPayRateExprngCount != null && driverPayRateExprngCount ne 0)}">
		<jsp:include page="alertInfo.jsp">
			<jsp:param name="alertType" value="Driver pay rate(s)"/>
		    <jsp:param name="expiredCount" value="${driverPayRateExprdCount}"/>
		    <jsp:param name="expiringCount" value="${driverPayRateExprngCount}"/>
		    <jsp:param name="alertListPg" value="${driverPayrollRateAlertListPg}"/>
		</jsp:include>
	</c:if>
	<c:if test="${(hourlyPayRateExprdCount != null && hourlyPayRateExprdCount ne 0)
				|| (hourlyPayRateExprngCount != null && hourlyPayRateExprngCount ne 0)}">
		<jsp:include page="alertInfo.jsp">
			<jsp:param name="alertType" value="Hourly rate(s)"/>
		    <jsp:param name="expiredCount" value="${hourlyPayRateExprdCount}"/>
		    <jsp:param name="expiringCount" value="${hourlyPayRateExprngCount}"/>
		    <jsp:param name="alertListPg" value="${hourlyPayrollRateAlertListPg}"/>
		</jsp:include>
	</c:if>
</table>