<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
var submitting = false;
function submitForm(){
	if(submitting) {
    	alert('Please wait a moment...');
    } else{
         submitting = true;
         document.forms["vehicleSaleForm"].submit();
	}
}

function populateBuyerDetails() {
	$("#buyerName").val("");
	$("#buyerAddress1").val("");
	$("#buyerAddress2").val("");
	$("#buyerCity").val("");
	$("#buyerState").val("");
	$("#buyerZipcode").val("");
	$("#buyerPhone").val("");
	$("#buyerFax").val("");
		
	var id = $("#buyer").val();
	if (id == '') {
		return;
	}
	
	$.ajax({
  		url: "ajax.do?action=retrieveBuyer" + "&id=" + id,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var buyer = jQuery.parseJSON(responseData);
       		$("#buyerName").val(buyer.name);
       		$("#buyerAddress1").val(buyer.address1);
       		$("#buyerAddress2").val(buyer.address2);
       		$("#buyerCity").val(buyer.city);
       		$("#buyerState").val(buyer.state.id);
       		$("#buyerZipcode").val(buyer.zipcode);
       		$("#buyerPhone").val(buyer.phone);
       		$("#buyerFax").val(buyer.fax);
		}
	});
}

function populateVehicleDetails() {
	$("#company").val("");
	$("#vin").val("");
	$("#year").val("");
	$("#make").val("");
	$("#model").val("");
		
	var id = $("#vehicle").val();
	if (id == '') {
		return;
	}
	
	$.ajax({
  		url: "ajax.do?action=retrieveVehicle" + "&id=" + id,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var vehicle = jQuery.parseJSON(responseData);
       		$("#company").val(vehicle.owner.name);
       		$("#vin").val(vehicle.vinNumber);
       		$("#year").val(vehicle.year);
       		$("#make").val(vehicle.make);
       		$("#model").val(vehicle.model);
		}
	});
}
</script>
<h3>
	<primo:label code="Add/Update Vehicle Sale" />
</h3>
<form:form action="save.do" name="vehicleSaleForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="6"><b><primo:label code="Vehicle Sale" /></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Vehicle" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="vehicle" id="vehicle" style="min-width:166px; max-width:166px"
					onChange="return populateVehicleDetails();">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${vehicles}" itemValue="id" itemLabel="unit" />
				</form:select> 
				<br> <form:errors path="vehicle" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Company" /></td>
			<td align="${left}">
				<input id="company" value="${modelObject.vehicle.owner.name}" style="background-color: #eee; min-width:162px; max-width:162px" 
					readonly class="flat"  />
			</td>
			<td class="form-left"><primo:label code="VIN" /></td>
			<td align="${left}">
				<input id="vin" value="${modelObject.vehicle.vinNumber}" style="background-color: #eee; min-width:162px; max-width:162px" class="flat" readonly/>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Year" /></td>
			<td align="${left}">
				<input id="year" value="${modelObject.vehicle.year}" style="background-color: #eee; min-width:162px; max-width:162px" class="flat" readonly/>
			</td>
			<td class="form-left"><primo:label code="Make" /></td>
			<td align="${left}">
				<input id="make" value="${modelObject.vehicle.make}" style="background-color: #eee; min-width:162px; max-width:162px" class="flat" readonly/>
			</td>
			<td class="form-left"><primo:label code="Model" /></td>
			<td align="${left}">
				<input id="model" value="${modelObject.vehicle.model}" style="background-color: #eee; min-width:162px; max-width:162px" class="flat" readonly/>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Buyer" /><span class="errorMessage">*</span></td>
			<td>
				<!--
				<form:select cssClass="flat" path="buyer" id="buyer" style="min-width:166px; max-width:166px"
					onChange="return populateBuyerDetails();">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${buyers}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br> <form:errors path="buyer" cssClass="errorMessage" />
				-->
				<form:input path="buyerName" style="min-width:210px; max-width:210px" cssClass="flat" />
				<br><form:errors path="buyerName" cssClass="errorMessage" />
			</td>
		</tr>
		<!-- 
		<tr>
			<td class="form-left"><primo:label code="Buyer Name" /></td>
			<td align="${left}">
				<input id="buyerName" value="${modelObject.buyer.name}" style="background-color: #eee; min-width:162px; max-width:162px" 
					readonly class="flat"  />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Address1" /></td>
			<td align="${left}">
				<input id="buyerAddress1" value="${modelObject.buyer.address1}" style="background-color: #eee; min-width:162px; max-width:162px" class="flat" readonly/>
			</td>
			<td class="form-left"><primo:label code="Address2" /></td>
			<td align="${left}">
				<input id="buyerAddress2" value="${modelObject.buyer.address2}" style="background-color: #eee; min-width:162px; max-width:162px" class="flat" readonly/>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="City" /></td>
			<td align="${left}">
				<input id="buyerCity" value="${modelObject.buyer.city}" style="background-color: #eee; min-width:162px; max-width:162px" class="flat" readonly/>
			</td>
			<td class="form-left"><primo:label code="State" /></td>
			<td align="${left}">
				<select id="buyerState" name="buyerState" style="background-color: #eee; min-width:164px; max-width:164px" 
					disabled="true">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${states}" var="aState">
						<c:set var="selected" value=""/>
						<c:if test="${modelObject.buyer.state.id == aState.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aState.id}" ${selected}>${aState.name}</option>
					</c:forEach>
				</select>
			</td>
			<td class="form-left"><primo:label code="Zipcode" /></td>
			<td align="${left}">
				<input id="buyerZipcode" value="${modelObject.buyer.zipcode}" style="background-color: #eee; min-width:162px; max-width:162px" class="flat" readonly/>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Phone" /></td>
			<td align="${left}">
				<input id="buyerPhone" value="${modelObject.buyer.phone}" style="background-color: #eee; min-width:162px; max-width:162px" class="flat" readonly/>
			</td>
			<td class="form-left"><primo:label code="Fax" /></td>
			<td align="${left}">
				<input id="buyerFax" value="${modelObject.buyer.fax}" style="background-color: #eee; min-width:162px; max-width:162px" class="flat" readonly/>
			</td>
		</tr>
		 -->
		<tr>
			<td class="form-left"><primo:label code="Sold Date" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input id="datepicker" path="saleDate" style="min-width:158px; max-width:158px" cssClass="flat"/>
				<br> 
				<form:errors path="saleDate" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Sale Price" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="salePrice" style="min-width:162px; max-width:162px" cssClass="flat" 
					maxlength="9" onkeypress="return onlyNumbers(event, true)"/>
				<br> <form:errors path="salePrice" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="button" name="create" id="create" onclick="javascript:submitForm();"
					value="<primo:label code="Save"/>" class="flat" />
				&nbsp;
				<input type="reset" id="resetBtn"
					value="<primo:label code="Reset"/>" class="flat" />
				&nbsp;
				<input type="button" id="cancelBtn" value="<primo:label code="Cancel"/>" class="flat"
					onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>

