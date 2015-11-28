<%@ include file="/common/taglibs.jsp"%>
<%-- Error Messages --%>
<c:if test="${not empty error}">
    <div class="error">
        <c:forEach var="error" items="${error}">
			<img src="${ctx}/images/iconWarning.gif" alt="Warning" class="icon" />
			<blink><c:out value="${error}" escapeXml="false"/></blink><br />
        </c:forEach>
    </div>
    <c:remove var="error"/>
</c:if>

<%-- Success Messages --%>
<c:if test="${not empty msg}">
    <div class="message">
        <c:forEach var="message" items="${msg}">
            <img src="${ctx}/images/iconInformation.gif" alt="Information" class="icon" />
            <c:out value="${message}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="msg"/>
</c:if>
