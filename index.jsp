<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Sentiment Analysis with Twitter Home</title>
<link href="css/styles.css" rel="stylesheet" type="text/css" />
<script src="js/jquery.js" type="text/javascript"></script>
<script src="js/modalPopup.js" type="text/javascript"></script>
<script src="js/jquery-1.4.min.js"></script>
<script src="js/jquery.speedometer.js"></script>
<script src="js/jquery.jqcanvas-modified.js"></script>
<script src="js/excanvas-modified.js"></script> 

<script type="text/javascript">

function execCleanUp(){
	$.ajax({
        type: "POST",
        url: './CleanUpServlet',
        dataType: "html",
        success: function(data) {
            return data;
        },
        error: function() {
            alert('Error occured. Re-run the process..');
        }
    });
}

function triggerHadoopJob(){
	$('#loading_image1').show();
	$("#jobStatus").hide();
	$('span.back').hide();
	$('span.buttonBg').hide();
	$.ajax({
        type: "POST",
        url: './MRService',
        dataType: "html",
        success: function(data) {
        	$("#jobStatus").show();
        	$('#loading_image1').hide();
        	$("#jobStatus").html(data);
        	$('span.back').show();
        	$('span.buttonBg').show();
            return data;
        },
        error: function() {
        	$('#loading_image1').hide();
        	$('span.back').show();
        	$('span.buttonBg').show();
            alert('Error occured. Re-run the process..');
        }
    });
}

function ShowReport(){
	$.ajax({
        type: "POST",
        url: './ReportServlet',
        dataType: "html",
        success: function(data) {
        	$("#displayReport").html(data);
            return data;
        },
        error: function() {
            alert('Error occured while generating report..');
        }
    });
}

/* function triggerHadoopForm()
{
	document.getElementById("trigger_hadoop").submit();
}

function ReportForm(){
	document.getElementById("show_report").submit();
} */

$(document).ready(function(){
  $(".buttonBg").click(function(){	
	var CurrentDivId = $(this).parents().find(".currentDiv").attr('id');	
	var SerialNo = CurrentDivId.substring(3);
	$("#"+CurrentDivId).removeClass('currentDiv');
	$("#"+CurrentDivId).addClass('hideDiv');
	var NewDivID = "div"+ (parseInt(SerialNo)+1);
	if($(this).attr('id') == "btnFinish")
	{
	    $('#mask').hide();
		$('.window').hide();
		NewDivID = "div1";
		$('.chkBox').find('input').attr('checked',false);	
		$("#GoToDemo a").attr('name',"");
	    $("#GoToDemo a img").attr('src','image/goToDemoDisable.png');	
	}
	
	$("#"+NewDivID).addClass('currentDiv');
	$("#"+NewDivID).removeClass('hideDiv');	
 });
 
$(".back").click(function(){
   var backDivId = $(this).parents().find(".currentDiv").attr('id');
   var backSerialNo = backDivId.substring(3);
   var DivID1 = "div"+ (parseInt(backSerialNo)-1);
   $("#"+backDivId).removeClass('currentDiv');
   $("#"+backDivId).addClass('hideDiv');
   
   $("#"+DivID1).addClass('currentDiv');
   $("#"+DivID1).removeClass('hideDiv');	
 });

 
 $('.chkBox').each(function(){	
	$(this).find('input').click(function(){
	 if($(this).attr('checked')){
	  //$('.chkBox').find('input').attr('checked',false);
	  $(this).attr('checked',true);
	  $("#GoToDemo a").attr('name',"modal");
	  $("#GoToDemo a img").attr('src','image/goToDemo.png');	
	 }
	 else
	 {
	  $("#GoToDemo a").attr('name',"");
	  $("#GoToDemo a img").attr('src','image/goToDemoDisable.png');	
	 }	 
	});
  });
 
 $("#GoToDemo").click(function(){
  if($(this).find('a').attr('name') == "")
  {
   alert("Please check a checkbox to proceed");
  }
 })
 
 $("marquee").hover(function () { 
    this.stop();
	}, function () {
    this.start();
 });
 
 });

</script>

</head>

<body>
<div id="wrapper">
	<div class="fullWidth">
      <div class="topLftCorner"></div>
      <div class="topMidbg">
        <div style="width:25%; text-align:center; margin-top:50px; float:left"><img src="image/RSLogo.png" alt="" width="100px" height="80px" /></div>
        <div class="floatLft"><img src="image/divider.jpg" alt="" /></div>
        <div class="heading">
        <h4>OVERVIEW</h4>
        <div class="txt1"><b>This is a Big Data sentiment analytics demo based on Apache Hadoop framework and social media (Twitter) data.</b></div></div>
      </div>
      <div class="topRtCorner"></div>
    </div>
    <div class="brdCrumb"><img src="image/step1.png" alt="" /></div>
<div class="fullWidth">
      <div class="greyBg">
        <div class="iconContainer">
          <div class="txt4"><strong>Select Social Media</strong></div>
          <div class="chkBox"><input name="" type="checkbox" value="" /></div>
          <div class="iconAlign"><img src="image/facebook.png" alt="" /></div>
          <div class="chkBox"><input name="" type="checkbox" value="" /></div>
          <div class="iconAlign"><img src="image/twitter.png" alt="" /></div>
          <div class="chkBox"><input name="" type="checkbox" value="" /></div>
          <div class="iconAlign"><img src="image/amazon.png" alt="" /></div>
          <div class="chkBox"><input name="" type="checkbox" value="" /></div>
          <div class="iconAlign"><img src="image/linkedIn.jpg" alt="" /></div>
        </div>
        <div style="float:right; margin:180px 20px 0 0" id="GoToDemo">
        <a href="#dialog" name=""><img src="image/goToDemoDisable.png" alt="" /></a></div>
      </div>
    </div>
    <div class="fullWidth">
      <div class="lftCrnrBot"></div>
      <div class="botMidbg"></div>
      <div class="rtCrnrBot"></div>
    </div>
</div>

<!-- Modal Popup Content --->
<div id="boxes">

<div id="dialog" class="window">
 
 <!--div class="fullWidth floatLft currentDiv" id="div1"> 
  
  <div class="floatLft"><img src="image/step2.png" alt="" /></div>

<div class="divScroll">
     <div class="divContainer">
       <div class="iconCont"><img src="image/twitterBig.png" alt="" /></div>
        <div class="txtCont">
        <div class="heading1">Name of Topic</div>
        <div class="txt2">Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</div></div>
      </div>
      
     <div class="divContainer">
       <div class="iconCont"><img src="image/twitterBig.png" alt="" /></div>
        <div class="txtCont">
        <div class="heading1">Name of Topic</div>
        <div class="txt2">Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</div></div>
      </div>

     <div class="divContainer">
       <div class="iconCont"><img src="image/twitterBig.png" alt="" /></div>
        <div class="txtCont">
        <div class="heading1">Name of Topic</div>
        <div class="txt2">Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</div></div>
      </div>

     <div class="divContainer">
       <div class="iconCont"><img src="image/twitterBig.png" alt="" /></div>
        <div class="txtCont">
        <div class="heading1">Name of Topic</div>
        <div class="txt2">Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</div></div>
      </div>

     <div class="divContainer">
       <div class="iconCont"><img src="image/twitterBig.png" alt="" /></div>
        <div class="txtCont">
        <div class="heading1">Name of Topic</div>
        <div class="txt2">Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</div></div>
      </div>
</div>
	<div class="fullWidth">
      <div style="float:right; margin-top:15px;"><span class="buttonBg" id="button1">Next Step</span></div>
    </div>
    
</div-->    

<div class="fullWidth floatLft currentDiv"  id="div1">
    <div class="floatLft"><img src="image/step2.png" alt="" /></div>
    <div class="listCont" >
      <div class="txt3"><strong>Enter Keyword</strong></div>
      <div class="fullWidth">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td class="txt5" width="20%">Brand</td>
        <td align="left" width="80%">
        <select>
  			<option value="casino" selected="selected">Casino</option>
  			<option value="reebok">Reebok</option>
  			<option value="mercedes">Mercedes</option>
  			<option value="sony">Sony</option>
		</select>
		</td>
      </tr>
      <tr>
        <td class="txt5" width="20%">Keyword</td>
        <td align="left" width="80%"><input type="" name="" id="" style="width:280px" class="inputTxt" value="Belagio"/></td>
      </tr>
      <!-- <tr>
        <td class="txt5">Keyword 2</td>
        <td align="left"><input type="" name="" id="" style="width:280px" class="inputTxt"/></td>
      </tr>
      <tr>
        <td class="txt5">Keyword 3</td>
        <td align="left"><input type="" name="" id="" style="width:280px" class="inputTxt"/></td>
      </tr>  -->
      <tr>
        <td colspan="2" class="txt6" height="50px"><strong>Note :</strong> Keyword can be anything like 'Hadoop', 'Airlines' etc.</td>
      </tr>
      <tr>
        <td colspan="2" height="10px"></td>
      </tr>
    </table>
	</div>
    </div>
    <div class="fullWidth">
      <div style="float:right; margin-top:15px; margin-right:12px;"><span class="buttonBg" onclick="triggerHadoopJob()">Start</span></div>
    </div>
</div>

<div class="fullWidth floatLft hideDiv" id="div2">
  <div class="floatLft"><img src="image/step3.png" alt="" /></div>
  <div class="tweetHeading">Job Status</div>
	<div class="floatLft" style="margin:6px 0 10px 0; width:640px;height: 300px">
    	<img src="./image/gif-load.gif" style="display: none;" id="loading_image1" />
    	<div id="jobStatus"> 

		</div>	
    </div>
	<div class="fullWidth">
      <div style="float:right; margin-top:3px; margin-right:12px;">
      <span class="back">Back</span>&nbsp;<span class="buttonBg" onclick="ShowReport()">Show Report</span></div>
    </div>
</div>

<div class="fullWidth floatLft hideDiv" id="div3">
  <div class="floatLft"><img src="image/step4.png" alt="" /></div>
   <div class="tweetHeading">Sentiment Analysis Report</div>
	<div class="floatLft" style="margin:6px 0 10px 0; width:640px;height: 300px">
   		<div id="displayReport"> 
		
		</div>
	</div>	
	<div class="fullWidth">
      <div style="float:right; margin-top:6px; margin-right:12px;">
      <span class="back">Back</span>&nbsp;<span class="buttonBg" id="btnFinish" onclick="execCleanUp()">Finish</span></div>
    </div>

<!-- <div class="fullWidth floatLft hideDiv" id="div4">
  <div class="floatLft"><img src="image/step5.png" alt="" /></div>
	<div style="margin:45px 0 15px 0;border:1px solid #d3d3d5;"><img src="image/hadoopServerRoles.jpg" alt="" /></div>
	<div class="fullWidth">
      <div style="float:right; margin-top:10px; margin-right:12px;">
      <form id="trigger_hadoop" name="trigger_hadoop" action="./MRService">
      	<span class="back">Back</span>&nbsp;<span class="buttonBg" onclick="triggerHadoopForm()">Next Step</span></div>
      </form>
    </div>
</div>

<form id="show_report" name="show_report" action="./ReportServlet">
<div class="fullWidth floatLft hideDiv" id="div5">
  <div class="floatLft"><img src="image/step6.png" alt="" /></div>
	<div style="margin:45px 0 15px 0;border:1px solid #d3d3d5;"><img src="image/outputStorage.jpg" alt="" /></div>
	<div class="fullWidth">
      <div style="float:right; margin-top:10px; margin-right:12px;">
      	<span class="back" >Back</span>&nbsp;<span class="buttonBg" onclick="ReportForm()">Next Step</span></div>
    </div>
</div>
<div class="fullWidth floatLft hideDiv" id = "div6">
    <div class="floatLft"><img src="image/step7.png" alt="" /></div>
    <div class="listCont" >
    <div class="fullWidth floatLft" style="margin:25px 0 30px 0;">
    	<img src="image/twitterAnalysis.jpg" alt="" /></div>
    </div>
    <div class="fullWidth">
      <div style="float:right; margin-top:15px; margin-right:12px;">
      <span class="back" >Back</span>&nbsp;<span class="buttonBg" id = "btnFinish">Finish</span></div>
    </div>
</div>
</form> -->

</div> 
</div>
</div> 
<div id="mask"></div>

</div>
<!-- Modal Popup Content End --->




</body>
</html>
