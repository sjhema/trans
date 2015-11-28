<%@include file="/common/taglibs.jsp"%>
<!-- <h1>Vehicle Permit Expiration Alert</h1> -->
<table border="0" style="margin-bottom:0px; margin-top: 0px">
<tr>
<td><h3><a href="${ctx}/admin/vehiclepermitalert/list.do" >Expiring in Next 30 Days
</a></h3></td>
<td>&nbsp;</td>
<td><h3><a href="${ctx}/admin/vehiclepermitalert/sixtydayslist.do" >Expiring in Next 60 Days
</a></h3></td>
<td>&nbsp;</td>
<td><h3><a href="${ctx}/admin/vehiclepermitalert/ninetydayslist.do" >Expiring in Next 90 Days
</a></h3></td>
</tr>
</table>
<h3 style="margin-top:0px;">${recordCount} Vehicle Permit(s) have been Expired</h3>
<div style="width:100%; margin:0px auto;"><form:form name="delete.do" id="customerForm">
		<primo:datatable urlContext="" deletable="false" editable="false" insertable="false" exportPdf="false" exportXls="false" exportCsv="false"
			baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
			<primo:textcolumn headerText="Vehicle#" dataField="vehicle.unitNum" />
		<primo:textcolumn headerText="Company" dataField="companyLocation.name" />
		<primo:textcolumn headerText="PERMIT TYPE" dataField="permitType.dataLabel" />
		<primo:textcolumn headerText="Permit Number" dataField="permitNumber" />
		<primo:textcolumn headerText="Effective Date" dataField="issueDate" dataFormat="MM-dd-yyyy"/>
		<primo:textcolumn headerText="Expiration Date" dataField="expirationDate" dataFormat="MM-dd-yyyy"/>
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList")); %>
	</form:form>
</div>