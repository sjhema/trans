<%@include file="/common/taglibs.jsp"%>
<h3>
Manage Setup Data
</h3>
<div id="search"><form:form action="search.do" method="get"
	name="searchForm">
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b><primo:label code="Search Setup Data"/></b></td>
		</tr>
		<TR>
		
		  <td align="${left}" class="first"><primo:label code="Data Type"/></td>

			<td align="${left}"><select id="dataType" name="dataType" style="min-width: 175px; max-width: 175px;">
					<option value="">- - - - Select - - - -</option>
					<c:forEach items="${dataTypes}" var="datatype">
						<option value="${datatype}">${datatype }</option>
					</c:forEach>
			</select>
               </td>
			
			<td align="${left}" class="first"><label><primo:label code="Data Text"/></label></td>
			<td align="${left}"><input name="dataLabel" type="text"	value="${sessionScope.searchCriteria.searchMap.dataLabel}" style="min-width: 175px; max-width: 175px;"/>
			</td>
		
		</TR>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();" value="<primo:label code="Search"/>"/></td>
		</tr>
	</table>
</form:form></div>
<br/>
<div style="width="100%"><form:form name="delete.do" id="serviceForm">
		<primo:datatable urlContext="hr/setupdata" deletable="true" editable="true" insertable="true"
			baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
			<primo:textcolumn headerText="Data Type" dataField="dataType" />
			<primo:textcolumn headerText="Data Text" dataField="dataLabel" />
			<primo:textcolumn headerText="Data Value" dataField="dataValue" />
		</primo:datatable>
	</form:form>
</div>