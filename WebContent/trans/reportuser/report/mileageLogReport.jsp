<%@include file="/common/taglibs.jsp"%>

<style>
.hide-calendar .ui-datepicker-calendar {
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

function processIFTAReport() {
	document.forms[0].elements["reportType"].value = 'IFTA';
	document.forms[0].action='iftaSearch.do'
	document.forms[0].submit();
}

function processMPGReport() {
	document.forms[0].elements["reportType"].value = 'MPG';
	document.forms[0].action='mpgSearch.do'
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
        	$('#ui-datepicker-div').addClass('hide-calendar');
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
        	$('#ui-datepicker-div').addClass('hide-calendar');
        	if ((selDate = $(this).val()).length > 0) {
               iYear = selDate.substring(selDate.length - 4, selDate.length);
               iMonth = jQuery.inArray(selDate.substring(0, selDate.length - 5), $(this).datepicker('option', 'monthNames'));
               $(this).datepicker('option', 'defaultDate', new Date(iYear, iMonth, 1));
               $(this).datepicker('setDate', new Date(iYear, iMonth, 1));
            }
        }
    });
});

function formatDate(datepickerId){
	var date=document.getElementById(datepickerId).value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById(datepickerId).value="";
		return true;
	}
	else{
		var str=new String(date);
		if(!str.match("-")){
			var mm=str.substring(0,2);
			var dd=str.substring(2,4);
			var yy=str.substring(4,8);
			var enddigit=str.substring(6,8);
			if(!enddigit==00 && enddigit%4==0 ){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("Invalid date format");
						document.getElementById(datepickerId).value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("Invalid date format");
					document.getElementById(datepickerId).value="";
					return true;
				}
				else if(dd>31){
					alert("Invalid date format");
					document.getElementById(datepickerId).value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("Invalid date format");
						document.getElementById(datepickerId).value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("Invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}else if(dd>31){
					alert("Invalid date format");
					document.getElementById(datepickerId).value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("Invalid date format");
						document.getElementById(datepickerId).value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("Invalid date format");
					document.getElementById(datepickerId).value="";
					return true;
				}else if(dd>31){
					alert("Invalid date format");
					document.getElementById(datepickerId).value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById(datepickerId).value=date;
		}
	 }
   }
}

$(document).ready(function() {
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
	 	<td class="form-left"><primo:label code="Date" /><span class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="firstInStateFrom" cssClass="flat" onblur="return formatDate('firstInStateFrom');"/>
				<script type="text/javascript">
					$(function() {
						$("#firstInStateFrom").datepicker({
							dateFormat:'mm-dd-yy',
			            	changeMonth: true,
			    			changeYear: true,
			    	        beforeShow : function(input, inst) {
			    	        	$('#ui-datepicker-div').removeClass('hide-calendar');
			    	        }
			    		});
					});
				</script>
				To:<form:input size="10" path="firstInStateTo" cssClass="flat" onblur="return formatDate('firstInStateTo');"/>
				<script type="text/javascript">
					$(function() {
						$("#firstInStateTo").datepicker({
							dateFormat:'mm-dd-yy',
			            	changeMonth: true,
			    			changeYear: true,
			    	        beforeShow : function(input, inst) {
			    	        	$('#ui-datepicker-div').removeClass('hide-calendar');
			    	        }
			    		});
					});
				</script>
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
					onclick="javascript:processMileageLogDetailsReport()" value="HUT" />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button"
					onclick="javascript:processIFTAReport()" value="IFTA" />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button"
					onclick="javascript:processMPGReport()" value="MPG" />
			</td>
		</tr>
	</table>
</form:form>

