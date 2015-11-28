<%@include file="/common/taglibs.jsp"%>
<script>
</script>
<div class="catbody">
 <form:form action="save.do" name="fuelsurchargeForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id"/>
    <table cellpadding="5" cellspacing="10" width="100%">
	    <tr>
			<td valign="top" class="productbox" width="90%">
				<table width="100%">
					<tr>
							<td colspan="4">
								<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
									<tr class="table-heading" >
							            <td colspan="2"><b><primo:label code="Add/Update Fuel Surcharge"/></b></td>
									</tr>
								</table>
							</td>
					</tr>
					<tr>
						<td class="form-left"><primo:label code="From Place"/><span
								class="errorMessage">*</span></td>
						<td>
							<form:select cssClass="flat" path="fromPlace">
								<form:option value="">------<primo:label code="Please Select"/>------</form:option>
								<form:options items="${locations}" itemValue="id" itemLabel="name"/>
							</form:select>
							<br><form:errors path="fromPlace" cssClass="errorMessage"/>
						</td>
					</tr>
					<tr>
						<td class="form-left"><primo:label code="To Place"/><span
								class="errorMessage">*</span></td>
						<td>
							<form:select cssClass="flat" path="toPlace">
								<form:option value="">------<primo:label code="Please Select"/>------</form:option>
								<form:options items="${locations}" itemValue="id" itemLabel="name"/>
							</form:select>
							<br><form:errors path="toPlace" cssClass="errorMessage"/>
						</td>
					</tr>
					<tr>
						<td class="form-left"><primo:label code="Rate"/><span
							class="errorMessage">*</span></td>
						<td align="${left}"><form:input path="rate" cssClass="flat" onkeypress="return onlyNumbers(event, true)" />
						<br><form:errors path="rate" cssClass="errorMessage"/></td>
					</tr>
					<tr>
						<td class="form-left"><primo:label code="Distance"/><span
							class="errorMessage">*</span></td>
						<td align="${left}"><form:input path="distance" cssClass="flat" onkeypress="return onlyNumbers(event, true)" />
						<br><form:errors path="distance" cssClass="errorMessage"/></td>
					</tr>
					<tr>
						<td align="${left}" colspan="2"><input type="submit" name="create" id="create" onclick=""
							value="<primo:label code="Save"/>" class="flat" /> <input
							type="reset" id="resetBtn" value="<primo:label code="Reset"/>" class="flat"/>
							<input type="button" id="cancelBtn" value="<primo:label code="Cancel"/>" class="flat" onClick="location.href='list.do'"/>
						</td>
					</tr>                 
	     		</table>
     		</td>
		</tr>                 
     </table>
 </form:form>
 </div>
             
