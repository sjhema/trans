<%@ include file="/common/taglibs.jsp"%>


<c:if test="${not empty errorlist}">
	<c:forEach var="error" items="${errorlist}">
		<div style="font:oblique; font-size:12px; color: red;";>
			<c:out value="${error}" escapeXml="false" />
			<br />
		</div>
	</c:forEach>

	<c:remove var="error" />
</c:if>

