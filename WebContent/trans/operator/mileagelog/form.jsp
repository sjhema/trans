<%@include file="/common/taglibs.jsp"%>

<style>
.hide-calendar .ui-datepicker-calendar {
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
			<td class="form-left"><primo:label code="First In State" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input size="10" path="firstInState" cssClass="flat" onblur="return formatDate('firstInState');"/>
				<script type="text/javascript">
					$(function() {
						$("#firstInState").datepicker({
							dateFormat:'mm-dd-yy',
			            	changeMonth: true,
			    			changeYear: true,
			    	        beforeShow : function(input, inst) {
			    	        	$('#ui-datepicker-div').removeClass('hide-calendar');
			    	        }
			    		});
					});
				</script>
				<br><form:errors path="firstInState" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Last In State" /><span class="errorMessage">*</span></td>
				<td align="${left}">
					<form:input size="10" path="lastInState" cssClass="flat" onblur="return formatDate('lastInState');"/>
					<script type="text/javascript">
						$(function() {
							$("#lastInState").datepicker({
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


