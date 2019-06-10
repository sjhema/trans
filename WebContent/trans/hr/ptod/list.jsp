<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage PTOD" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search PTOD" /></b></td>
		</tr>
		<%-- <tr>
			<td align="${left}" class="first"><primo:label code="Name"/></td>
			<td align="${left}"><select id="name" name="id">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${ptods}" var="name">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['id'] == name.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${name.id}" ${selected}>${name.name}</option>
				</c:forEach>
			</select>
			</td>
	    </tr> --%>
        <tr>
			<td align="${left}" class="first"><primo:label code="Company"/></td>
				<td align="${left}"><select id="company" name="company.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${companyLocation}" var="company">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['company.id'] == company.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${company.id}" ${selected}>${company.name}</option>
					</c:forEach>
			</select></td>
			<td align="${left}" class="first"><primo:label code="Terminal"/></td>
			<td align="${left}"><select id="terminal" name="terminal.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${terminals}" var="terminal">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['terminal.id'] == terminal.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${terminal.id}" ${selected}>${terminal.name}</option>
				</c:forEach>
			</select></td>
			
	   </tr>
	   <tr>
	      <%-- <td align="${left}" class="first"><primo:label code="Category" /></td>
			<td align="${left}"><select id="category" name="category">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${EmpCategary}" var="empCategory">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['category'] == empCategory.dataValue}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${empCategory.dataText}"${selected}>${empCategory.dataText}</option>
					</c:forEach>
			</select></td> --%>
			<td align="${left}" class="first"><primo:label code="Category"/></td>
			<td align="${left}"><select id="category" name="category.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${catagories}" var="category">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['category.id'] == category.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${category.id}" ${selected}>${category.name}</option>
				</c:forEach>
			</select></td><!-- leavetypes -->
			
			
			
			
			
			<td align="${left}" class="first"><primo:label code="Leave Type" /></td>
			<td align="${left}"><select id="leavetype" name="leavetype.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${leavetypes}" var="leavetype">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['leavetype.id'] == leavetype.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${leavetype.id}" ${selected}>${leavetype.name}</option>
				</c:forEach>
			</select></td>
			
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Status"/></td>
			<td align="${left}"><select id="status.id" name="status" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select" />------</option>
					   
	                                    <option value="1">Active</option>
                                       <option value="0">Inactive</option>
			                  </select>
                         </td>
			<%-- <td align="${left}"><select id="leavetype" name="leavetype">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${LeaveTypes}" var="leaveTypes">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['leavetype'] == leaveTypes.dataValue}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${leaveTypes.dataText}"${selected}>${leaveTypes.dataText}</option>
					</c:forEach>
			</select></td> --%>
	   </tr>
	   
	 
	  <tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();"
				value="<primo:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="delete.do" id="serviceForm">
	<primo:datatable urlContext="hr/ptod" deletable="true"
		editable="true" insertable="true" exportPdf="true" exportXls="true"
			exportCsv="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<%-- <primo:textcolumn headerText="Name" dataField="name" /> --%>
		<primo:textcolumn headerText="Company" dataField="company.name" />
		<primo:textcolumn headerText="Terminal" dataField="terminal.name" />
		<primo:textcolumn headerText="Category" dataField="category.name" />
		<primo:textcolumn headerText="Leave Type" dataField="leavetype.name" />
		<primo:textcolumn headerText="Experience From<br/>(in years)" dataField="experienceinyears"  />
		<primo:textcolumn headerText="Experience To<br/>(in years)" dataField="experienceinyearsTo"  />
		<primo:textcolumn headerText="Experience in days" dataField="experienceindays"  />
		<primo:staticdatacolumn headerText="Leave Qualifier" dataField="leaveQualifier" dataType="LeaveQualifier"/>
		<primo:textcolumn headerText="Days Earned" dataField="dayearned" />		
		<primo:textcolumn headerText="Hours Earned" dataField="hoursearned" />
		<primo:textcolumn headerText="PTOD Rate" dataField="rate" />
		<primo:datecolumn headerText="Effective Date" dataField="effectiveDate" dataFormat="MM-dd-yyyy"/>	
		<primo:datecolumn headerText="End Date" dataField="endDate" dataFormat="MM-dd-yyyy"/>
		<primo:textcolumn headerText="Calculate" dataField="calculateFlag" />
		<primo:textcolumn headerText="Annual accrual" dataField="annualoraccrual" />	
		<primo:staticdatacolumn headerText="Status" dataField="status" dataType="STATUS" />
		
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


