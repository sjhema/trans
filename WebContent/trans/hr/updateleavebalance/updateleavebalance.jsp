<%@include file="/common/taglibs.jsp"%>


<br/>



<h3>
	<primo:label code="Update Regular Sick/Personal Leave Balance" />
</h3>
<form:form action="${ctx}/hr/updateleavebalance/update.do" target="update" method="get" name="searchForm">
	<table width="80%" id="form-table" align="left">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Update Regular Sick/Personal Leave Balance" /></b></td>
		</tr>
		
        <tr>
			<td align="${left}" class="first"><primo:label code="Company"/></td>
				<td align="${left}"><select id="company" name="company.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${companyLocation}" var="company">
					<c:set var="selected" value=""/>
					
						<option value="${company.id}" ${selected}>${company.name}</option>
					</c:forEach>
			</select></td>
			<td align="${left}" class="first"><primo:label code="Terminal"/></td>
			<td align="${left}"><select id="terminal" name="terminal.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${terminals}" var="terminal">
					<c:set var="selected" value=""/>
					
						<option value="${terminal.id}" ${selected}>${terminal.name}</option>
				</c:forEach>
			</select></td>
			
	   </tr>
	   <tr>
	      
			<td align="${left}" class="first"><primo:label code="Category"/></td>
			<td align="${left}"><select id="category" name="category.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${catagories}" var="category">
					<c:set var="selected" value=""/>
				
						<option value="${category.id}" ${selected}>${category.name}</option>
				</c:forEach>
			</select></td>	
			
		</tr>   
	 
	  <tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();"
				value="<primo:label code="Run"/>" /></td>
		</tr>
	</table>
</form:form>







<%-- <table  width="100%" cellspacing="1" cellpadding="5">
<tr>
<td colspan="2"></td>
</tr>
<tr>
<td colspan="2"></td>
<td align="center"><a href="${ctx}/hr/updateleavebalance/update.do" target="update"><u>Update Current Leave Balance</u></a></td>
</tr>
</table> --%>
<table width="100%">
	<tr><td align="${left}" width="100%" valign="top"><iframe src="${ctx}/trans/blank.jsp" width="1000px" height="600" name="update" frameborder="0"></iframe></td></tr>
</table>

