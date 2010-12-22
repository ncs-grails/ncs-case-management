<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Enumerate Roles of Currently Logged In User</title>
  </head>
  <body>
    <h1>What Roles Do I (${username}) Have?</h1>
    <ul>
      <g:each var="r" in="${roles}">
        <li>${r}</li>
      </g:each>
    </ul>
  </body>
</html>
