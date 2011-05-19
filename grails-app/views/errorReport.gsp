<%@ page contentType="text/html"%>
<html>
<head>
<title>Error Report - National Children's Study</title>
</head>
<body>
<h1 style="color: maroon;">NCS Error Report</h1>
<pre>
Source Address: ${remoteAddr}
User: ${username}
Time: ${now}
</pre>
<hr />
<pre>${errorMessage}</pre>
<hr />
<div id="footer" style="font-size: 0.6em; color: gray;"><span>This
report came from "ncs-case-management".</span></div>
</body>
</html>
