<%@include file="/common/taglibs.jsp"%>
<c:set var="alertType" value="${param.alertType}"/>
<c:set var="expiringCount" value="${param.expiringCount}"/>
<c:set var="expiredCount" value="${param.expiredCount}"/>
<c:set var="alertListPg" value="${param.alertListPg}"/>
<c:set var="warningImgSrc" value="${ctx}/images/SignWarning.png"/>
<tr>
	<c:if test="${expiredCount != null && expiredCount ne 0 
			&& expiringCount != null && expiringCount ne 0}">
		<td><img src="${warningImgSrc}" alt="test" width="22" height="22"/></td>
		<td style="color:grey;">
			<h3>
				<a href="${alertListPg}" style="color:grey">${expiredCount} ${alertType} have expired and ${expiringCount} more expiring within 30 days.</a>
			</h3>
		</td>
	</c:if>
	<c:if test="${expiredCount != null && expiredCount ne 0 
			&& expiringCount != null && expiringCount eq 0}">
		<td><img src="${warningImgSrc}" alt="test" width="22" height="22"/></td>
		<td style="color:grey;">
			<h3>
				<a href="${alertListPg}" style="color:grey">${expiredCount} ${alertType} have expired.</a>
			</h3>
		</td>
	</c:if>
	<c:if test="${expiringCount != null && expiringCount ne 0 
			&& expiredCount != null && expiredCount eq 0}">
		<td><img src="${warningImgSrc}" alt="test" width="22" height="22"/></td>
		<td style="color:grey;">
			<h3>
				<a href="${alertListPg}" style="color:grey">${expiringCount} ${alertType} expiring within 30 days.</a>
			</h3>
		</td>
	</c:if>
</tr>