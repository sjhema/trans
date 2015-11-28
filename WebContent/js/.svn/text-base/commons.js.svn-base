function ValidateFileType(fileId)
 {
	var image =document.getElementById(fileId).value;
		if(image!=''){
	    var checkimg = image.toLowerCase();
	    	if (!checkimg.match(/(\.jpg|\.png|\.JPG|\.PNG|\.jpeg|\.JPEG|\.GIF|\.gif|\.html|\.HTML)$/)){
		  		document.getElementById("image").value = null;
				alert("Only JPG,PNG,JPEG,GIF,HTML  files are allowed !");
				document.getElementById("image");
				return false;
				   }		
				 }
	return true;
 }

function removeData() {
	var inputs = document.getElementsByName("id");
	var submitForm = false;
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs[i].checked == true) {
			submitForm = true;
		}
	}
	if (submitForm) {
		if(confirm("Do you want to delete the selected record(s)?")) {
			document.deleteForm.action = "bulkdelete.do";
			document.deleteForm.submit();
		}
	} else {
		alert("Please select at least one record");
	}
}
				
function openWindow(url,title){
	 document.getElementById("iframe").src=url;
	 $('#popUpWindow').AeroWindow({
	      WindowTitle:          title,
	      WindowPositionTop:    'center',
	      WindowPositionLeft:   'center',
	      WindowWidth:          700,
	      WindowHeight:         300,
	      WindowAnimationSpeed: 1000,
	      WindowAnimation:      'easeOutCubic',
	      WindowResizable:      true,
	      WindowDraggable:      true,
	      WindowMinimize:       false,
	      WindowMaximize:       true,
	      WindowClosable:       true   
	              
	    });
}

$(function() {
	$( "#datepicker" ).datepicker({
		dateFormat: 'mm-dd-yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	$( "#datepicker1" ).datepicker({
		dateFormat: 'mm-dd-yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	$( "#datepicker2" ).datepicker({
		dateFormat: 'mm-dd-yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	$( "#datepicker3" ).datepicker({
		dateFormat: 'mm-dd-yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	$( "#datepicker4" ).datepicker({
		dateFormat: 'mm-dd-yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	
	$( "#datepicker5" ).datepicker({
		dateFormat: 'mm-dd-yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	
	$( "#datepicker6" ).datepicker({
		dateFormat: 'mm-dd-yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	$( "#datepicker7" ).datepicker({
		dateFormat: 'mm-dd-yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	
});

function confirmDelete(url) {
	if(confirm("Do you want to delete the selected record ?")) {
		document.location = url;
	}	
}

function hideTr( objId ){
	var obj =  document.getElementById( objId );
	if( obj != null )
		obj.style.display="none";
}

function showTr( objId ){
	var obj =  document.getElementById( objId );
	if( obj != null )
		obj.style.display="table-row";
}

function enableButton(elementId) {
	var obj = document.getElementById(elementId);
	if(obj != null)
		obj.disabled = false;
}

function updateParentWindowInputValue( objId, value ){
	var obj =  top.document.getElementById( objId );
	if( obj != null )
		obj.value=value;
}

function checkUncheck(currObj, objId) {
    var checked;
    if (currObj.checked==true){
        checked=true;
    }
    else {
        checked=false;
    }
    var obj = document.getElementsByName(objId);
    if (obj==null)
        return;
    if (obj.length == 1 && obj.disabled != true) {
        obj.checked = checked;
    }
    else {
        for(var i=0; i< obj.length; i++) {
	         if(obj[i].disabled != true)
            	obj[i].checked=checked;
        }
    }
}

function popup(url) {
 	params  = 'width='+screen.width/2;
 	params += ', height='+screen.height/2;
 	params += ', titlebar=no';
 	params += ', scrollbars=yes';
 	params += ', resizable=yes';
 	params += ', toolbar=no';
 	params += ', location=no';
 	params += ', menubar=no';
 	params += ', directories=no';
 	params += ', status=yes';
 	params += ', top='+screen.height/4;
 	params += ', left='+screen.width/4;
 	newwin=window.open(url,'Print Window', params);
 	if (window.focus) {
	 	newwin.focus();
	 	}
 	return false;
}

GB_AdShow = function(caption, url,height, width, callback_fn) {
	
    var options = {
        caption: caption,
        height: height || 500,
        width: width || 950,
        fullscreen: false,
        show_loading: true,
        center_win:true,
        callback_fn: callback_fn
    };
    var win = new GB_Window(options);
    return win.show(url);
   
};

/**
 * Function to allow only numbers and/or doubles
 * @param evt window's event
 * @return
 */
function onlyNumbers(evt, dec) {
	var theEvent = evt || window.event;
	var key = theEvent.keyCode || theEvent.which;
	var keychar = String.fromCharCode(key);
	// allow decimal if dec is true
	if (dec && (keychar == "."))
		return true;
	if (key > 31 && (key < 48 || key > 57))
		return false;
	return true;
}

//Checks a string to see if it in a valid date format
//of (M)M/(D)D/(YY)YY and returns true/false
function isValidDate(s) {
 // format D(D)/M(M)/(YY)YY
 var dateFormat = /^\d{1,2}[\.|\/|-]\d{1,2}[\.|\/|-]\d{1,4}$/;

 if (dateFormat.test(s)) {
     // remove any leading zeros from date values
     s = s.replace(/0*(\d*)/gi,"$1");
     var dateArray = s.split(/[\.|\/|-]/);
   
     // correct month value
     dateArray[0] = dateArray[0]-1;

     // correct year value
     if (dateArray[2].length<4) {
         // correct year value
         dateArray[2] = (parseInt(dateArray[2]) < 50) ? 2000 + parseInt(dateArray[2]) : 1900 + parseInt(dateArray[2]);
     }

     var testDate = new Date(dateArray[2], dateArray[0], dateArray[1]);
     if (testDate.getDate()!=dateArray[1] || testDate.getMonth()!=dateArray[0] || testDate.getFullYear()!=dateArray[2]) {
         return false;
     } else {
         return true;
     }
 } else {
     return false;
 }
}


/**
 * Function for Editable Date
 */
function formatReportDate(d1){
	var date1=document.getElementById(d1).value;
	if(date1!="") {
		if (date1.length>=8){
			var str=new String(date1);
			if(!str.match("-")){
				var mm=str.substring(0,2);
				var dd=str.substring(2,4);
				var yy=str.substring(4,8);
				var date1=mm+"-"+dd+"-"+yy;
				document.getElementById(d1).value=date1;
			}
		}
		else {
			alert("Invalid date");
			document.getElementById(d1).focus();
		}
	}
}


function formatDate(datepicker){
	var date=document.getElementById(datepicker).value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById(datepicker).value="";
		return true;
	}
	else{
		var str=new String(date);
		if(!str.match("-")){
			var mm=str.substring(0,2);
			var dd=str.substring(2,4);
			var yy=str.substring(4,8);
			var enddigit=str.substring(6,8);
			if(mm>12){
				alert("invalid date format");
				document.getElementById(datepicker).value="";
				return true;
			}
			if(!enddigit==00 && enddigit%4==0 ){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById(datepicker).value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById(datepicker).value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById(datepicker).value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById(datepicker).value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById(datepicker).value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById(datepicker).value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById(datepicker).value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById(datepicker).value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById(datepicker).value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById(datepicker).value=date;
		}
	 }
   }
}
