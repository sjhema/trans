<%@include file="/common/taglibs.jsp"%>

<style>
 .ui-datepicker-calendar {
     display: none;
  }
</style>

<script>
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

$(function() {
	$("#deleteByPeriodDialogPeriod").datepicker( {
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

function deleteByPeriod() {
	var deleteByPeriodDialogErrorMessageElem = $('#deleteByPeriodDialogErrorMessage');
	
	var deleteByPeriod = $('#deleteByPeriodDialogPeriod').val();
	if (deleteByPeriod == "") {
		deleteByPeriodDialogErrorMessageElem.html("Please enter period");
		return;
	}
	
	clearDeleteByPeriodDialogMsgs();
	
	if (!confirm("Do you want to delete all mileage log for " + deleteByPeriod + "?")) {
		return;
	}
	
	$.ajax({
  		url: "${ctx}/operator/mileagelog/ajax.do?action=deleteByPeriod&period=" + deleteByPeriod,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData.indexOf("success") == -1) {
       			deleteByPeriodDialogErrorMessageElem.html(responseData);
       		} else {
       			alert("All mileage log for " + deleteByPeriod + " deleted successfully");
       			
       			closeDeleteByPeriodDialog();
       			document.location.href='list.do';
       		}
		}
	});
}

function clearDeleteByPeriodDialog() {
	$('#deleteByPeriodDialogPeriod').val("");
	
	clearDeleteByPeriodDialogMsgs();
}

function clearDeleteByPeriodDialogMsgs() {
	$('#deleteByPeriodDialogErrorMessage').html("");
	$('#deleteByPeriodDialogSuccessMessage').html("");
}

function openDeleteByPeriodDialog() {
	clearDeleteByPeriodDialog();
	$('#deleteByPeriodDialog').dialog({ 
		 modal: true,
         bgiframe: true,
         width: 500,
         resizable: false
     });
	
	$('#deleteByPeriodDialogDelete').focus();
}

function closeDeleteByPeriodDialog() {
	clearDeleteByPeriodDialog();
	$('#deleteByPeriodDialog').dialog('close');
}
</script>

<h3>
	<primo:label code="Manage Mileage" />
</h3>
<form:form action="search.do" method="post" name="searchForm">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b>Search Mileage</b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Period" /><span class="errorMessage"></span></td>
			<td  align="${left}">
				From:
				<input type="text" size="15" id="periodFrom" name="periodFrom" class="flat"
					value="${sessionScope.searchCriteria.searchMap['periodFrom']}" /> 
				To:
			    <input type="text" size="15" id="periodTo" name="periodTo" class="flat"
			    	value="${sessionScope.searchCriteria.searchMap['periodTo']}"/> 	
			</td>
			<td class="form-left"><primo:label code="State" /><span class="errorMessage"></span></td>
			<td>
				<select id="state" name="state.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${states}" var="aState">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['state.id'] == aState.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
							<option value="${aState.id}" ${selected}>${aState.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Company"/></td>
			<td align="${left}">
				<select id="company" name="company.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${companies}" var="company">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['company.id'] == company.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
							<option value="${company.id}" ${selected}>${company.name}</option>
					</c:forEach>
				</select>
			</td>	
			<td align="${left}" class="first"><primo:label code="Unit#"/></td>
			<td align="${left}">
				<select id="unit" name="unit.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${trucks}" var="anUnit">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['unit.id'] == anUnit.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
							<option value="${anUnit.unit}" ${selected}>${anUnit.unit}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button" onclick="javascript:document.forms['searchForm'].submit();"
					value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<table width="100%">
	<tr>
		<td align="left" colspan="3">		
			<img src="/trans/images/edit.png" border="0" title="Delete By Period" class="toolbarButton"> 
			<a href="javascript:;" onclick="openDeleteByPeriodDialog()">Delete By Period</a>
		</td>
	</tr>
</table>
<br/>
<div style="width: 100%; margin: 0px auto;">
	<form:form action="delete.do" id="deleteForm" name="deleteForm">
		<primo:datatable urlContext="operator/mileagelog" deletable="true"
			editable="true" insertable="true" baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="true" searcheable="false"
			exportPdf="true" exportXls="true" exportCsv="true">
				<primo:textcolumn headerText="Period" dataField="period" dataFormat="MM-yyyy" />
				<primo:textcolumn headerText="Company" dataField="company.name"/>
				<primo:textcolumn headerText="State" dataField="state.name" />
				<primo:textcolumn headerText="Unit#" dataField="unitNum"/>
				<primo:textcolumn headerText="VIN" dataField="vin"/>
				<primo:textcolumn headerText="Permit" dataField="vehiclePermitNumber"/>
				<primo:textcolumn headerText="Miles" dataField="miles"/>
				<primo:textcolumn headerText="First In State" dataField="firstInState" dataFormat="MM-dd-yyyy HH:mm:ss"/>
				<primo:textcolumn headerText="Last In State" dataField="lastInState" dataFormat="MM-dd-yyyy HH:mm:ss"/>
				<primo:textcolumn headerText="Groups" dataField="groups"/>
				<primo:textcolumn headerText="Source" dataField="source"/>
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
	</form:form>
</div>

<div id="deleteByPeriodDialog" title="Delete By Period" style="display:none;">
	<div id="deleteByPeriodDialogBody">
		<div id="deleteByPeriodDialogMessage">
     		<div id="deleteByPeriodDialogErrorMessage" style="color:red; font-size:14px; vertical-align:center;"></div>
     		<div id="deleteByPeriodDialogSuccessMessage" style="color:green; font-size:14px; vertical-align:center;"></div>
     	</div>
		<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
			<tr>
				<td class="form-left"><primo:label code="Period" /><span class="errorMessage">*</span></td>
				<td align="${left}">
					<input type="text" size="15" id="deleteByPeriodDialogPeriod" name="deleteByPeriodDialogPeriod" class="flat" /> 
				</td>
			</tr>
			<tr><td colspan="2"></td></tr>
			<tr>
				<td>&nbsp;</td>
				<td align="${left}" colspan="2">
					<input type="button" id="deleteByPeriodDialogDelete" onclick="javascript:deleteByPeriod();"
						value="<primo:label code="Delete By Period"/>" class="flat" />
					<input type="button" id="deleteByPeriodDialogCancel" value="<primo:label code="Cancel"/>" class="flat"
						onClick="javascript:closeDeleteByPeriodDialog();" />
				</td>
			</tr>
		</table>
	</div>
</div>