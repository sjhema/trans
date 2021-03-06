<%@include file="/common/taglibs.jsp"%>
<h1>Rate Expiration Alert</h1>
<table border="0" style="margin-left: 20px; margin-top: 20px;">
<tr>
<c:if test="${billingrateExprdCount ne 0 && billingrateExprngCount ne 0}">
<td><img src="${ctx}/images/SignWarning.png" alt="test" width="22" height="22"/></td>
<td style="color:grey;"><h3><a href="${ctx}/rate/expirationreport/list.do?type=billingrate" style="color:grey">${billingrateExprdCount} Billing rates have been expired and ${billingrateExprngCount} more expiring within 30 days.
</a></h3></td>
</c:if>
<c:if test="${billingrateExprdCount ne 0 && billingrateExprngCount eq 0}">
<td><img src="${ctx}/images/SignWarning.png" alt="test" width="22" height="22"/></td>
<td style="color:grey;"><h3><a href="${ctx}/rate/expirationreport/list.do?type=billingrate" style="color:grey">${billingrateExprdCount} Billing rates have been expired.</a></h3></td>
</c:if>
<c:if test="${billingrateExprngCount ne 0 && billingrateExprdCount eq 0}">
<td><img src="${ctx}/images/SignWarning.png" alt="test" width="22" height="22"/></td>
<td style="color:grey;"><h3><a href="${ctx}/rate/expirationreport/list.do?type=billingrate" style="color:grey">${billingrateExprngCount} Billing rates expiring within 30 days.</a></h3></td>
</c:if>
</tr>

<tr>
<c:if test="${subcontractorRateExprdCount ne 0 && subcontractorRateExprngCount ne 0}">
<td><img src="${ctx}/images/SignWarning.png" alt="test" width="22" height="22"/></td>
<td style="color:grey;"><h3><a href="${ctx}/rate/expirationreport/list.do?type=subcontractorrate" style="color:grey">${subcontractorRateExprdCount} Subcontractor rates have been expired and ${subcontractorRateExprngCount} more expiring within 30 days.
</a></h3></td>
</c:if>
<c:if test="${subcontractorRateExprdCount ne 0 && subcontractorRateExprngCount eq 0}">
<td><img src="${ctx}/images/SignWarning.png" alt="test" width="22" height="22"/></td>
<td style="color:grey;"><h3><a href="${ctx}/rate/expirationreport/list.do?type=subcontractorrate" style="color:grey">${subcontractorRateExprdCount} Subcontractor rates have been expired.</a></h3></td>
</c:if>
<c:if test="${subcontractorRateExprngCount ne 0 && subcontractorRateExprdCount eq 0}">
<td><img src="${ctx}/images/SignWarning.png" alt="test" width="22" height="22"/></td>
<td style="color:grey;"><h3><a href="${ctx}/rate/expirationreport/list.do?type=subcontractorrate" style="color:grey">${subcontractorRateExprngCount} Subcontractor rates expiring within 30 days.</a></h3></td>
</c:if>
</tr>

<tr>
<c:if test="${fuelSurchargePaddExprdCount ne 0 && fuelSurchargePaddExprngCount ne 0}">
<td><img src="${ctx}/images/SignWarning.png" alt="test" width="22" height="22"/></td>
<td style="color:grey;"><h3><a href="${ctx}/rate/expirationreport/list.do?type=surchargepadd" style="color:grey">${fuelSurchargePaddExprdCount} Fuel Surcharge Padds have been expired and ${fuelSurchargePaddExprngCount} more expiring within 5 days.
</a></h3></td>
</c:if>
<c:if test="${fuelSurchargePaddExprdCount ne 0 && fuelSurchargePaddExprngCount eq 0}">
<td><img src="${ctx}/images/SignWarning.png" alt="test" width="22" height="22"/></td>
<td style="color:grey;"><h3><a href="${ctx}/rate/expirationreport/list.do?type=surchargepadd" style="color:grey">${fuelSurchargePaddExprdCount} Fuel Surcharge Padds have been expired.</a></h3></td>
</c:if>
<c:if test="${fuelSurchargePaddExprngCount ne 0 && fuelSurchargePaddExprdCount eq 0}">
<td><img src="${ctx}/images/SignWarning.png" alt="test" width="22" height="22"/></td>
<td style="color:grey;"><h3><a href="${ctx}/rate/expirationreport/list.do?type=surchargepadd" style="color:grey">${fuelSurchargePaddExprngCount} Fuel Surcharge Padds expiring within 5 days.</a></h3></td>
</c:if>
</tr>

<tr>
<c:if test="${weeklyRateExprdCount ne 0 && weeklyRateExprngCount ne 0}">
<td><img src="${ctx}/images/SignWarning.png" alt="test" width="22" height="22"/></td>
<td style="color:grey;"><h3><a href="${ctx}/rate/expirationreport/list.do?type=weeklyrate" style="color:grey">${weeklyRateExprdCount} Fuel Surcharge Weekly rates have been expired and ${weeklyRateExprngCount} more expiring within 5 days.
</a></h3></td>
</c:if>
<c:if test="${weeklyRateExprdCount ne 0 && weeklyRateExprngCount eq 0}">
<td><img src="${ctx}/images/SignWarning.png" alt="test" width="22" height="22"/></td>
<td style="color:grey;"><h3><a href="${ctx}/rate/expirationreport/list.do?type=weeklyrate" style="color:grey">${weeklyRateExprdCount} Fuel Surcharge Weekly rates have been expired.</a></h3></td>
</c:if>
<c:if test="${weeklyRateExprngCount ne 0 && weeklyRateExprdCount eq 0}">
<td><img src="${ctx}/images/SignWarning.png" alt="test" width="22" height="22"/></td>
<td style="color:grey;"><h3><a href="${ctx}/rate/expirationreport/list.do?type=weeklyrate" style="color:grey">${weeklyRateExprngCount} Fuel Surcharge Weekly rates expiring within 5 days.</a></h3></td>
</c:if>
</tr>

</table>