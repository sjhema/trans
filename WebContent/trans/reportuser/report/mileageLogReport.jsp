<%@include file="/common/taglibs.jsp"%>

<style>
 .ui-datepicker-calendar {
     display: none;
  }
</style>

<script type="text/javascript">
function processMileageLogTotalsReport() {
	document.forms[0].elements["reportType"].value = 'TOTALS';
	document.forms[0].submit();
}

function processMileageLogDetailsReport() {
	document.forms[0].elements["reportType"].value = 'DETAILS';
	document.forms[0].submit();
}

$(function() {
	$("#periodFrom").datepicker( {
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

$(function() {
	$("#periodTo").datepicker( {
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

$(document).ready(function(){
	   $("select").multiselect();
});
</script>
<br/>

<form:form action="search.do" name="searchForm" method="post" commandName="modelObject">
	<form:hidden path="reportType" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Mileage Report" /></b></td>
	    </tr>
	    <tr>
			<td class="form-left"><primo:label code="Period" /><span class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="15" path="periodFrom" cssClass="flat" /> 
				To:<form:input size="15" path="periodTo" cssClass="flat" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="States" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="state" multiple="true">
					<form:option value="-1">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${states}" itemValue="id" itemLabel="name" />
				</form:select>
			</td>
		</tr>
		 <tr>
		 	<td class="form-left"><primo:label code="Companies" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="company" multiple="true">
					<form:option value="-1">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select>
			</td>
			<td class="form-left"><primo:label code="Unit#" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="unit" multiple="true">
					<form:option value="-1">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${trucks}" itemValue="unit" itemLabel="unit" />
				</form:select>
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3">
				<input type="button"
					onclick="javascript:processMileageLogTotalsReport()" value="Totals" />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button"
					onclick="javascript:processMileageLogDetailsReport()" value="Details" />
			</td>
		</tr>
	</table>
</form:form>

