<%@include file="/common/taglibs.jsp"%>
<h3><primo:label code="Add/Update Role"/></h3>
<form:form action="save.do" name="serviceForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="2"><b><primo:label code="Add/Update Role"/></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Role Name"/><span
				class="errorMessage">*</span></td>
			<td align="${left}"><c:if test="${empty modelObject.id}"><form:input path="name" cssClass="flat"  style="min-width:149px; max-width:149px" maxlength="20"/></c:if>
			<c:if test="${not empty modelObject.id}"><form:input path="name" cssClass="flat"  style="min-width:149px; max-width:149px"  readonly="true"/></c:if>
			<br><form:errors path="name" cssClass="errorMessage"/></td>
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Status"/></td>
		<td align="${left}">
			<form:select path="status" style="min-width:153px; max-width:153px">
				<form:option value="-1"><primo:label code="Please Select"/></form:option>
				<c:forEach var="item" items="${statuses}">
					<form:option value="${item.dataValue}" label="${item.dataText}"/>
				</c:forEach>
			</form:select>
		</td>
		</tr>
		<tr>
			<td align="${left}" colspan="2"><input type="submit" name="create" id="create"
				value="<primo:label code="Save"/>" class="flat" /> <input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>" class="flat" />
				<input type="button" id="cancelBtn" value="<primo:label code="Cancel"/>" class="flat" onClick="location.href='list.do'"/>
			</td>
		</tr>
	</table>
</form:form>
	
