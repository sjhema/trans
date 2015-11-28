<%@include file="/common/taglibs.jsp"%>
<h3><primo:label code="Manage Users"/></h3>
<div><form:form action="search.do" method="get"
	name="searchForm">
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b><primo:label code="Search User"/></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><label><primo:label code="Username"/></label></td>
			<td align="${left}">
			<select id="username" name="username" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${lists}" var="userinfo">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['username'] == userinfo.username}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${userinfo.username}" ${selected}>${userinfo.username}</option>
					</c:forEach>
			</select>
			</td>
			<%-- <td align="${left}"><input name="username" type="text"
				value="${sessionScope.searchCriteria.searchMap.username}"  style="min-width:149px; max-width:149px" /></td>
			 --%>
			<td align="${left}" class="first"><label><primo:label code="Short Name"/></label></td>		
			<td align="${left}">
			<select id="shortname" name="name" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${lists}" var="userinfo">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['name'] == userinfo.name}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${userinfo.name}" ${selected}>${userinfo.name}</option>
					</c:forEach>
			</select>
			</td>
			</tr>
			
			
			<tr>
			<td align="${left}" class="first"><label><primo:label code="First Name"/></label></td>
			<td align="${left}">
			<select id="firstname" name="firstName" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${lists}" var="userinfo">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['firstName'] == userinfo.firstName}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${userinfo.firstName}" ${selected}>${userinfo.firstName}</option>
					</c:forEach>
			</select>
			</td>
			<%-- <td align="${left}"><input name="username" type="text"
				value="${sessionScope.searchCriteria.searchMap.username}"  style="min-width:149px; max-width:149px" /></td>
			 --%>
			<td align="${left}" class="first"><label><primo:label code="Last Name"/></label></td>		
			<td align="${left}">
			<select id="lastname" name="lastName" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${lists}" var="userinfo">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['lastName'] == userinfo.lastName}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${userinfo.lastName}" ${selected}>${userinfo.lastName}</option>
					</c:forEach>
			</select>
			</td>
			</tr>
			
			
			<tr>
			<td align="${left}" class="first"><label><primo:label code="Role"/></label></td>
			<td align="${left}">
			<select id="role" name="role.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${roles}" var="urole">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['role.id'] == urole.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${urole.id}" ${selected}>${urole.name}</option>
					</c:forEach>
			</select>
			</td>
			<%-- <td align="${left}"><input name="username" type="text"
				value="${sessionScope.searchCriteria.searchMap.username}"  style="min-width:149px; max-width:149px" /></td>
			 --%>
			<td align="${left}" class="first"><label><primo:label code="Status"/></label></td>		
			<td align="${left}">
			<select id="status" name="status" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${statuses}" var="actstatus">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['status'] == actstatus.dataValue}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${actstatus.dataValue}" ${selected}>${actstatus.dataText}</option>
					</c:forEach>
			</select>
			</td>
			</tr>
			
			
			
			
		<tr>
			<td align="${left}" class="first"><label><primo:label code="Mobile No"/></label></td>
			<td align="${left}"><input name="phoneNumber" type="text"
				value="${sessionScope.searchCriteria.searchMap.phoneNumber}"  style="min-width:149px; max-width:149px"  /></td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();" value="<primo:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form></div>
<br/>

<div style="width:100%; margin:0px auto;"><form:form name="delete.do" id="customerForm">
		<primo:datatable urlContext="admin/access/user" deletable="true" editable="true" insertable="true" exportPdf="true" exportXls="true" exportCsv="true"
			baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
			<primo:textcolumn headerText="Username" dataField="username" />
			<primo:textcolumn headerText="First Name" dataField="firstName" />
			<primo:textcolumn headerText="Last Name" dataField="lastName" />
			<primo:textcolumn headerText="Short Name" dataField="name" />
			<primo:textcolumn headerText="Phone Number" dataField="phoneNumber"/>
			<primo:textcolumn headerText="Mobile No" dataField="mobileNo"/>
			<primo:textcolumn headerText="Email" dataField="email"/>
			<primo:textcolumn headerText="Role" dataField="role.name"/>
			<primo:anchorcolumn headerText="Reset Password"  linkUrl="/admin/access/user/resetuserpassword.do?id={id}" linkText="Reset Password"/>
			<primo:staticdatacolumn headerText="Status" dataField="status" dataType="STATUS"/>
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList")); %>
	</form:form>
</div>
<!-- headerText="Manage User Role" dataField="status" linkUrl="${ctx}/userrole/create.do?id={id}" linkText="Manage User Role" -->
