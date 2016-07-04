<%@include file="/common/taglibs.jsp"%>

<style>
 .ui-datepicker-calendar {
     display: none;
  }
</style>

<script type="text/javascript">
$(function() {
	$("#period").datepicker( {
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true,
        dateFormat: 'MM yy',
        onClose: function(dateText, inst) { 
        	var iMonth = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
            var iYear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
            $(this).datepicker('setDate', new Date(iYear, iMonth, 1));
        },
        beforeShow : function(input, inst) {
        	if ((selDate = $(this).val()).length > 0) {
               iYear = selDate.substring(selDate.length - 4, selDate.length);
               iMonth = jQuery.inArray(selDate.substring(0, selDate.length - 5), $(this).datepicker('option', 'monthNames'));
               $(this).datepicker('option', 'defaultDate', new Date(iYear, iMonth, 1));
               $(this).datepicker('setDate', new Date(iYear, iMonth, 1));
            }
        }
    });
});
</script>

<h3>
	<primo:label code="Add/Update Mileage" />
</h3>

<form:form action="save.do" name="modelForm" modelAttribute="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="5"><b>Mileage</b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Period" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="period" style="min-width:150px; max-width:150px" cssClass="flat"/> 
				<br><form:errors path="period" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="State" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="state" style="min-width:154px; max-width:154px">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${states}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="state" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Company" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="company" style="min-width:154px; max-width:154px">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="company" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Unit" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="unit" style="min-width:154px; max-width:154px">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${trucks}" itemValue="id" itemLabel="unit" />
				</form:select>
				<br> <form:errors path="unit" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="VIN" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="vin" style="min-width:150px; max-width:150px" cssClass="flat"/> 
				<br><form:errors path="vin" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Permit" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="vehiclePermitNumber" style="min-width:150px; max-width:150px" cssClass="flat"/> 
				<br><form:errors path="vehiclePermitNumber" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Miles" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="miles" style="min-width:150px; max-width:150px" cssClass="flat"/>
				<br><form:errors path="miles" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2"><input type="submit"
				name="create" id="create" onclick="" value="Save" class="flat" /> <input
				type="reset" id="resetBtn" value="Reset" class="flat" /> <input
				type="button" id="cancelBtn" value="Cancel" class="flat"
				onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>


