<%@include file="/common/taglibs.jsp"%>

<h3>
	<primo:label code="Add/Update Load Miles" />
</h3>

<form:form action="save.do" name="modelForm" modelAttribute="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="5"><b>Load Miles</b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Company" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="company" id="company" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="company" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Origin" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="origin" id="origin" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${origins}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="origin" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Origin State" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="originState" id="originState" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${states}" itemValue="id" itemLabel="code" />
				</form:select> <br> <form:errors path="originState" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Destination" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="destination" id="destination" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${destinations}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="destination" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Destination State" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="destnState" id="destnState" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${states}" itemValue="id" itemLabel="code" />
				</form:select> <br> <form:errors path="destnState" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Total Miles" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="miles" style="min-width:150px; max-width:150px" cssClass="flat"
					 maxlength="9" onkeypress="return onlyNumbers(event, true)"/>
				<br><form:errors path="miles" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="NY Miles" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="nyMiles" style="min-width:150px; max-width:150px" cssClass="flat"
					 maxlength="9" onkeypress="return onlyNumbers(event, true)"/>
				<br><form:errors path="nyMiles" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="NJ Miles" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="njMiles" style="min-width:150px; max-width:150px" cssClass="flat"
					 maxlength="9" onkeypress="return onlyNumbers(event, true)"/>
				<br><form:errors path="njMiles" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="PA Miles" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="paMiles" style="min-width:150px; max-width:150px" cssClass="flat"
					 maxlength="9" onkeypress="return onlyNumbers(event, true)"/>
				<br><form:errors path="paMiles" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="MD Miles" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="mdMiles" style="min-width:150px; max-width:150px" cssClass="flat"
					 maxlength="9" onkeypress="return onlyNumbers(event, true)"/>
				<br><form:errors path="mdMiles" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="VA Miles" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="vaMiles" style="min-width:150px; max-width:150px" cssClass="flat"
					 maxlength="9" onkeypress="return onlyNumbers(event, true)"/>
				<br><form:errors path="vaMiles" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="DE Miles" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="deMiles" style="min-width:150px; max-width:150px" cssClass="flat"
					 maxlength="9" onkeypress="return onlyNumbers(event, true)"/>
				<br><form:errors path="deMiles" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="IL Miles" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="ilMiles" style="min-width:150px; max-width:150px" cssClass="flat"
					 maxlength="9" onkeypress="return onlyNumbers(event, true)"/>
				<br><form:errors path="ilMiles" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="FL Miles" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="flMiles" style="min-width:150px; max-width:150px" cssClass="flat"
					 maxlength="9" onkeypress="return onlyNumbers(event, true)"/>
				<br><form:errors path="flMiles" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="CT Miles" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="ctMiles" style="min-width:150px; max-width:150px" cssClass="flat"
					 maxlength="9" onkeypress="return onlyNumbers(event, true)"/>
				<br><form:errors path="ctMiles" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="WV Miles" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="wvMiles" style="min-width:150px; max-width:150px" cssClass="flat"
					 maxlength="9" onkeypress="return onlyNumbers(event, true)"/>
				<br><form:errors path="wvMiles" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Wash DC Miles" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="dcMiles" style="min-width:150px; max-width:150px" cssClass="flat"
					 maxlength="9" onkeypress="return onlyNumbers(event, true)"/>
				<br><form:errors path="dcMiles" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit" name="create" id="create" onclick="" value="Save" class="flat" /> 
				<input type="reset" id="resetBtn" value="Reset" class="flat" />
				<input type="button" id="cancelBtn" value="Cancel" class="flat" onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>


