<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Show Report</title>
<script src="js/jquery-1.4.min.js"></script>
<script src="js/jquery.speedometer.js"></script>
<script src="js/jquery.jqcanvas-modified.js"></script>
<script src="js/excanvas-modified.js"></script> 
<script>
	$(function() {
		$('#speedDiv1').speedometer();
	});
</script>
</head>
<body>
<div id="speedometer1" style="padding: 10px 0 10px 10px" align="center">
	<div id="speedDiv1"> <%=session.getAttribute("totalSentiment")%> </div>						
</div>
</body>
</html>