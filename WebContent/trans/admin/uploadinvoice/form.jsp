<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
</script>
<h3>
	<primo:label code="Manage Invoice" />
</h3>
<form:form action="save.do" name="modelForm"
	modelAttribute="modelObject" method="post" enctype="multipart/form-data" >
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="2"><b>Upload Invoice</b>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Invoice Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker" path="invoiceDate"
					cssClass="flat" onblur="return formatDate('datepicker');" size="22"/> <br> <form:errors
					path="invoiceDate" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left">Invoice Number<span class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="invoiceNumber" id="invoiceNumber" size="22"/> 
			<form:errors path="invoiceNumber" cssClass="errorMessage"/></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Transfer Station" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="transferStation">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${transferStations}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="transferStation" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Landfill" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="landfill">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${landfills}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="landfill" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Invoice File:<span class="errorMessage">*</span></td>
					<td>
						<input type="file" name="invoiceFile" id="invoiceFile" onchange="return validateInvoiceFile(this.id);" size="22"/>
					</td>
				</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2"><input type="submit"
				name="create" id="create" onclick="" value="Upload" class="flat" /> <input
				type="reset" id="resetBtn" value="Reset" class="flat" /> <input
				type="button" id="cancelBtn" value="Cancel" class="flat"
				onClick="location.href='list.do'" /></td>
		</tr>
	</table>
</form:form>