<%@ page contentType="text/html;charset=UTF-8" %>
<g:setProvider library="jquery"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="ncs" />
    <g:set var="entityName" value="${message(code: 'receipt.label', default: 'Receipt Items')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>

    <g:javascript src="ncs-case-management.js" />

  </head>
  <body>
    <div class="nav">
          <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
    </div>
    <div class="body">
      <h1>Receipt Items</h1>

      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>

      <g:form>
	<div class="prop">
	  <span class="name">
		<label for="item"><g:message code="receiptItems.item.label" default="Item to receipt" /></label>
	  </span>
	  <span class="value">
		<g:textField name="item" size="15" value="" />
	  </span>
	</div>
        <input type="button" onClick="addDoc(this);" value="Test"/>
       <!--  <g:submitToRemote before="addDoc(this);" /> -->
       </g:form>


      <div id="updatedArea"></div>

    </div>
  </body>
</html>