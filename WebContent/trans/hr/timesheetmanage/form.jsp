<%@include file="/common/taglibs.jsp"%>
<%@ page import="com.primovision.lutransport.model.hr.TimeSheet" %>
<script type="text/javascript">
function getworksHours0(field){ 
	
	if(!getTime(field)){
	var signIn=document.getElementById("ssignintimeid").value;
	//alert(signIn);
	var signInamOrpm=document.getElementById("ssigninampmid").value;
	//alert(signInamOrpm);
	var signOut=document.getElementById("ssignouttimeid").value;
	//alert(signOut);
	var signOutamOrpm=document.getElementById("ssignoutampmid").value;
	//alert(signOutamOrpm);
	//alert("0");
	
	if(signIn=="" && signInamOrpm=="" && signOut=="" && signOutamOrpm=="" ){
		document.getElementById("shoursworkedid").value="";
	}
	if(signIn!="" && signInamOrpm!="" && signOut!="" && signOutamOrpm!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn+'&signInamOrpm='+signInamOrpm+'&signOut='+signOut+'&signOutamOrpm='+signOutamOrpm,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var hoursWorked=listData[0];
				document.getElementById("shoursworkedid").value=hoursWorked;
				
				
			}
		});	
	}
}
}

function copyUrl(id){
    window.location.href = "./copy.do?id="+id
}



function getworksHours20(field){ 
	
	if(!getTime(field)){
	var signIn2=document.getElementById("s2signintimeid").value;
	
	var signInamOrpm2=document.getElementById("s2signinampmid").value;
	
	var signOut2=document.getElementById("s2signouttimeid").value;
	
	var signOutamOrpm2=document.getElementById("s2signoutampmid").value;
	
	var signIn=document.getElementById("ssignintimeid").value;
	
	var signInamOrpm=document.getElementById("ssigninampmid").value;
	
	var signOut=document.getElementById("ssignouttimeid").value;
	
	var signOutamOrpm=document.getElementById("ssignoutampmid").value;
	
	var slunchHour=document.getElementById("sluncHoursId").value;
	
	if(slunchHour==""){
		slunchHour = 0.0 ;
	}
	
	var hoursworked = 0.0;
	
	if(signIn=="" &&  signOut==""  && signIn2==""  && signOut2=="" ){
		document.getElementById("shoursworkedid").value="";
	}
	if(signIn!="" && signInamOrpm!="" && signOut!="" && signOutamOrpm!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn+'&signInamOrpm='+signInamOrpm+'&signOut='+signOut+'&signOutamOrpm='+signOutamOrpm,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				hoursworked=parseFloat(hoursworked)+parseFloat(listData[0]);
				hoursworked=Number(hoursworked).toFixed(2);
				//hoursworked = hoursworked - slunchHour;
				slunchHour = Number(slunchHour).toFixed(2);
				slunchHourArray = (slunchHour+"").split(".");
				
				hoursworkedarray=(hoursworked+"").split(".");			
				
				
				if(hoursworkedarray[1]>59){
					var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
					var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
					hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
					hoursworked=(hoursworked+".")+minutes;						
				}
				
				hoursworkedarray=(hoursworked+"").split(".");
				hrswrkd = (Number(hoursworked) * 60)%60;				
				subNum = Number(hoursworkedarray[1]).toFixed(2) - hrswrkd;				
				
				if(slunchHour > 0.0){
					hoursworkedhr = Number(hoursworked).toFixed(2)- parseInt(slunchHourArray[0]);
					hoursworkedquo = parseInt(((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))/60);
					hoursworkedrem = (((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))%60);	
					
					hoursworkedrem = hoursworkedrem + subNum;				
					hoursworked=(hoursworkedquo+".")+hoursworkedrem;
					
					
					hoursworkedarray=(hoursworked+"").split(".");						
					if(hoursworkedarray[1]>59){
						var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
						var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
						hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
						if(hoursworkedrem > 60){
							acthoursworkedrem = "0"+(hoursworkedrem - 60);
							hoursworked=(hoursworked+".")+acthoursworkedrem;
						}
						else{
							hoursworked=(hoursworked+".")+minutes;
						}												
					}
				}
				
				
				document.getElementById("shoursworkedid").value=Number(hoursworked).toFixed(2);
				
			}
		});	
	}
	
	if(signIn2!="" && signInamOrpm2!="" && signOut2!="" && signOutamOrpm2!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn2+'&signInamOrpm='+signInamOrpm2+'&signOut='+signOut2+'&signOutamOrpm='+signOutamOrpm2,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				hoursworked=parseFloat(hoursworked)+parseFloat(listData[0]);
				hoursworked=Number(hoursworked).toFixed(2);
				//hoursworked = hoursworked - slunchHour;
				
				hoursworkedarray=(hoursworked+"").split(".");			
								
				if(hoursworkedarray[1]>59){
					var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
					var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
					hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
					hoursworked=(hoursworked+".")+minutes;				    
				}
				
				/* hoursworkedhr = Number(hoursworked).toFixed(2)- parseInt(slunchHourArray[0]);
				hoursworkedquo = parseInt(((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))/60);
				hoursworkedrem = (((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))%60);				
				hoursworked=(hoursworkedquo+".")+hoursworkedrem;
				 */
				document.getElementById("shoursworkedid").value=Number(hoursworked).toFixed(2);
				
			}
		});	
	}
	
	
}
}






function getworksHours1(field){ 
	
	if(!getTime(field)){
	var signIn=document.getElementById("msignintimeid").value;
	var signInamOrpm=document.getElementById("msigninampmid").value;
	var signOut=document.getElementById("msignouttimeid").value;
	var signOutamOrpm=document.getElementById("msignoutampmid").value;
	//alert("1");
	
	if(signIn=="" && signInamOrpm=="" && signOut=="" && signOutamOrpm=="" ){
		document.getElementById("mhoursworkedid").value="";
	}
	if(signIn!="" && signInamOrpm!="" && signOut!="" && signOutamOrpm!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn+'&signInamOrpm='+signInamOrpm+'&signOut='+signOut+'&signOutamOrpm='+signOutamOrpm,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var hoursWorked=listData[0];
				document.getElementById("mhoursworkedid").value=hoursWorked;
				
				
			}
		});	
	}
}
}


function getworksHours21(field){

	if(!getTime(field)){
		
	var signIn=$('#msignintimeid').val();
	var signInamOrpm=$("#msigninampmid").val();
	var signOut=$("#msignouttimeid").val();
	var signOutamOrpm=$("#msignoutampmid").val();
	
	var signIn2=$("#m2signintimeid").val();
	var signInamOrpm2=$("#m2signinampmid").val();
	var signOut2=$("#m2signouttimeid").val();
	var signOutamOrpm2=$("#m2signoutampmid").val();

	var slunchHour=document.getElementById("mluncHoursId").value;
	
	if(slunchHour==""){
		slunchHour = 0.0 ;
	}
	var hoursworked = 0.0;
	
	if(signIn=="" && signIn2=="" && signOut=="" && signIn2=="" ){
		document.getElementById("mhoursworkedid").value="";
	}
	if(signIn!="" && signInamOrpm!="" && signOut!="" && signOutamOrpm!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn+'&signInamOrpm='+signInamOrpm+'&signOut='+signOut+'&signOutamOrpm='+signOutamOrpm,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				hoursworked=parseFloat(hoursworked)+parseFloat(listData[0]);
				hoursworked=Number(hoursworked).toFixed(2);
				
				slunchHour = Number(slunchHour).toFixed(2);
				slunchHourArray = (slunchHour+"").split(".");
				
				hoursworkedarray=(hoursworked+"").split(".");			
				
				if(hoursworkedarray[1]>59){
					var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
				    var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
				    hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
				    hoursworked=(hoursworked+".")+minutes;				    
				 }
				
				
				hoursworkedarray=(hoursworked+"").split(".");
				hrswrkd = (Number(hoursworked) * 60)%60;				
				subNum = Number(hoursworkedarray[1]).toFixed(2) - hrswrkd;
				
				if(slunchHour > 0.0){
					hoursworkedhr = Number(hoursworked).toFixed(2)- parseInt(slunchHourArray[0]);
					hoursworkedquo = parseInt(((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))/60);
					hoursworkedrem = (((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))%60);	

					hoursworkedrem = hoursworkedrem + subNum;				
					hoursworked=(hoursworkedquo+".")+hoursworkedrem;
					
					
					hoursworkedarray=(hoursworked+"").split(".");						
					if(hoursworkedarray[1]>59){
						var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
						var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
						hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
						if(hoursworkedrem > 60){
							acthoursworkedrem = "0"+(hoursworkedrem - 60);
							hoursworked=(hoursworked+".")+acthoursworkedrem;
						}
						else{
							hoursworked=(hoursworked+".")+minutes;
						}												
					}
					
				}
				$("#mhoursworkedid").val(Number(hoursworked).toFixed(2));
			}
		});	
	}
	if(signIn2!="" && signInamOrpm2!="" && signOut2!="" && signOutamOrpm2!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn2+'&signInamOrpm='+signInamOrpm2+'&signOut='+signOut2+'&signOutamOrpm='+signOutamOrpm2,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				hoursworked=parseFloat(hoursworked)+parseFloat(listData[0]);	
				hoursworked=Number(hoursworked).toFixed(2);
				
				hoursworkedarray=(hoursworked+"").split(".");				
				
				if(hoursworkedarray[1]>59){
					var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
				    var minutes = parseInt(hoursworkedarray[1]) % 60;			    	
				    hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
				    hoursworked=(hoursworked+".")+minutes;				    
				}
				/* hoursworkedhr = Number(hoursworked).toFixed(2)- parseInt(slunchHourArray[0]);
				hoursworkedquo = parseInt(((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))/60);
				hoursworkedrem = (((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))%60);				
				hoursworked=(hoursworkedquo+".")+hoursworkedrem; */
				$("#mhoursworkedid").val(Number(hoursworked).toFixed(2));
				//document.getElementById("mhoursworkedid").value=Number(hoursworked).toFixed(2);		
			}
		});	
	}	
}
}




function getworksHours2(field){ 

	if(!getTime(field)){
	var signIn=document.getElementById("tsignintimeid").value;
	var signInamOrpm=document.getElementById("tsigninampmid").value;
	var signOut=document.getElementById("tsignouttimeid").value;
	var signOutamOrpm=document.getElementById("tsignoutampmid").value;
	//alert("2");
	
	if(signIn=="" && signInamOrpm=="" && signOut=="" && signOutamOrpm=="" ){
		document.getElementById("thoursworkedid").value="";
	}
	if(signIn!="" && signInamOrpm!="" && signOut!="" && signOutamOrpm!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn+'&signInamOrpm='+signInamOrpm+'&signOut='+signOut+'&signOutamOrpm='+signOutamOrpm,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var hoursWorked=listData[0];
				document.getElementById("thoursworkedid").value=hoursWorked;
				
				
			}
		});	
	}
}
}


function getworksHours22(field){ 

	if(!getTime(field)){
	var signIn=document.getElementById("tsignintimeid").value;
	var signInamOrpm=document.getElementById("tsigninampmid").value;
	var signOut=document.getElementById("tsignouttimeid").value;
	var signOutamOrpm=document.getElementById("tsignoutampmid").value;
	
	
	var signIn2=document.getElementById("t2signintimeid").value;
	var signInamOrpm2=document.getElementById("t2signinampmid").value;
	var signOut2=document.getElementById("t2signouttimeid").value;
	var signOutamOrpm2=document.getElementById("t2signoutampmid").value;
	
	var slunchHour=document.getElementById("tluncHoursId").value;
	
	if(slunchHour==""){
		slunchHour = 0.0 ;
	}
	
	var hoursworked = 0.0;
	
	if(signIn=="" && signIn2=="" && signOut=="" && signOut2=="" ){
		document.getElementById("thoursworkedid").value="";
	}
	if(signIn!="" && signInamOrpm!="" && signOut!="" && signOutamOrpm!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn+'&signInamOrpm='+signInamOrpm+'&signOut='+signOut+'&signOutamOrpm='+signOutamOrpm,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				hoursworked=parseFloat(hoursworked)+parseFloat(listData[0]);
				hoursworked=Number(hoursworked).toFixed(2);

				slunchHour = Number(slunchHour).toFixed(2);
				slunchHourArray = (slunchHour+"").split(".");
				
				hoursworkedarray=(hoursworked+"").split(".");			
								
				if(hoursworkedarray[1]>59){
					var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
					var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
					hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
					hoursworked=(hoursworked+".")+minutes;				    
				}
				
				hoursworkedarray=(hoursworked+"").split(".");
				hrswrkd = (Number(hoursworked) * 60)%60;				
				subNum = Number(hoursworkedarray[1]).toFixed(2) - hrswrkd;
				
				if(slunchHour > 0.0){
					hoursworkedhr = Number(hoursworked).toFixed(2)- parseInt(slunchHourArray[0]);
					hoursworkedquo = parseInt(((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))/60);
					hoursworkedrem = (((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))%60);	
					
					hoursworkedrem = hoursworkedrem + subNum;				
					hoursworked=(hoursworkedquo+".")+hoursworkedrem;
					
					
					hoursworkedarray=(hoursworked+"").split(".");						
					if(hoursworkedarray[1]>59){
						var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
						var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
						hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
						if(hoursworkedrem > 60){
							acthoursworkedrem = "0"+(hoursworkedrem - 60);
							hoursworked=(hoursworked+".")+acthoursworkedrem;
						}
						else{
							hoursworked=(hoursworked+".")+minutes;
						}												
					}
				}
				document.getElementById("thoursworkedid").value=Number(hoursworked).toFixed(2);	
			}
		});	
	}
	
	if(signIn2!="" && signInamOrpm2!="" && signOut2!="" && signOutamOrpm2!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn2+'&signInamOrpm='+signInamOrpm2+'&signOut='+signOut2+'&signOutamOrpm='+signOutamOrpm2,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				hoursworked=parseFloat(hoursworked)+parseFloat(listData[0]);
				hoursworked=Number(hoursworked).toFixed(2);
				
				hoursworkedarray=(hoursworked+"").split(".");			
								
				if(hoursworkedarray[1]>59){
					var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
					var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
					hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
					hoursworked=(hoursworked+".")+minutes;				    
				}
				/* hoursworkedhr = Number(hoursworked).toFixed(2)- parseInt(slunchHourArray[0]);
				hoursworkedquo = parseInt(((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))/60);
				hoursworkedrem = (((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))%60);				
				hoursworked=(hoursworkedquo+".")+hoursworkedrem; */
				document.getElementById("thoursworkedid").value=Number(hoursworked).toFixed(2);			
			}
		});	
	}
}
}




function getworksHours3(field){

	if(!getTime(field)){
	var signIn=document.getElementById("wsignintimeid").value;
	var signInamOrpm=document.getElementById("wsigninampmid").value;
	var signOut=document.getElementById("wsignouttimeid").value;
	var signOutamOrpm=document.getElementById("wsignoutampmid").value;
	//alert("3");
	
	if(signIn=="" && signInamOrpm=="" && signOut=="" && signOutamOrpm=="" ){
		document.getElementById("whoursworkedid").value="";
	}
	if(signIn!="" && signInamOrpm!="" && signOut!="" && signOutamOrpm!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn+'&signInamOrpm='+signInamOrpm+'&signOut='+signOut+'&signOutamOrpm='+signOutamOrpm,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var hoursWorked=listData[0];
				document.getElementById("whoursworkedid").value=hoursWorked;
				
				
			}
		});	
	}
}
}



function getworksHours23(field){ 

	if(!getTime(field)){
	var signIn=document.getElementById("wsignintimeid").value;
	var signInamOrpm=document.getElementById("wsigninampmid").value;
	var signOut=document.getElementById("wsignouttimeid").value;
	var signOutamOrpm=document.getElementById("wsignoutampmid").value;
	
	
	var signIn2=document.getElementById("w2signintimeid").value;
	var signInamOrpm2=document.getElementById("w2signinampmid").value;
	var signOut2=document.getElementById("w2signouttimeid").value;
	var signOutamOrpm2=document.getElementById("w2signoutampmid").value;
	
	var slunchHour=document.getElementById("wluncHoursId").value;
	
	if(slunchHour==""){
		slunchHour = 0.0 ;
	}
	
	var hoursworked = 0.0;
	
	if(signIn=="" && signIn2=="" && signOut=="" && signOut2=="" ){
		document.getElementById("whoursworkedid").value="";
	}
	if(signIn!="" && signInamOrpm!="" && signOut!="" && signOutamOrpm!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn+'&signInamOrpm='+signInamOrpm+'&signOut='+signOut+'&signOutamOrpm='+signOutamOrpm,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				hoursworked=parseFloat(hoursworked)+parseFloat(listData[0]);
				hoursworked=Number(hoursworked).toFixed(2);

				slunchHour = Number(slunchHour).toFixed(2);
				slunchHourArray = (slunchHour+"").split(".");
				
				hoursworkedarray=(hoursworked+"").split(".");			
								
				if(hoursworkedarray[1]>59){
					var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
					var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
					hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
					hoursworked=(hoursworked+".")+minutes;				    
				}
				
				hoursworkedarray=(hoursworked+"").split(".");
				hrswrkd = (Number(hoursworked) * 60)%60;				
				subNum = Number(hoursworkedarray[1]).toFixed(2) - hrswrkd;
				
				if(slunchHour > 0.0){
					hoursworkedhr = Number(hoursworked).toFixed(2)- parseInt(slunchHourArray[0]);
					hoursworkedquo = parseInt(((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))/60);
					hoursworkedrem = (((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))%60);	
					
					hoursworkedrem = hoursworkedrem + subNum;				
					hoursworked=(hoursworkedquo+".")+hoursworkedrem;
					
					
					hoursworkedarray=(hoursworked+"").split(".");						
					if(hoursworkedarray[1]>59){
						var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
						var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
						hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
						if(hoursworkedrem > 60){
							acthoursworkedrem = "0"+(hoursworkedrem - 60);
							hoursworked=(hoursworked+".")+acthoursworkedrem;
						}
						else{
							hoursworked=(hoursworked+".")+minutes;
						}												
					}
				}
				document.getElementById("whoursworkedid").value=Number(hoursworked).toFixed(2);			
			}
		});	
	}
	
	if(signIn2!="" && signInamOrpm2!="" && signOut2!="" && signOutamOrpm2!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn2+'&signInamOrpm='+signInamOrpm2+'&signOut='+signOut2+'&signOutamOrpm='+signOutamOrpm2,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				hoursworked=parseFloat(hoursworked)+parseFloat(listData[0]);
				hoursworked=Number(hoursworked).toFixed(2);
				
				hoursworkedarray=(hoursworked+"").split(".");			
								
				if(hoursworkedarray[1]>59){
					var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
					var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
					hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
					hoursworked=(hoursworked+".")+minutes;				    
				}
				/* hoursworkedhr = Number(hoursworked).toFixed(2)- parseInt(slunchHourArray[0]);
				hoursworkedquo = parseInt(((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))/60);
				hoursworkedrem = (((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))%60);				
				hoursworked=(hoursworkedquo+".")+hoursworkedrem; */
				document.getElementById("whoursworkedid").value=Number(hoursworked).toFixed(2);			
			}
		});	
	}
}
}






function getworksHours24(field){ 

	if(!getTime(field)){	
	var signIn=document.getElementById("thsignintimeid").value;
	var signInamOrpm=document.getElementById("thsigninampmid").value;
	var signOut=document.getElementById("thsignouttimeid").value;
	var signOutamOrpm=document.getElementById("thsignoutampmid").value;
	
	var signIn2=document.getElementById("th2signintimeid").value;
	var signInamOrpm2=document.getElementById("th2signinampmid").value;
	var signOut2=document.getElementById("th2signouttimeid").value;
	var signOutamOrpm2=document.getElementById("th2signoutampmid").value;
	
	var slunchHour=document.getElementById("thluncHoursId").value;
	
	if(slunchHour==""){
		slunchHour = 0.0 ;
	}
	
	var hoursworked = 0.0;
	
	if(signIn=="" && signIn2=="" && signOut=="" && signOut2=="" ){
		document.getElementById("thhoursworkedid").value="";
	}
	if(signIn!="" && signInamOrpm!="" && signOut!="" && signOutamOrpm!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn+'&signInamOrpm='+signInamOrpm+'&signOut='+signOut+'&signOutamOrpm='+signOutamOrpm,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				hoursworked=parseFloat(hoursworked)+parseFloat(listData[0]);
				hoursworked=Number(hoursworked).toFixed(2);
				
				slunchHour = Number(slunchHour).toFixed(2);
				slunchHourArray = (slunchHour+"").split(".");
				
				hoursworkedarray=(hoursworked+"").split(".");			
								
				if(hoursworkedarray[1]>59){
					var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
					var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
					hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
					hoursworked=(hoursworked+".")+minutes;				    
				}
				
				hoursworkedarray=(hoursworked+"").split(".");
				hrswrkd = (Number(hoursworked) * 60)%60;				
				subNum = Number(hoursworkedarray[1]).toFixed(2) - hrswrkd;
				
				if(slunchHour > 0.0){
					hoursworkedhr = Number(hoursworked).toFixed(2)- parseInt(slunchHourArray[0]);
					hoursworkedquo = parseInt(((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))/60);
					hoursworkedrem = (((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))%60);	
					
					hoursworkedrem = hoursworkedrem + subNum;				
					hoursworked=(hoursworkedquo+".")+hoursworkedrem;
					
					
					hoursworkedarray=(hoursworked+"").split(".");						
					if(hoursworkedarray[1]>59){
						var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
						var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
						hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
						if(hoursworkedrem > 60){
							acthoursworkedrem = "0"+(hoursworkedrem - 60);
							hoursworked=(hoursworked+".")+acthoursworkedrem;
						}
						else{
							hoursworked=(hoursworked+".")+minutes;
						}												
					}
				}
				document.getElementById("thhoursworkedid").value=Number(hoursworked).toFixed(2);			
			}
		});	
	}
	
	
	
	if(signIn2!="" && signInamOrpm2!="" && signOut2!="" && signOutamOrpm2!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn2+'&signInamOrpm='+signInamOrpm2+'&signOut='+signOut2+'&signOutamOrpm='+signOutamOrpm2,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				hoursworked=parseFloat(hoursworked)+parseFloat(listData[0]);
				hoursworked=Number(hoursworked).toFixed(2);
				
				hoursworkedarray=(hoursworked+"").split(".");			
								
				if(hoursworkedarray[1]>59){
					var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
					var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
					hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
					hoursworked=(hoursworked+".")+minutes;				    
				}
				/* hoursworkedhr = Number(hoursworked).toFixed(2)- parseInt(slunchHourArray[0]);
				hoursworkedquo = parseInt(((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))/60);
				hoursworkedrem = (((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))%60);				
				hoursworked=(hoursworkedquo+".")+hoursworkedrem; */
				document.getElementById("thhoursworkedid").value=Number(hoursworked).toFixed(2);				
			}
		});	
	}
}
}


function getworksHours5(field){ 

	if(!getTime(field)){
	var signIn=document.getElementById("fsignintimeid").value;
	var signInamOrpm=document.getElementById("fsigninampmid").value;
	var signOut=document.getElementById("fsignouttimeid").value;
	var signOutamOrpm=document.getElementById("fsignoutampmid").value;
	//alert("5");
	
	if(signIn=="" && signInamOrpm=="" && signOut=="" && signOutamOrpm=="" ){
		document.getElementById("fhoursworkedid").value="";
	}
	if(signIn!="" && signInamOrpm!="" && signOut!="" && signOutamOrpm!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn+'&signInamOrpm='+signInamOrpm+'&signOut='+signOut+'&signOutamOrpm='+signOutamOrpm,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var hoursWorked=listData[0];
				document.getElementById("fhoursworkedid").value=hoursWorked;
				
				
			}
		});	
	}
}
}




function getworksHours25(field){ 

	if(!getTime(field)){
	var signIn=document.getElementById("fsignintimeid").value;
	var signInamOrpm=document.getElementById("fsigninampmid").value;
	var signOut=document.getElementById("fsignouttimeid").value;
	var signOutamOrpm=document.getElementById("fsignoutampmid").value;
	
	var signIn2=document.getElementById("f2signintimeid").value;
	var signInamOrpm2=document.getElementById("f2signinampmid").value;
	var signOut2=document.getElementById("f2signouttimeid").value;
	var signOutamOrpm2=document.getElementById("f2signoutampmid").value;
	
	var slunchHour=document.getElementById("fluncHoursId").value;
	
	if(slunchHour==""){
		slunchHour = 0.0 ;
	}
	
	var hoursworked = 0.0;
	if(signIn=="" && signIn2=="" && signOut=="" && signOut2=="" ){
		document.getElementById("fhoursworkedid").value="";
	}
	if(signIn!="" && signInamOrpm!="" && signOut!="" && signOutamOrpm!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn+'&signInamOrpm='+signInamOrpm+'&signOut='+signOut+'&signOutamOrpm='+signOutamOrpm,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				hoursworked=parseFloat(hoursworked)+parseFloat(listData[0]);
				hoursworked=Number(hoursworked).toFixed(2);

				slunchHour = Number(slunchHour).toFixed(2);
				slunchHourArray = (slunchHour+"").split(".");
				
				hoursworkedarray=(hoursworked+"").split(".");			
								
				if(hoursworkedarray[1]>59){
					var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
					var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
					hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
					hoursworked=(hoursworked+".")+minutes;				    
				}
				
				hoursworkedarray=(hoursworked+"").split(".");
				hrswrkd = (Number(hoursworked) * 60)%60;				
				subNum = Number(hoursworkedarray[1]).toFixed(2) - hrswrkd;
				
				if(slunchHour > 0.0){
					hoursworkedhr = Number(hoursworked).toFixed(2)- parseInt(slunchHourArray[0]);
					hoursworkedquo = parseInt(((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))/60);
					hoursworkedrem = (((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))%60);	
					
					hoursworkedrem = hoursworkedrem + subNum;				
					hoursworked=(hoursworkedquo+".")+hoursworkedrem;
					
					
					hoursworkedarray=(hoursworked+"").split(".");						
					if(hoursworkedarray[1]>59){
						var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
						var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
						hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
						if(hoursworkedrem > 60){
							acthoursworkedrem = "0"+(hoursworkedrem - 60);
							hoursworked=(hoursworked+".")+acthoursworkedrem;
						}
						else{
							hoursworked=(hoursworked+".")+minutes;
						}												
					}
				}
				document.getElementById("fhoursworkedid").value=Number(hoursworked).toFixed(2);				
			}
		});	
	}
	
	if(signIn2!="" && signInamOrpm2!="" && signOut2!="" && signOutamOrpm2!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn2+'&signInamOrpm='+signInamOrpm2+'&signOut='+signOut2+'&signOutamOrpm='+signOutamOrpm2,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				hoursworked=parseFloat(hoursworked)+parseFloat(listData[0]);
				hoursworked=Number(hoursworked).toFixed(2);
				
				hoursworkedarray=(hoursworked+"").split(".");			
								
				if(hoursworkedarray[1]>59){
					var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
					var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
					hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
					hoursworked=(hoursworked+".")+minutes;				    
				}
				/* hoursworkedhr = Number(hoursworked).toFixed(2)- parseInt(slunchHourArray[0]);
				hoursworkedquo = parseInt(((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))/60);
				hoursworkedrem = (((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))%60);				
				hoursworked=(hoursworkedquo+".")+hoursworkedrem; */
				document.getElementById("fhoursworkedid").value=Number(hoursworked).toFixed(2);				
			}
		});	
	}
}
}



function getworksHours6(field){ 

	if(!getTime(field)){
	var signIn=document.getElementById("stsignintimeid").value;
	var signInamOrpm=document.getElementById("stsigninampmid").value;
	var signOut=document.getElementById("stsignouttimeid").value;
	var signOutamOrpm=document.getElementById("stsignoutampmid").value;
	//alert("6");
	
	if(signIn=="" && signInamOrpm=="" && signOut=="" && signOutamOrpm=="" ){
		document.getElementById("sthoursworkedid").value="";
	}
	if(signIn!="" && signInamOrpm!="" && signOut!="" && signOutamOrpm!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn+'&signInamOrpm='+signInamOrpm+'&signOut='+signOut+'&signOutamOrpm='+signOutamOrpm,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var hoursWorked=listData[0];
				document.getElementById("sthoursworkedid").value=Number(hoursworked).toFixed(2);
				
				
			}
		});	
	}
	}
}



function getworksHours26(field){ 

	if(!getTime(field)){
	var signIn=document.getElementById("stsignintimeid").value;
	var signInamOrpm=document.getElementById("stsigninampmid").value;
	var signOut=document.getElementById("stsignouttimeid").value;
	var signOutamOrpm=document.getElementById("stsignoutampmid").value;
	  
	var signIn2=document.getElementById("st2signintimeid").value;
	var signInamOrpm2=document.getElementById("st2signinampmid").value;
	var signOut2=document.getElementById("st2signouttimeid").value;
	var signOutamOrpm2=document.getElementById("st2signoutampmid").value;
	
	var slunchHour=document.getElementById("stluncHoursId").value;
	
	if(slunchHour==""){
		slunchHour = 0.0 ;
	}
	
	var hoursworked = 0.0;
	
	if(signIn=="" && signIn2=="" && signOut=="" && signOut2=="" ){
		document.getElementById("sthoursworkedid").value="";
	}
	if(signIn!="" && signInamOrpm!="" && signOut!="" && signOutamOrpm!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn+'&signInamOrpm='+signInamOrpm+'&signOut='+signOut+'&signOutamOrpm='+signOutamOrpm,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				hoursworked=parseFloat(hoursworked)+parseFloat(listData[0]);
				hoursworked=Number(hoursworked).toFixed(2);

				slunchHour = Number(slunchHour).toFixed(2);
				slunchHourArray = (slunchHour+"").split(".");
				
				hoursworkedarray=(hoursworked+"").split(".");			
								
				if(hoursworkedarray[1]>59){
					var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
					var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
					hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
					hoursworked=(hoursworked+".")+minutes;				    
				}
				
				hoursworkedarray=(hoursworked+"").split(".");
				hrswrkd = (Number(hoursworked) * 60)%60;				
				subNum = Number(hoursworkedarray[1]).toFixed(2) - hrswrkd;
				
				if(slunchHour > 0.0){
					hoursworkedhr = Number(hoursworked).toFixed(2)- parseInt(slunchHourArray[0]);
					hoursworkedquo = parseInt(((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))/60);
					hoursworkedrem = (((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))%60);	
					
					hoursworkedrem = hoursworkedrem + subNum;				
					hoursworked=(hoursworkedquo+".")+hoursworkedrem;
					
					
					hoursworkedarray=(hoursworked+"").split(".");						
					if(hoursworkedarray[1]>59){
						var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
						var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
						hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
						if(hoursworkedrem > 60){
							acthoursworkedrem = "0"+(hoursworkedrem - 60);
							hoursworked=(hoursworked+".")+acthoursworkedrem;
						}
						else{
							hoursworked=(hoursworked+".")+minutes;
						}												
					}
				}
				
				
				document.getElementById("sthoursworkedid").value=Number(hoursworked).toFixed(2);
				
				
			}
		});	
	}
	
	if(signIn2!="" && signInamOrpm2!="" && signOut2!="" && signOutamOrpm2!=""){
		jQuery.ajax({
			url:'${ctx}/hr/timesheetmanage/ajax.do?action=fidworkedHours&signIn='+signIn2+'&signInamOrpm='+signInamOrpm2+'&signOut='+signOut2+'&signOutamOrpm='+signOutamOrpm2,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				hoursworked=parseFloat(hoursworked)+parseFloat(listData[0]);
				hoursworked=Number(hoursworked).toFixed(2);
				
				hoursworkedarray=(hoursworked+"").split(".");			
								
				if(hoursworkedarray[1]>59){
					var hours = Math.floor( parseInt(hoursworkedarray[1]) / 60);          
					var minutes = parseInt(hoursworkedarray[1]) % 60;				    	
					hoursworked=parseInt(hoursworkedarray[0])+parseInt(hours);
					hoursworked=(hoursworked+".")+minutes;				    
				}
				/* hoursworkedhr = Number(hoursworked).toFixed(2)- parseInt(slunchHourArray[0]);
				hoursworkedquo = parseInt(((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))/60);
				hoursworkedrem = (((Number(hoursworkedhr)*60) - parseInt(slunchHourArray[1]))%60);				
				hoursworked=(hoursworkedquo+".")+hoursworkedrem; */
				document.getElementById("sthoursworkedid").value=Number(hoursworked).toFixed(2);
				
				
			}
		});	
	}
	
	
	}
}




function getComTermCatagory(){

	var emp=document.getElementById("employeeId");
	var driver=emp.options[emp.selectedIndex].value;
	
	jQuery.ajax({
		url:'${ctx}/hr/timesheetmanage/ajax.do?action=findDCompany&driver='+driver, 
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
		url:'${ctx}/hr/timesheetmanage/ajax.do?action=findDTerminal&driver='+driver, 
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
		url:'${ctx}/hr/timesheetmanage/ajax.do?action=findDCategory&driver='+driver, 
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
	
	jQuery.ajax({
		url:'${ctx}/hr/timesheetmanage/ajax.do?action=findhours&driver='+driver, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options;
			
			if(driver==""){
				
			}			
			$("#weeklyHoursid").val(listData[0]);
			$("#dailyHoursid").val (listData[1]);
		}
			
		});
	  }

function formatDate(){
	var date=document.getElementById("datepicker").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalid date format");
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
		alert("Invalid date format");
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
			getAllDaysOfWeek(date);
		}		
		getAllDaysOfWeek(date);		
	 }
   }
	else{
		document.getElementById("datepicker2").value="";
		document.getElementById("datepicker3").value="";
		document.getElementById("datepicker4").value="";
		document.getElementById("datepicker5").value="";
		document.getElementById("datepicker6").value="";
		document.getElementById("datepicker7").value="";
		document.getElementById("datepicker").value="";
	}
		
}


function getAllDaysOfWeek(date){
	jQuery.ajax({				
		url:'${ctx}/hr/timesheetmanage/ajax.do?action=allDaysOfWeek&batchDate='+date,
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			if(listData)
			document.getElementById("datepicker2").value=listData[0];
			document.getElementById("datepicker3").value=listData[1];
			document.getElementById("datepicker4").value=listData[2];
			document.getElementById("datepicker5").value=listData[3];
			document.getElementById("datepicker6").value=listData[4];
			document.getElementById("datepicker7").value=listData[5];
			document.getElementById("datepicker").value=listData[6];
		}
	});
}


function formatDate2(){
	var date=document.getElementById("datepicker2").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker2").value="";
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
						document.getElementById("datepicker2").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker2").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker2").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker2").value=date;
			getAllDaysOfWeek(date);
		}
		getAllDaysOfWeek(date);
	 }
   }
	else{
		document.getElementById("datepicker2").value="";
		document.getElementById("datepicker3").value="";
		document.getElementById("datepicker4").value="";
		document.getElementById("datepicker5").value="";
		document.getElementById("datepicker6").value="";
		document.getElementById("datepicker7").value="";
		document.getElementById("datepicker").value="";
	}
}


function formatDate3(){
	var date=document.getElementById("datepicker3").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker3").value="";
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
						document.getElementById("datepicker3").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker3").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker3").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker3").value=date;
		}
	 }
   }
}

function formatDate4(){
	var date=document.getElementById("datepicker4").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker4").value="";
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
						document.getElementById("datepicker4").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker4").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker4").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker4").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker4").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker4").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker4").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker4").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker4").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker4").value=date;
		}
	 }
   }
}


function formatDate5(){
	var date=document.getElementById("datepicker5").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker5").value="";
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
						document.getElementById("datepicker5").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker5").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker5").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker5").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker5").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker5").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker5").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker5").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker5").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker5").value=date;
		}
	 }
   }
}

function formatDate6(){
	var date=document.getElementById("datepicker6").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker6").value="";
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
						document.getElementById("datepicker6").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker6").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker6").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker6").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker6").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker6").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker6").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker6").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker6").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker6").value=date;
		}
	 }
   }
}

 function formatDate7(){
	var date=document.getElementById("datepicker7").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker7").value="";
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
						document.getElementById("datepicker7").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker7").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker7").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker7").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker7").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker7").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker7").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker7").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker7").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker7").value=date;
		}
	 }
   }
} 

 function getTotalworksHours(){
	
	 //alert("ok");
     
	  var weeklyHours=document.getElementById("weeklyHoursid").value;
	  var dailyregularHours=document.getElementById("dailyHoursid").value;
	    
	  if(weeklyHours==""){
		  weeklyHours=0.0;
	  }
	  
	  if(dailyregularHours=""){
		  dailyregularHours=0.0;
	  }
	  
	  
		var SWhours=document.getElementById("shoursworkedid").value;
		var MWhours=document.getElementById("mhoursworkedid").value;
		var TWhours=document.getElementById("thoursworkedid").value;
		var WWhours=document.getElementById("whoursworkedid").value;
		var THWhours=document.getElementById("thhoursworkedid").value;
		var FWhours=document.getElementById("fhoursworkedid").value;
		var STWhours=document.getElementById("sthoursworkedid").value;
		
		//OT FLAGS
		var Sot=document.getElementById("sotflagid").value;
		var Mot=document.getElementById("motflagid").value;
		var Tot=document.getElementById("totflagid").value;
		var Wot=document.getElementById("wotflagid").value;
		var THot=document.getElementById("thotflagid").value;
		var Fot=document.getElementById("fotflagid").value;
		var STot=document.getElementById("stotflagid").value;
		
		//DT FLAGS
		var Sdt=document.getElementById("sdtflagid").value;
		var Mdt=document.getElementById("mdtflagid").value;
		var Tdt=document.getElementById("tdtflagid").value;
		var Wdt=document.getElementById("wdtflagid").value;
		var THdt=document.getElementById("thdtflagid").value;
		var Fdt=document.getElementById("fdtflagid").value;
		var STdt=document.getElementById("stdtflagid").value;
		
		
		//
		var Mholiday=document.getElementById("mholidayflagid").value;
		var Tholiday=document.getElementById("tholidayflagid").value;
		var Wholiday=document.getElementById("wholidayflagid").value;
		var THholiday=document.getElementById("thholidayflagid").value;
		var Fholiday=document.getElementById("fholidayflagid").value;
		var STholiday=document.getElementById("stholidayflagid").value;	
		var Sholiday=document.getElementById("sholidayflagid").value;
		
		
		
		
		
		
		// alert("ok1");
		if(weeklyHours!=""){
			
			//alert("WRH not null");
			document.getElementById("totaldthoursinweekid").value="";
		 //if(SWhours!="" && MWhours!="" && TWhours!="" && WWhours!="" && THWhours!="" && FWhours!="" && STWhours!="")
		 //{
				jQuery.ajax({
					url:'${ctx}/hr/timesheetmanage/ajax.do?action=totalworkedHours&SWhours='+SWhours+'&MWhours='+MWhours+'&TWhours='+TWhours+'&WWhours='+WWhours+'&THWhours='+THWhours+'&FWhours='+FWhours+'&STWhours='+STWhours+'&weeklyHours='+weeklyHours+'&dailyregularHours='+dailyregularHours+
							'&Sot='+Sot+'&Mot='+Mot+'&Tot='+Tot+'&Wot='+Wot+'&THot='+THot+'&Fot='+Fot+'&STot='+STot+
							'&Sdt='+Sdt+'&Mdt='+Mdt+'&Tdt='+Tdt+'&Wdt='+Wdt+'&THdt='+THdt+'&Fdt='+Fdt+'&STdt='+STdt+
							'&Sholiday='+Sholiday+'&Mholiday='+Mholiday+'&Tholiday='+Tholiday+'&Wholiday='+Wholiday+'&THholiday='+THholiday+'&Fholiday='+Fholiday+'&STholiday='+STholiday,
					success: function( data ) {
						var listData=jQuery.parseJSON(data);
						var tohoursWorked=listData[0];
						var Rhours=listData[1];
						var OThours=listData[2];
						var holidayhours=listData[7];
						//var roundedTotal=listData[4];
						var hoursinrounded=listData[5];
						document.getElementById("totalhoursworkedInweekid").value=tohoursWorked; 
						document.getElementById("regularhoursid").value=Rhours;
						document.getElementById("totalothoursinweekid").value=OThours;
						document.getElementById("holidayhoursid").value=holidayhours;
						//document.getElementById("totalhoursworkedInweekRoundedid").value=roundedTotal;
						
						document.getElementById("hoursworkedInweekRoundedValueid").value=hoursinrounded;
						
						
					}
				});	
			//}
		}
		if(dailyregularHours!="" && weeklyHours==""){
			//alert("DRH not null and WRH is null");
			 //if(SWhours!="" && MWhours!="" && TWhours!="" && WWhours!="" && THWhours!="" && FWhours!="" && STWhours!="")
			 //{
					jQuery.ajax({
						url:'${ctx}/hr/timesheetmanage/ajax.do?action=totalworkedHours&SWhours='+SWhours+'&MWhours='+MWhours+'&TWhours='+TWhours+'&WWhours='+WWhours+'&THWhours='+THWhours+'&FWhours='+FWhours+'&STWhours='+STWhours+'&weeklyHours='+weeklyHours+'&dailyregularHours='+dailyregularHours+
								'&Sot='+Sot+'&Mot='+Mot+'&Tot='+Tot+'&Wot='+Wot+'&THot='+THot+'&Fot='+Fot+'&STot='+STot+
								'&Sdt='+Sdt+'&Mdt='+Mdt+'&Tdt='+Tdt+'&Wdt='+Wdt+'&THdt='+THdt+'&Fdt='+Fdt+'&STdt='+STdt+
								'&Sholiday='+Sholiday+'&Mholiday='+Mholiday+'&Tholiday='+Tholiday+'&Wholiday='+Wholiday+'&THholiday='+THholiday+'&Fholiday='+Fholiday+'&STholiday='+STholiday,
						success: function( data ) {
							var listData=jQuery.parseJSON(data);
							var tohoursWorked=listData[0];
							var Rhours=listData[1];
							var OThours=listData[2];
							var DThours=listData[3];
							var holidayhours=listData[7];
							//var roundedTotal=listData[4];
							var hoursinrounded=listData[5];
							document.getElementById("totalhoursworkedInweekid").value=tohoursWorked; 
							document.getElementById("regularhoursid").value=Rhours;
							document.getElementById("totalothoursinweekid").value=OThours;
							document.getElementById("totaldthoursinweekid").value=DThours;
							document.getElementById("holidayhoursid").value=holidayhours;
							//document.getElementById("totalhoursworkedInweekRoundedid").value=roundedTotal;
							document.getElementById("hoursworkedInweekRoundedValueid").value=hoursinrounded;
							
						}
					});	
				//}
			   
			}
		if(dailyregularHours=="" && weeklyHours=="")
			{
			   alert("Plese enter either weekly regular hours or daily regular hours!");
			}
	}
 
 
 function getTime(field){
	 
	 if(field=='sluncHoursId' || field=='mluncHoursId' || field=='tluncHoursId' || field=='wluncHoursId' || field=='thluncHoursId' || field=='fluncHoursId' || field=='stluncHoursId'){
		 return false;
	 }
	 else{
		var timein=document.getElementById(field).value;
		if(timein!=""){
			if(timein.length<4){
				alert("Invalidte time format");
				document.getElementById(field).value="";
				document.getElementById(field).focus();
				return true;
			}else{
				var str=new String(timein);
				if(!str.match(":")){
					var hour=str.substring(0,2);
					var min=str.substring(2,4);
					if(hour>=24 || min>=60){
						alert("Invalidte time format");
						document.getElementById(field).value="";
						document.getElementById(field).focus();
						return true;
					}
					var time=hour+":"+min;
					document.getElementById(field).value=time;
				}
			}
		}
 	}
}
 
 

 function setWrDrHrs(batchDate) {
 		var emp = document.getElementById("employeeId");
 		var driver = emp.options[emp.selectedIndex].value;
 		if (driver != '' ) {
 			if(batchDate!=''){
 				jQuery
 			
 					.ajax({
 						url : '${ctx}/hr/timesheetmanage/ajax.do?action=getWrDrHrsOnBdt&driver='
 								+ driver + '&batchdate=' + batchDate,
 						success : function(data) {
 							var listData=jQuery.parseJSON(data);
 							$("#weeklyHoursid").val(listData[0]);
 							$("#dailyHoursid").val (listData[1]);
 						}
 					});
 			}
 		}else{
 			alert("Please select Driver");
 		}
 	}
 
 function processForm() {
		var submitBtn = document.getElementById("create");
		submitBtn.disabled = true;
		
		var form = document.getElementById("timesheetManageForm");
		form.submit();
		
		return false;
	}
 
</script>

<h3><primo:label code="Add/Update Time Sheet" /></h3>
<form:form action="save.do" name="typeForm" id="timesheetManageForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<form:hidden path="mode" id="mode"/>
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="14"><b><primo:label code="Manage Time Sheet" /></b></td>
		</tr>
		
		<tr>
            <td class="form-left"><primo:label code="Employee" /><span	class="errorMessage">*</span></td>
            <%-- 	<td align="${left}"><form:select cssClass="flat" path="driver" id="employeeId"  onchange="javascript:getComTermCatagory()">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<c:forEach var="item" items="${employees}">
						<c:set var="selected" value=""/>
						<c:if test="${item.id eq modelObject.driver.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${item.id}" ${selected}>${item.fullName}--${item.catagory.name}</option>
					</c:forEach>
				</form:select> <br> <form:errors path="driver" cssClass="errorMessage" />
			</td> --%>
			<td align="${left}">
				<form:select cssClass="flat" id="employeeId" path="driver"  style="min-width:120px; max-width:120px" onchange="javascript:getComTermCatagory()">
					<form:option value="">--Please Select--</form:option>
					<form:options items="${employees}" itemValue="id" itemLabel="FullName" />
				</form:select> 
				<br><form:errors path="driver" cssClass="errorMessage" /></td>
				<td ><primo:label code="" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>	
			<td class="form-left"><primo:label code="Category" /><span	class="errorMessage">*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
			<td align="${left}">
			   <select name="catagory" id="categoryId"   style="min-width:100px; max-width:100px">
					<option value="">Please Select</option>
					<!--   <option value=""></option> -->
					<c:forEach var="item" items="${catagories}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
				<br><form:errors path="catagory" cssClass="errorMessage" /></td>
				
				<td >&nbsp;</td>
			    <td >&nbsp;</td>
			    
			    <td ><input type="text" disabled="disabled" style="min-width:100px; max-width:100px"/></td>
			    <td >&nbsp;</td>
			    
			    <td ><input type="text" disabled="disabled" style="min-width:100px; max-width:100px"/></td>
			    <td >&nbsp;</td>
				
		</tr>
		
		
		<tr>
		 <td class="form-left"><primo:label code="Company" /><span	class="errorMessage">*</span></td>
			<td align="${left}">				
				<select name="company" id="company"   style="min-width:120px; max-width:120px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${companies}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
				<br><form:errors path="company" cssClass="errorMessage" />
			</td>
			<td ><primo:label code="" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			 <td class="form-left"><primo:label code="Terminal" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<select name="terminal" id="terminalId"   style="min-width:100px; max-width:100px">
					<option value="">Please Select</option>
					<c:forEach var="item1" items="${terminals}">
						<option value="${item1.id}">${item1.name}</option>
					</c:forEach>
				</select>
			   <br><form:errors path="terminal" cssClass="errorMessage" />
			</td>
			
	    </tr>
	    
		<tr>
		<td class="form-left"><primo:label code="Weekly Regular Hours for OT" /><span class="errorMessage"></span></td>
			<td align="${left}"><form:input path="weeklyHours" id="weeklyHoursid" style="min-width:116px; max-width:116px"
					Class="flat"  readonly="false"/> <%-- <br> <form:errors path="weeklyHours" cssClass="errorMessage" /> --%>
			</td>
		<td ><primo:label code="" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td class="form-left"><primo:label code="Daily Regular Hours for OT" /><span class="errorMessage"></span></td>
			<td align="${left}"><form:input path="dailyHours" id="dailyHoursid"
					Class="flat" style="min-width:96px; max-width:96px" readonly="false"/> <%-- <br> <form:errors path="weeklyHours" cssClass="errorMessage" /> --%>
			</td>
		  
		</tr>
		<%-- <tr>
		      <td class="form-left"><primo:label code="Batch Date" /><span class="errorMessage">*</span></td>
		      <td align="${left}"><form:input id="datepicker1" path="batchdate" cssClass="flat"  style="min-width:116px; max-width:116px" onblur="return formatDate1();" onchange="return formatDate1();"/> <br> 
			     <form:errors path="batchdate" cssClass="errorMessage" />
	           </td>
	           <!-- <td>&nbsp;</td>
				<td>&nbsp;</td> -->
				
	     </tr> --%>
	     
	     
	     <tr>
		      <td class="form-left"><primo:label code="Batch Date" /><span class="errorMessage">*</span></td>
		      <td align="${left}"><form:input id="datepicker1" path="batchdate" cssClass="flat"  style="min-width:116px; max-width:116px" onblur="return setWrDrHrs(this.value);" onkeyup="return setWrDrHrs(this.value);" onkeydown="return setWrDrHrs(this.value);" onchange="return formatDate1();"/> <br> 
			     <form:errors path="batchdate" cssClass="errorMessage" />
	           </td>
	           <!-- <td>&nbsp;</td>
				<td>&nbsp;</td> -->
			<td>
				<c:if test="${not empty timesheetid}">
                 <input type="button" onclick="javascript:copyUrl(${timesheetid})" value=" Copy " class="flat" />
             </c:if>
				
			</td>		
	     </tr>
	     
	     
		<tr class="table-heading">
			<td colspan="16"><b><primo:label code="Daily hours" /></b></td>
		</tr>
		
		
		
		
		<tr>
		    <!-- style="width:100px" -->
		    <td class="form-left" width="75px"><primo:label code="Day" /><span class="errorMessage"></span></td>
		    <td class="form-left" width="30px"><primo:label code="Date" /><span class="errorMessage"></span></td>
			
			<td class="form-left"  colspan="2" width="30px"><primo:label code="Sign In Time"/><span class="errorMessage"></span></td>
			<td class="form-left" colspan="2" width="30px"><primo:label code="Sign Out Time"/><span class="errorMessage"></span></td>
			<td class="form-left"  colspan="2" width="30px"><primo:label code="Sign In Time"/><span class="errorMessage"></span></td>
			<td class="form-left" colspan="2" width="30px"><primo:label code="Sign Out Time"/><span class="errorMessage"></span></td>
			<td class="form-left" width="30px"><primo:label code="Lunch" /><span class="errorMessage"></span></td>
			<td class="form-left" width="30px"><primo:label code="Hours Worked" /><span class="errorMessage"></span></td>
			<td class="form-left" width="30px"><primo:label code="OT" /><span class="errorMessage"></span></td>
			<td class="form-left" width="30px"><primo:label code="DT" /><span class="errorMessage"></span></td>
		    <td class="form-left" width="30px"><primo:label code="Holiday" /><span class="errorMessage"></span></td>
	  </tr>
     
     	
	 
	  <tr > <!-- monday  -->
	      <td align="${left}"><form:input id="mondayname" path="mondayname" cssClass="flat" value="Monday" readonly="true" style="min-width:75px; max-width:75px"/> <br> 
			    <form:errors path="mondayname" cssClass="errorMessage" /></td>
         <td align="${left}"><form:input id="datepicker2" path="mdate" cssClass="flat" style="min-width:72px; max-width:72px"  onblur="return formatDate2();" onchange="return formatDate2();"/> <br> 
			    <form:errors path="mdate" cssClass="errorMessage" /></td>
	     
	     <td align="${left}" colspan="2">
			<form:input id="msignintimeid" path="msignintime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours21('msignintimeid')"/> 
					<form:errors path="msignintime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="msigninampm" id="msigninampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours21('msignintimeid')" onblur="javascript:getworksHours21('msignintimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${ampm}" itemValue="dataText" itemLabel="dataText" />
			 </form:select>
		</td>
		<td align="${left}" colspan="2">
			 <form:input id="msignouttimeid" path="msignouttime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours21('msignouttimeid')"/> 
					 <form:errors path="msignouttime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="msignoutampm" id="msignoutampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours21('msignouttimeid')" onblur="javascript:getworksHours21('msignouttimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${pmam}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
			 
		 </td>
		 
		 
		 
		 
		 <td align="${left}" colspan="2">
			<form:input id="m2signintimeid" path="m2signintime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours21('m2signintimeid')"/> 
					<form:errors path="m2signintime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="m2signinampm" id="m2signinampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours21('m2signintimeid')" onblur="javascript:getworksHours21('m2signintimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${ampm}" itemValue="dataText" itemLabel="dataText" />
			 </form:select>
		</td>
		<td align="${left}" colspan="2">
			 <form:input id="m2signouttimeid" path="m2signouttime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours21('m2signouttimeid')"/> 
					 <form:errors path="m2signouttime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="m2signoutampm" id="m2signoutampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours21('m2signouttimeid')" onblur="javascript:getworksHours21('m2signouttimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${pmam}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
			 
		 </td>
		 
		 
		 
		 <td align="${left}"><form:input path="mluncHours" id="mluncHoursId" Class="flat" style="min-width:50px; max-width:50px" onblur="javascript:getworksHours21('mluncHoursId')"/> 
		     <br> <form:errors path="mluncHours" cssClass="errorMessage" /></td>	 
		 
		 
		 <td align="${left}"><form:input path="mhoursworked" id="mhoursworkedid" Class="flat" style="min-width:50px; max-width:50px"/> 
		     <br> <form:errors path="mhoursworked" cssClass="errorMessage" /></td>
					
		<td><%-- <form:checkbox path="sotflag" id="sotflagid" name="box1"/></td> --%>
		       <form:select cssClass="flat" path="mtotflag" id="motflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${otstatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
		 <td><%-- <form:checkbox path="sotflag" id="sotflagid" name="box1"/></td> --%>
		       <form:select cssClass="flat" path="mdtflag" id="mdtflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${dtstatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
	     <td>
		       <form:select cssClass="flat" path="mholidayflag" id="mholidayflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${holidaystatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
	 </tr>
	
	 <tr> <!-- Tuesday  -->
	      <td align="${left}"><form:input id="tuesdayname" path="tuesdayname" cssClass="flat" value="Tuesday" readonly="true"   style="min-width:75px; max-width:75px"/> <br> 
			    <form:errors path="tuesdayname" cssClass="errorMessage" /></td>
         <td align="${left}"><form:input id="datepicker3" path="tdate" cssClass="flat" style="min-width:72px; max-width:72px"  onblur="return formatDate3();"/> <br> 
			    <form:errors path="tdate" cssClass="errorMessage" /></td>
	     
	     <td align="${left}" colspan="2">
			<form:input id="tsignintimeid" path="tsignintime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours22('tsignintimeid')"/> 
					<form:errors path="tsignintime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="tsigninampm" id="tsigninampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours22('tsignintimeid')" onblur="javascript:getworksHours22('tsignintimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${ampm}" itemValue="dataText" itemLabel="dataText" />
			 </form:select>
		</td>
		<td align="${left}" colspan="2">
			 <form:input id="tsignouttimeid" path="tsignouttime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours22('tsignouttimeid')"/> 
					 <form:errors path="tsignouttime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="tsignoutampm" id="tsignoutampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours22('tsignouttimeid')" onblur="javascript:getworksHours22('tsignouttimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${pmam}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
			 
		 </td>
		 
		 
		 
		 
		 <td align="${left}" colspan="2">
			<form:input id="t2signintimeid" path="t2signintime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours22('t2signintimeid')"/> 
					<form:errors path="t2signintime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="t2signinampm" id="t2signinampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours22('t2signintimeid')" onblur="javascript:getworksHours22('t2signintimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${ampm}" itemValue="dataText" itemLabel="dataText" />
			 </form:select>
		</td>
		<td align="${left}" colspan="2">
			 <form:input id="t2signouttimeid" path="t2signouttime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours22('t2signouttimeid')"/> 
					 <form:errors path="t2signouttime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="t2signoutampm" id="t2signoutampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours22('t2signouttimeid')" onblur="javascript:getworksHours22('t2signouttimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${pmam}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
			 
		 </td>		 
		 
		 <td align="${left}"><form:input path="tluncHours" id="tluncHoursId" Class="flat" style="min-width:50px; max-width:50px" onblur="javascript:getworksHours22('tluncHoursId')"/> 
		     <br> <form:errors path="tluncHours" cssClass="errorMessage" /></td>
		 
		 
		 
		 <td align="${left}"><form:input path="thoursworked" id="thoursworkedid" Class="flat" style="min-width:50px; max-width:50px"/> 
		     <br> <form:errors path="thoursworked" cssClass="errorMessage" /></td>
					
		<td><%-- <form:checkbox path="sotflag" id="sotflagid" name="box1"/></td> --%>
		       <form:select cssClass="flat" path="totflag" id="totflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${otstatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
		 <td><%-- <form:checkbox path="sotflag" id="sotflagid" name="box1"/></td> --%>
		       <form:select cssClass="flat" path="tdtflag" id="tdtflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${dtstatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
	     
	     
	     <td>
		       <form:select cssClass="flat" path="tholidayflag" id="tholidayflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${holidaystatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
	     
	     
	 </tr>

     <tr> <!-- Wednesday  -->
	      <td align="${left}"><form:input id="wednesdayname" path="wednesdayname" cssClass="flat" value="Wednesday" readonly="true" style="min-width:75px; max-width:75px"/> <br> 
			    <form:errors path="wednesdayname" cssClass="errorMessage" /></td>
         <td align="${left}"><form:input id="datepicker4" path="wdate" cssClass="flat" style="width:72px"  onblur="return formatDate4();"/> <br> 
			    <form:errors path="wdate" cssClass="errorMessage" /></td>
	     
	     <td align="${left}" colspan="2">
			<form:input id="wsignintimeid" path="wsignintime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours23('wsignintimeid')"/> 
					<form:errors path="wsignintime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="wsigninampm" id="wsigninampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours23('wsignintimeid')" onblur="javascript:getworksHours23('wsignintimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${ampm}" itemValue="dataText" itemLabel="dataText" />
			 </form:select>
		</td>
		<td align="${left}" colspan="2">
			 <form:input id="wsignouttimeid" path="w_signouttime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours23('wsignouttimeid')"/> 
					 <form:errors path="w_signouttime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="wsignoutampm" id="wsignoutampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours23('wsignouttimeid')" onblur="javascript:getworksHours23('wsignouttimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${pmam}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>			 
		 </td>
		 
		 
		 
		 
		 <td align="${left}" colspan="2">
			<form:input id="w2signintimeid" path="w2signintime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours23('w2signintimeid')"/> 
					<form:errors path="w2signintime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="w2signinampm" id="w2signinampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours23('w2signintimeid')" onblur="javascript:getworksHours23('w2signintimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${ampm}" itemValue="dataText" itemLabel="dataText" />
			 </form:select>
		</td>
		<td align="${left}" colspan="2">
			 <form:input id="w2signouttimeid" path="w2_signouttime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours23('w2signouttimeid')"/> 
					 <form:errors path="w2_signouttime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="w2signoutampm" id="w2signoutampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours23('w2signouttimeid')" onblur="javascript:getworksHours23('w2signouttimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${pmam}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>			 
		 </td>
		 
		 <td align="${left}"><form:input path="wluncHours" id="wluncHoursId" Class="flat" style="min-width:50px; max-width:50px" onblur="javascript:getworksHours23('wluncHoursId')"/> 
		     <br> <form:errors path="wluncHours" cssClass="errorMessage" /></td>
		 
		 
		 <td align="${left}"><form:input path="whoursworked" id="whoursworkedid" Class="flat" style="min-width:50px; max-width:50px"/> 
		     <br> <form:errors path="whoursworked" cssClass="errorMessage" /></td>
					
		<td><%-- <form:checkbox path="sotflag" id="sotflagid" name="box1"/></td> --%>
		       <form:select cssClass="flat" path="wotflag" id="wotflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${otstatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
		 <td><%-- <form:checkbox path="sotflag" id="sotflagid" name="box1"/></td> --%>
		       <form:select cssClass="flat" path="wdtflag" id="wdtflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${dtstatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
	     
	     
	    <td>
		       <form:select cssClass="flat" path="wholidayflag" id="wholidayflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${holidaystatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td> 
	     
	 </tr>
     
	 <tr> <!-- thursday  -->
	      <td align="${left}"><form:input id="thrusdayname" path="thrusdayname" cssClass="flat" value="Thursday" readonly="true"  style="width:75px"/> <br> 
			    <form:errors path="thrusdayname" cssClass="errorMessage" /></td>
         <td align="${left}"><form:input id="datepicker5" path="thdate" cssClass="flat" style="min-width:72px; max-width:72px"  onblur="return formatDate5();"/> <br> 
			    <form:errors path="thdate" cssClass="errorMessage" /></td>
	     
	     <td align="${left}" colspan="2">
			<form:input id="thsignintimeid" path="thsignintime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours24('thsignintimeid')"/> 
					<form:errors path="thsignintime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="thsigninampm" id="thsigninampmid" style="width:50px" onchange="javascript:getworksHours24('thsignintimeid')" onblur="javascript:getworksHours24('thsignintimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${ampm}" itemValue="dataText" itemLabel="dataText" />
			 </form:select>
		</td>
		<td align="${left}" colspan="2">
			 <form:input id="thsignouttimeid" path="thsignouttime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours24('thsignouttimeid')"/> 
					 <form:errors path="thsignouttime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="thsignoutampm" id="thsignoutampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours24('thsignouttimeid')" onblur="javascript:getworksHours24('thsignouttimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${pmam}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
			 
		 </td>
		 
		 
		 
		 <td align="${left}" colspan="2">
			<form:input id="th2signintimeid" path="th2signintime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours24('th2signintimeid')"/> 
					<form:errors path="th2signintime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="th2signinampm" id="th2signinampmid" style="width:50px" onchange="javascript:getworksHours24('th2signintimeid')" onblur="javascript:getworksHours24('th2signintimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${ampm}" itemValue="dataText" itemLabel="dataText" />
			 </form:select>
		</td>
		<td align="${left}" colspan="2">
			 <form:input id="th2signouttimeid" path="th2signouttime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours24('th2signouttimeid')"/> 
					 <form:errors path="th2signouttime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="th2signoutampm" id="th2signoutampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours24('th2signouttimeid')" onblur="javascript:getworksHours24('th2signouttimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${pmam}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
			 
		 </td>
		 
		 <td align="${left}"><form:input path="thluncHours" id="thluncHoursId" Class="flat" style="min-width:50px; max-width:50px" onblur="javascript:getworksHours24('thluncHoursId')"/> 
		     <br> <form:errors path="thluncHours" cssClass="errorMessage" /></td>
		 
		 <td align="${left}"><form:input path="thhoursworked" id="thhoursworkedid" Class="flat" style="min-width:50px; max-width:50px"/> 
		     <br> <form:errors path="thhoursworked" cssClass="errorMessage" /></td>
					
		<td><%-- <form:checkbox path="sotflag" id="sotflagid" name="box1"/></td> --%>
		       <form:select cssClass="flat" path="thotflag" id="thotflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${otstatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
		 <td><%-- <form:checkbox path="sotflag" id="sotflagid" name="box1"/></td> --%>
		       <form:select cssClass="flat" path="thdtflag" id="thdtflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${dtstatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
	     
	     <td>
		       <form:select cssClass="flat" path="thholidayflag" id="thholidayflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${holidaystatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
	     
	     
	     
	 </tr>
	
	 <tr> <!-- friday  -->
	      <td align="${left}"><form:input id="fridayname" path="fridayname" cssClass="flat" value="Friday" readonly="true" style="min-width:75px; max-width:75px"/> <br> 
			    <form:errors path="fridayname" cssClass="errorMessage" /></td>
         <td align="${left}"><form:input id="datepicker6" path="fdate" cssClass="flat" style="min-width:72px; max-width:72px"  onblur="return formatDate6();"/> <br> 
			    <form:errors path="fdate" cssClass="errorMessage" /></td>
	     
	     <td align="${left}" colspan="2">
			<form:input id="fsignintimeid" path="fsignintime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours25('fsignintimeid')"/> 
					<form:errors path="fsignintime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="fsigninampm" id="fsigninampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours25('fsignintimeid')" onblur="javascript:getworksHours25('fsignintimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${ampm}" itemValue="dataText" itemLabel="dataText" />
			 </form:select>
		</td>
		<td align="${left}" colspan="2">
			 <form:input id="fsignouttimeid" path="fsignouttime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours25('fsignouttimeid')"/> 
					 <form:errors path="fsignouttime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="fsignoutampm" id="fsignoutampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours25('fsignouttimeid')" onblur="javascript:getworksHours25('fsignouttimeid')">
			       <%-- <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${pmam}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
			 
		 </td>
		 
		 
		 
		 
		  <td align="${left}" colspan="2">
			<form:input id="f2signintimeid" path="fs2ignintime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours25('f2signintimeid')"/> 
					<form:errors path="fs2ignintime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="f2signinampm" id="f2signinampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours25('f2signintimeid')" onblur="javascript:getworksHours25('f2signintimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${ampm}" itemValue="dataText" itemLabel="dataText" />
			 </form:select>
		</td>
		<td align="${left}" colspan="2">
			 <form:input id="f2signouttimeid" path="f2signouttime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours25('f2signouttimeid')"/> 
					 <form:errors path="f2signouttime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="f2signoutampm" id="f2signoutampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours25('f2signouttimeid')" onblur="javascript:getworksHours25('f2signouttimeid')">
			       <%-- <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${pmam}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
			 
		 </td>
		 
		 
		 <td align="${left}"><form:input path="fluncHours" id="fluncHoursId" Class="flat" style="min-width:50px; max-width:50px" onblur="javascript:getworksHours25('fluncHoursId')"/> 
		     <br> <form:errors path="fluncHours" cssClass="errorMessage" /></td>
		 
		 
		 <td align="${left}"><form:input path="fhoursworked" id="fhoursworkedid" Class="flat" style="min-width:50px; max-width:50px"/> 
		     <br> <form:errors path="fhoursworked" cssClass="errorMessage" /></td>
					
		<td><%-- <form:checkbox path="sotflag" id="sotflagid" name="box1"/></td> --%>
		       <form:select cssClass="flat" path="fotflag" id="fotflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${otstatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
		 <td><%-- <form:checkbox path="sotflag" id="sotflagid" name="box1"/></td> --%>
		       <form:select cssClass="flat" path="fdtflag" id="fdtflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${dtstatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
	     
	     <td>
		       <form:select cssClass="flat" path="fholidayflag" id="fholidayflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${holidaystatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
	     
	 </tr>
	 
	 
	 <tr> <!-- saturday  -->
	      <td align="${left}"><form:input id="stdayname" path="stdayname" cssClass="flat" value="Saturday" readonly="true" style="min-width:75px; max-width:75px"/> <br> 
			    <form:errors path="stdayname" cssClass="errorMessage" /></td>
         <td align="${left}"><form:input id="datepicker7" path="stadate" cssClass="flat" style="min-width:72px; max-width:72px"  onblur="return formatDate7();"/> <br> 
			    <form:errors path="stadate" cssClass="errorMessage" /></td>
	     
	     <td align="${left}" colspan="2">
			<form:input id="stsignintimeid" path="stsignintime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours26('stsignintimeid')"/> 
					<form:errors path="stsignintime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="stsigninampm" id="stsigninampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours26('stsignintimeid')" onblur="javascript:getworksHours26('stsignintimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${ampm}" itemValue="dataText" itemLabel="dataText" />
			 </form:select>
		</td>
		<td align="${left}" colspan="2">
			 <form:input id="stsignouttimeid" path="st_signouttime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours26('stsignouttimeid')"/> 
					 <form:errors path="st_signouttime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="stsignoutampm" id="stsignoutampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours26('stsignouttimeid')" onblur="javascript:getworksHours26('stsignouttimeid')">
			       <%-- <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${pmam}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
			 
		 </td>
		 
		 
		 
		 
		 <td align="${left}" colspan="2">
			<form:input id="st2signintimeid" path="st2signintime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours26('st2signintimeid')"/> 
					<form:errors path="st2signintime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="st2signinampm" id="st2signinampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours26('st2signintimeid')" onblur="javascript:getworksHours26('st2signintimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${ampm}" itemValue="dataText" itemLabel="dataText" />
			 </form:select>
		</td>
		<td align="${left}" colspan="2">
			 <form:input id="st2signouttimeid" path="st2_signouttime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours26('st2signouttimeid')"/> 
					 <form:errors path="st2_signouttime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="st2signoutampm" id="st2signoutampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours26('st2signouttimeid')" onblur="javascript:getworksHours26('st2signouttimeid')">
			       <%-- <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${pmam}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
			 
		 </td>
		 
		 
		 <td align="${left}"><form:input path="stluncHours" id="stluncHoursId" Class="flat" style="min-width:50px; max-width:50px" onblur="javascript:getworksHours26('stluncHoursId')"/> 
		     <br> <form:errors path="stluncHours" cssClass="errorMessage" /></td>
		 
		 
		 
		 <td align="${left}"><form:input path="sthoursworked" id="sthoursworkedid" Class="flat" style="min-width:50px; max-width:50px"/> 
		     <br> <form:errors path="sthoursworked" cssClass="errorMessage" /></td>
					
		<td><%-- <form:checkbox path="sotflag" id="sotflagid" name="box1"/></td> --%>
		       <form:select cssClass="flat" path="stotflag" id="stotflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${otstatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
		 <td><%-- <form:checkbox path="sotflag" id="sotflagid" name="box1"/></td> --%>
		       <form:select cssClass="flat" path="stdtflag" id="stdtflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${dtstatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
	     
	     <td>
		       <form:select cssClass="flat" path="stholidayflag" id="stholidayflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${holidaystatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
	     
	 </tr>
	 
	 
	 
	 <tr> <!-- sunday  -->
	      <td align="${left}"><form:input id="sundayname" path="sundayname" cssClass="flat" value="Sunday" readonly="true"  style="min-width:75px; max-width:75px"/> <br> 
			    <form:errors path="sundayname" cssClass="errorMessage" /></td>
         <td align="${left}"><form:input id="datepicker" path="sdate" cssClass="flat" style="min-width:72px; max-width:72px"  onblur="return formatDate();"/> <br> 
			    <form:errors path="sdate" cssClass="errorMessage" /></td>
	     
	     <td align="${left}" colspan="2">
			<form:input id="ssignintimeid" path="ssignintime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours20('ssignintimeid')"/> 
					<form:errors path="ssignintime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="ssigninampm" id="ssigninampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours20('ssignintimeid')" onblur="javascript:getworksHours20('ssignintimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${ampm}" itemValue="dataText" itemLabel="dataText" />
			 </form:select>
		</td>
		<td align="${left}" colspan="2">
			 <form:input id="ssignouttimeid" path="ssignouttime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours20('ssignouttimeid')"/> 
					 <form:errors path="ssignouttime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="ssignoutampm" id="ssignoutampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours20('ssignouttimeid')" onblur="javascript:getworksHours20('ssignouttimeid')">
			       <%-- <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${pmam}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
			 
		 </td>
		 
		 
		 <td align="${left}" colspan="2">
			<form:input id="s2signintimeid" path="s2signintime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours20('s2signintimeid')"/> 
					<form:errors path="s2signintime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="s2signinampm" id="s2signinampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours20('s2signintimeid')" onblur="javascript:getworksHours20('s2signintimeid')">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${ampm}" itemValue="dataText" itemLabel="dataText" />
			 </form:select>
		</td>
		<td align="${left}" colspan="2">
			 <form:input id="s2signouttimeid" path="s2signouttime"
					cssClass="flat" style="min-width:50px; max-width:50px" onkeypress="return onlyNumbers(event,false)" onblur="javascript:getworksHours20('s2signouttimeid')"/> 
					 <form:errors path="s2signouttime" cssClass="errorMessage" />
					
			    <form:select cssClass="flat" path="s2signoutampm" id="s2signoutampmid" style="min-width:50px; max-width:50px" onchange="javascript:getworksHours20('s2signouttimeid')" onblur="javascript:getworksHours20('s2signouttimeid')">
			       <%-- <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${pmam}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
			 
		 </td>
		 
		 
		 
		 <td align="${left}"><form:input path="sluncHours" id="sluncHoursId" Class="flat" style="min-width:50px; max-width:50px" onblur="javascript:getworksHours20('sluncHoursId')"/> 
		     <br> <form:errors path="sluncHours" cssClass="errorMessage" /></td>
		 
		 
		 <td align="${left}"><form:input path="shoursworked" id="shoursworkedid" Class="flat" style="min-width:50px; max-width:50px"/> 
		     <br> <form:errors path="shoursworked" cssClass="errorMessage" /></td>
					
		<td><%-- <form:checkbox path="sotflag" id="sotflagid" name="box1"/></td> --%>
		       <form:select cssClass="flat" path="sotflag" id="sotflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${otstatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
		 <td><%-- <form:checkbox path="sdtflag" id="sdtflagid" name="box1"/></td> --%>
		       <form:select cssClass="flat" path="sdtflag" id="sdtflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${dtstatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
		 
		 
		 <td>
		       <form:select cssClass="flat" path="sholidayflag" id="sholidayflagid" style="min-width:50px; max-width:50px">
			      <%--  <form:option value="">--Please Select--</form:option>  --%>
				   <form:options items="${holidaystatuss}" itemValue="dataText" itemLabel="dataText" />
			   </form:select>
	     </td>
		 
		 
		 
	 </tr>
	 
	 
	 
	 
	 
	 
	 
	 
	<tr>
	 <td align="${left}" colspan="2"><input type="button"
				name="calculate" id="calculate" onclick="javascript:getTotalworksHours()"
				value="<primo:label code="Calculate"/>" class="flat" /></td>
	 </tr>
	 
      <tr class="table-heading"><td colspan="14"><b><primo:label code="Total Hours" /></b></td></tr>
      
      <tr>
           
          <td class="form-left"><primo:label code="Total Hours" /><span class="errorMessage"></span></td>
         <td align="${left}"><form:input path="hoursworkedInweekRoundedValue" id="hoursworkedInweekRoundedValueid" style="min-width:100px; max-width:100px" 
					Class="flat"/> <br> <form:errors path="hoursworkedInweekRoundedValue" cssClass="errorMessage" /></td>
	   
           
         <td class="form-left" colspan="2"><primo:label code="Actual Hours" /></td>
         <td align="${left}"><form:input path="totalhoursworkedInweek" id="totalhoursworkedInweekid" style="min-width:50px; max-width:50px" 
					Class="flat"/></td>
		 
		 <%-- <td class="form-left"><primo:label code="Round Up Total" /><span class="errorMessage"></span></td>
         <td align="${left}"><form:input path="totalhoursworkedInweekRounded" id="totalhoursworkedInweekRoundedid" style="width:100px" 
					Class="flat"/> <br> <form:errors path="totalhoursworkedInweekRounded" cssClass="errorMessage" /></td>
	   --%>
	     <%--  <td class="form-left"><primo:label code="Total Hours" /><span class="errorMessage"></span></td>
         <td align="${left}"><form:input path="hoursworkedInweekRoundedValue" id="hoursworkedInweekRoundedValueid" style="width:100px" 
					Class="flat"/> <br> <form:errors path="hoursworkedInweekRoundedValue" cssClass="errorMessage" /></td>
	   --%>
	  </tr>			
	
	  <tr>
         <td class="form-left"><primo:label code="Regular Hours" /><span class="errorMessage"></span></td>
         <td align="${left}"><form:input path="regularhours" id="regularhoursid" style="min-width:100px; max-width:100px" 
					Class="flat"/> <br> <form:errors path="regularhours" cssClass="errorMessage" /></td>
					
		 
	  </tr>	
	  
	  
	  <tr>
	  <td class="form-left"><primo:label code="Holiday Hours" /><span class="errorMessage"></span></td>
         <td align="${left}"><form:input path="holidayhours" id="holidayhoursid" style="min-width:100px; max-width:100px" 
					Class="flat"/> <br> <form:errors path="holidayhours" cssClass="errorMessage" /></td>
	  </tr>
	  
	  
	
	  <tr>
         <td class="form-left"><primo:label code="Total OT Hours" /><span class="errorMessage"></span></td>
         <td align="${left}"><form:input path="totalothoursinweek" id="totalothoursinweekid" style="min-width:100px; max-width:100px"   Class="flat"/> 
         <br> <form:errors path="totalothoursinweek" cssClass="errorMessage" /></td>
         
         <%-- <td class="form-left"><primo:label code="Round Up Total" /><span class="errorMessage"></span></td>
         <td align="${left}"><form:input path="totalothoursinweekRounded" id="totalothoursinweekRoundedid" style="width:100px"  Class="flat"/> 
         <br> <form:errors path="totalothoursinweekRounded" cssClass="errorMessage" /></td>
          --%>
	  </tr>
	  
	  <tr>
         <td class="form-left"><primo:label code="Total DT Hours" /><span class="errorMessage"></span></td>
         <td align="${left}"><form:input path="totaldthoursinweek" id="totaldthoursinweekid" style="min-width:100px; max-width:100px" 
					Class="flat"/> <br> <form:errors path="totaldthoursinweek" cssClass="errorMessage" /></td>
		
		<%-- <td class="form-left"><primo:label code="Round Up Total" /><span class="errorMessage"></span></td>
         <td align="${left}"><form:input path="totaldthoursinweekRounded" id="totaldthoursinweekRoundedid" style="width:100px"
					Class="flat"/> <br> <form:errors path="totaldthoursinweekRounded" cssClass="errorMessage" /></td>
	 			 --%>
		  			
	  </tr>
	  <c:if test="${modelObject.mode eq 'ADD_TO_PREV' || modelObject.sequenceNum1 != null}">
		  <tr>
	         <td class="form-left"><primo:label code="Seq Num" /><span class="errorMessage">*</span></td>
	         <td align="${left}">
	         	<form:input path="sequenceNum1" id="sequenceNum1" style="min-width:100px; max-width:100px" 
						class="flat"/> 
				<br><form:errors path="sequenceNum1" cssClass="errorMessage" />
			 </td>
		  </tr>
	  </c:if>
	  
	  <tr>
  <td class="form-left"><primo:label code="Notes" /><span
				class="errorMessage"></span></td>
	<td align="${left}" colspan="5">
				<form:textarea path="notes" rows="2" cols="69"/>    	
			</td>
</tr>
	
		
	<tr><td colspan="2"></td></tr>
		<tr><!-- <td>&nbsp;</td> -->
			<td align="${left}" colspan="2"><input type="submit"
				name="create" id="create" onclick="javascript:processForm();"
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
getworksHours0();
getworksHours1();
getworksHours2();
getworksHours3();
getworksHours4();
getworksHours5();
getworksHours6();
getTime('ssignintimeid');
getTime('ssignouttimeid');

getTime('msignintimeid');
getTime('msignouttimeid');

getTime('tsignintimeid');
getTime('tsignouttimeid');

getTime('wsignintimeid');
getTime('wsignouttimeid');

getTime('thsignintimeid');
getTime('thsignouttimeid');

getTime('fsignintimeid');
getTime('fsignouttimeid');

getTime('stsignintimeid');
getTime('stsignouttimeid');

getTotalworksHours();
</script>
	
