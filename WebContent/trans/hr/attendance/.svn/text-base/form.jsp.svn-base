<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function getComTermCatagory(){
	var emp=document.getElementById("employeeId");
	var driver=emp.options[emp.selectedIndex].value;
	
	jQuery.ajax({
		url:'${ctx}/hr/attendance/ajax.do?action=findDCompany&driver='+driver, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options;
			
			if(driver==""){
				options = '<option value="">Please Select</option>';
			}
			for (var i = 0; i <listData.length; i++) {
				var company=listData[i];
				options += '<option value="'+company.id+'">'+company.name+'</option>';
				}
			$("#company").html(options);
		}
			
		});
         
	jQuery.ajax({
		url:'${ctx}/hr/attendance/ajax.do?action=findDTerminal&driver='+driver, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options;
			
			if(driver==""){
				options = '<option value="">Please Select</option>';
			}
			for (var i = 0; i <listData.length; i++) {
				var terminal=listData[i];
				options += '<option value="'+terminal.id+'">'+terminal.name+'</option>';
				}
			$("#terminalId").html(options);
		}
			
		});
	
	jQuery.ajax({
		url:'${ctx}/hr/attendance/ajax.do?action=findDCategory&driver='+driver, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options;
			
			if(driver==""){
				options = '<option value="">Please Select</option>';
			}
			for (var i = 0; i <listData.length; i++) {
				var category=listData[i];
				options += '<option value="'+category.id+'">'+category.name+'</option>';
				}
			$("#categoryId").html(options);
		}
			
		});
	  }

function formatDate(){
	var date=document.getElementById("datepicker").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker").value="";
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
						alert("invalid date format");
						document.getElementById("datepicker").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker").value=date;
		}
	 }
   }
}
function formatDate1(){
	var date=document.getElementById("datepicker1").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker1").value="";
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
						alert("invalid date format");
						document.getElementById("datepicker1").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker1").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker1").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker1").value=date;
		}
	 }
   }
}
</script>
<h3><primo:label code="Add/Update Attendance" /></h3>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Manage Attendance" /></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker" path="attendancedate"
					cssClass="flat" onblur="return formatDate();"/> <br> <form:errors
					path="attendancedate" cssClass="errorMessage" /></td>
			<td class="form-left"><primo:label code="Shift" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="shift">
					<form:option value="">------Please Select------</form:option>
					<form:options items="${shiftnames}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="shift" cssClass="errorMessage" /></td>
		</tr>
		<tr>
            <td class="form-left"><primo:label code="Employee" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat" id="employeeId" path="driver" onchange="javascript:getComTermCatagory()">
					<form:option value="">------Please Select------</form:option>
					<form:options items="${employees}" itemValue="id" itemLabel="FullName" />
				</form:select> 
				<br><form:errors path="driver" cssClass="errorMessage" /></td>
			<td class="form-left"><primo:label code="Category" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
			   <select name="catagory" id="categoryId"   style="width:160px">
					<option value="">------Please Select------</option>
					<c:forEach var="item" items="${catagories}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
				<br><form:errors path="catagory" cssClass="errorMessage" /></td>
		</tr>
		<tr>
		 <td class="form-left"><primo:label code="Company" /><span	class="errorMessage">*</span></td>
			<td align="${left}">				
				<select name="company" id="company"   style="width:160px">
					<option value="">------Please Select------</option>
					<c:forEach var="item" items="${companies}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
				<br><form:errors path="company" cssClass="errorMessage" />
			</td>
			 <td class="form-left"><primo:label code="Terminal" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<select name="terminal" id="terminalId"   style="width:160px">
					<option value="">------Please Select------</option>
					<c:forEach var="item1" items="${terminals}">
						<option value="${item1.id}">${item1.name}</option>
					</c:forEach>
				</select>
			   <br><form:errors path="terminal" cssClass="errorMessage" />
			</td>
	    </tr>
		<tr>
			<td class="form-left"><primo:label code="Sign In Time" /><span class="errorMessage">*</span></td>
			<td align="${left}">
			 <form:select cssClass="flat" path="timestarthours">
			                 <form:option value="">--Please Select--</form:option>
                            <form:option value="00">12:00 am</form:option>
							<form:option value="01">1:00 am</form:option>
							<form:option value="02">2:00 am</form:option>
							<form:option value="03">3:00 am</form:option>
							<form:option value="04">4:00 am</form:option>
							<form:option value="05">5:00 am</form:option>
							<form:option value="06">6:00 am</form:option>
							<form:option value="07">7:00 am</form:option>
							<form:option value="08">8:00 am</form:option>
							<form:option value="09">9:00 am</form:option>
							<form:option value="10">10:00 am</form:option>
							<form:option value="11">11:00 am</form:option>
							<form:option value="12">12:00 pm</form:option>
							<form:option value="13">1:00 pm</form:option>
							<form:option value="14">2:00 pm</form:option>
							<form:option value="15">3:00 pm</form:option>
							<form:option value="16">4:00 pm</form:option>
							<form:option value="17">5:00 pm</form:option>
							<form:option value="18">6:00 pm</form:option>
							<form:option value="19">7:00 pm</form:option>
							<form:option value="20">8:00 pm</form:option>
							<form:option value="21">9:00 pm</form:option>
							<form:option value="22">10:00 pm</form:option>
							<form:option value="23">11:00 pm</form:option>
                </form:select>Hour
                  <br> <form:errors path="timestarthours" cssClass="errorMessage" />
                </td>
                <td align="${left}">
			     <form:select cssClass="flat" path="timestartminuts">
			                 <!-- <option value="">--Please Select--</option> -->
                            <form:option value="00">00</form:option>
							<%-- <form:option value="01">01</form:option>
							<form:option value="02">02</form:option>
							<form:option value="03">03</form:option>
							<form:option value="04">04</form:option> --%>
							<form:option value="05">05</form:option>
							<%-- <form:option value="06">06</form:option>
							<form:option value="07">07</form:option>
							<form:option value="08">08</form:option>
							<form:option value="09">09</form:option> --%>
							<form:option value="10">10</form:option>
							<%-- <form:option value="11">11</form:option>
							<form:option value="12">12</form:option>
							<form:option value="13">13</form:option>
							<form:option value="14">14</form:option> --%>
							<form:option value="15">15</form:option>
							<%-- <form:option value="16">16</form:option>
							<form:option value="17">17</form:option>
							<form:option value="18">18</form:option>
							<form:option value="19">19</form:option> --%>
							<form:option value="20">20</form:option>
							<%-- <form:option value="21">21</form:option>
							<form:option value="22">22</form:option>
							<form:option value="23">23</form:option>
							<form:option value="24">24</form:option> --%>
							<form:option value="25">25</form:option>
							<%-- <form:option value="26">26</form:option>
							<form:option value="27">27</form:option>
							<form:option value="28">28</form:option>
							<form:option value="29">29</form:option> --%>
							<form:option value="30">30</form:option>
							<%-- <form:option value="31">31</form:option>
							<form:option value="32">32</form:option>
							<form:option value="33">33</form:option>
							<form:option value="34">34</form:option> --%>
							<form:option value="35">35</form:option>
							<%-- <form:option value="36">36</form:option>
							<form:option value="37">37</form:option>
							<form:option value="38">38</form:option>
							<form:option value="39">39</form:option> --%>
							<form:option value="40">40</form:option>
							<%-- <form:option value="41">41</form:option>
							<form:option value="42">42</form:option>
							<form:option value="43">43</form:option>
							<form:option value="44">44</form:option> --%>
							<form:option value="45">45</form:option>
							<%-- <form:option value="46">46</form:option>
							<form:option value="47">47</form:option>
							<form:option value="48">48</form:option>
							<form:option value="49">49</form:option> --%>
							<form:option value="50">50</form:option>
							<%-- <form:option value="51">51</form:option>
							<form:option value="52">52</form:option>
							<form:option value="53">53</form:option>
							<form:option value="54">54</form:option> --%>
							<form:option value="55">55</form:option>
							<%-- <form:option value="56">56</form:option>
							<form:option value="57">57</form:option>
							<form:option value="58">58</form:option> --%>
							<form:option value="59">59</form:option>
						
               </form:select>Minutes
                  <br> <form:errors path="timestartminuts" cssClass="errorMessage" /> </td>
             </tr> 
		     <tr>
		    <td class="form-left"><primo:label code="Sign Out Time" /><span class="errorMessage">*</span></td>
			<td align="${left}">
			  <form:select cssClass="flat" path="timeendinhours">
			                <form:option value="">--Please Select--</form:option>
                            <form:option value="00">12:00 am</form:option>
							<form:option value="01">1:00 am</form:option>
							<form:option value="02">2:00 am</form:option>
							<form:option value="03">3:00 am</form:option>
							<form:option value="04">4:00 am</form:option>
							<form:option value="05">5:00 am</form:option>
							<form:option value="06">6:00 am</form:option>
							<form:option value="07">7:00 am</form:option>
							<form:option value="08">8:00 am</form:option>
							<form:option value="09">9:00 am</form:option>
							<form:option value="10">10:00 am</form:option>
							<form:option value="11">11:00 am</form:option>
							<form:option value="12">12:00 pm</form:option>
							<form:option value="13">1:00 pm</form:option>
							<form:option value="14">2:00 pm</form:option>
							<form:option value="15">3:00 pm</form:option>
							<form:option value="16">4:00 pm</form:option>
							<form:option value="17">5:00 pm</form:option>
							<form:option value="18">6:00 pm</form:option>
							<form:option value="19">7:00 pm</form:option>
							<form:option value="20">8:00 pm</form:option>
							<form:option value="21">9:00 pm</form:option>
							<form:option value="22">10:00 pm</form:option>
							<form:option value="23">11:00 pm</form:option>
                </form:select>Hour
                  <br> <form:errors path="timeendinhours" cssClass="errorMessage" /></td>
               <td align="${left}">
			   <form:select cssClass="flat" path="timeendinminuts">
			                <form:option value="00">00</form:option>
							<%-- <form:option value="01">01</form:option>
							<form:option value="02">02</form:option>
							<form:option value="03">03</form:option>
							<form:option value="04">04</form:option> --%>
							<form:option value="05">05</form:option>
							<%-- <form:option value="06">06</form:option>
							<form:option value="07">07</form:option>
							<form:option value="08">08</form:option>
							<form:option value="09">09</form:option> --%>
							<form:option value="10">10</form:option>
							<%-- <form:option value="11">11</form:option>
							<form:option value="12">12</form:option>
							<form:option value="13">13</form:option>
							<form:option value="14">14</form:option> --%>
							<form:option value="15">15</form:option>
							<%-- <form:option value="16">16</form:option>
							<form:option value="17">17</form:option>
							<form:option value="18">18</form:option>
							<form:option value="19">19</form:option> --%>
							<form:option value="20">20</form:option>
							<%-- <form:option value="21">21</form:option>
							<form:option value="22">22</form:option>
							<form:option value="23">23</form:option>
							<form:option value="24">24</form:option> --%>
							<form:option value="25">25</form:option>
							<%-- <form:option value="26">26</form:option>
							<form:option value="27">27</form:option>
							<form:option value="28">28</form:option>
							<form:option value="29">29</form:option> --%>
							<form:option value="30">30</form:option>
							<%-- <form:option value="31">31</form:option>
							<form:option value="32">32</form:option>
							<form:option value="33">33</form:option>
							<form:option value="34">34</form:option> --%>
							<form:option value="35">35</form:option>
							<%-- <form:option value="36">36</form:option>
							<form:option value="37">37</form:option>
							<form:option value="38">38</form:option>
							<form:option value="39">39</form:option> --%>
							<form:option value="40">40</form:option>
							<%-- <form:option value="41">41</form:option>
							<form:option value="42">42</form:option>
							<form:option value="43">43</form:option>
							<form:option value="44">44</form:option> --%>
							<form:option value="45">45</form:option>
							<%-- <form:option value="46">46</form:option>
							<form:option value="47">47</form:option>
							<form:option value="48">48</form:option>
							<form:option value="49">49</form:option> --%>
							<form:option value="50">50</form:option>
							<%-- <form:option value="51">51</form:option>
							<form:option value="52">52</form:option>
							<form:option value="53">53</form:option>
							<form:option value="54">54</form:option> --%>
							<form:option value="55">55</form:option>
							<%-- <form:option value="56">56</form:option>
							<form:option value="57">57</form:option>
							<form:option value="58">58</form:option> --%>
							<form:option value="59">59</form:option>
					</form:select>Minutes
                  <br> <form:errors path="timeendinminuts" cssClass="errorMessage" />
                </td>
             </tr>
		<tr><td colspan="2"></td></tr>
		<tr><td>&nbsp;</td>
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
<script language="javascript">
getComTermCatagory();
</script>
	
