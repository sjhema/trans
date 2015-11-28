<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function formatPhone(){
	var phone = document.getElementById("phone").value;
	if(phone != ""){
		if(phone.length < 10){
			alert("Invalid Phone Number");
			document.getElementById("phone").value = "";
			return true;
		}
		else{
			var str = new String(phone);
			if(!str.match("-")){
				var p1 = str.substring(0,3);
				var p2 = str.substring(3,6);
				var p3 = str.substring(6,10);				
				var phone = p1 + "-" + p2 + "-" + p3;
				document.getElementById("phone").value = phone;
			}
		}
	}	
}

function formatFax(){
	var fax = document.getElementById("fax").value;
	if(fax != ""){
		if(fax.length < 10){
			alert("Invalid Phone Number");
			document.getElementById("fax").value = "";
			return true;
		}
		else{
			var str = new String(fax);
			if(!str.match("-")){
				var p1 = str.substring(0,3);
				var p2 = str.substring(3,6);
				var p3 = str.substring(6,10);				
				var fax = p1 + "-" + p2 + "-" + p3;
				document.getElementById("fax").value = fax;
			}
		}
	}	
}
</script>
<br/>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="4"><b><primo:label code="Add/Update Customers"/></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Name" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="name" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="name" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><primo:label code="Customer ID" /></td>
			<td align="${left}">
				<form:input path="customerNameID" cssClass="flat" style="min-width:200px; max-width:200px" />
			 	
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Address Line1"/><span	class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="address" cssClass="flat" style="min-width:200px; max-width:200px"/>
				 <br><form:errors path="address" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Address Line2"/></td>
			<td align="${left}">
				<form:input path="address2" cssClass="flat" style="min-width:200px; max-width:200px"/>
				 <br><form:errors path="address2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="City" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input cssClass="flat" path="city" style="min-width:200px; max-width:200px"/>
				<br><form:errors path="city" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Zipcode" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="zipcode" cssClass="flat" style="min-width:200px; max-width:200px" maxlength="5"
					 onkeypress="return onlyNumbers(event, false)"/>
			 	<br><form:errors path="zipcode" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="State" /><span	class="errorMessage"></span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="state" style="min-width:204px; max-width:204px">
					<form:option value="">-----------Please Select----------</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="state" cssClass="errorMessage" />
			</td>
		</tr>		
		<tr>
			<td class="form-left"><primo:label code="Phone" /></td>
			<td align="${left}">
				<form:input path="phone" cssClass="flat" style="min-width:200px; max-width:200px" maxlength="12" 
					id="phone" onkeypress="return onlyNumbers(event, false)" onblur="return formatPhone();"/>
			 	<br><form:errors path="phone" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Fax" /></td>
			<td align="${left}">
				<form:input path="fax" cssClass="flat"	style="min-width:200px; max-width:200px" maxlength="12" 
					id="fax" onkeypress="return onlyNumbers(event, false)" onblur="return formatFax();"/>
				 <br><form:errors path="fax" cssClass="errorMessage" />
			</td>
		</tr>
	   

		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2"><input type="submit"
				name="create" id="create" onclick=""
				value="<primo:label code="Save"/>" class="flat" /> <input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat" /> <input type="button" id="cancelBtn"
				value="<primo:label code="Cancel"/>" class="flat"
				onClick="location.href='list.do'" /></td>
		</tr>
	</table>
</form:form>
	
