<%@ page import="edu.umn.ncs.BatchCreationConfig" %>
<%@ page import="edu.umn.ncs.Instrument" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="ncs" />
  <g:set var="entityName" value="${message(code: 'batchCreationConfig.label', default: 'BatchCreationConfig')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
  <script type="text/javascript">
        $(function() {
                $("#tabs").tabs();
                $("#doctabs").tabs();
                $("#itemtabs").tabs();
        });
  </script>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1>Edit Mailing Configuration</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${batchCreationConfigInstance}">
      <div class="errors">
        <g:renderErrors bean="${batchCreationConfigInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${batchCreationConfigInstance?.id}" />
      <g:hiddenField name="version" value="${batchCreationConfigInstance?.version}" />
      <div class="dialog">
        <g:include action="form" model="${[batchCreationConfigInstance: batchCreationConfigInstance]}" />
      </div>
      <div class="buttons">
        <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
        <span class="button"><g:actionSubmit class="list" action="list" value="${message(code: 'default.button.cancel.label', default: 'Cancel')}" /></span>
      </div>
    </g:form>

    <!-- Documents -->
    <div class="dialog">
      <div id="doctabs">
        <ul>
          <g:each var="d" in="${batchCreationConfigInstance.documents}">
            <li><a href="#dtabs-${d.id}">${new File(d.documentLocation).name}</a></li>
          </g:each>
          <li><a href="#dtabs-new">New</a></li>
        </ul>


        <!-- Form goes here -->
        <g:each var="d" in="${batchCreationConfigInstance.documents}">
          <div id="dtabs-${d.id}">
            <g:form method="post" controller="batchCreationDocument" >
              <g:hiddenField name="id" value="${d?.id}" />
              <g:hiddenField name="version" value="${d?.version}" />

              <div class="prop">
                <span class="name">
                  <label for="documentLocation"><g:message code="batchCreationDocument.documentLocation.label" default="Document Location"/></label>
                </span>
                <span class="value">
                  <g:textField size="80" name="documentLocation" value="${d.documentLocation}" />
                </span>
              </div>

              <div class="prop">
                <span class="name">
                  <label for="mergeSourceFile"><g:message code="batchCreationDocument.mergeSourceFile.label" default="Merge Data Location" /></label>
                </span>
                <span class="value">
                  <g:textField size="80" name="mergeSourceFile" value="${d.mergeSourceFile}" />
                </span>
              </div>

              <div class="prop">
                <span class="name">
                  <label for="mergeSourceQuery"><g:message code="batchCreationDocument.mergeSourceQuery.label" default="Merge Source Query" /></label>
                </span>
                <span class="value">
                  <g:textArea name="mergeSourceQuery" rows="3" cols="80">${d.mergeSourceQuery}</g:textArea>
                </span>
              </div>

              <g:actionSubmit action="update" value="Save" />
              <g:actionSubmit action="delete" value="Remove" />
            </g:form>
          </div>
        </g:each>

        <div id="dtabs-new">
          <g:form method="post" controller="batchCreationDocument" >
            <g:hiddenField name="batchCreationConfig.id" value="${batchCreationConfigInstance?.id}" />

            <div class="prop">
              <span class="name">
                <label for="documentLocation"><g:message code="batchCreationDocument.documentLocation.label" default="Document Location"/></label>
              </span>
              <span class="value">
                <g:textField size="80" name="documentLocation" value="" />
              </span>
            </div>

            <div class="prop">
              <span class="name">
                <label for="mergeSourceFile"><g:message code="batchCreationDocument.mergeSourceFile.label" default="Merge Data Location" /></label>
              </span>
              <span class="value">
                <g:textField size="80" name="mergeSourceFile" value="" />
              </span>
            </div>

            <div class="prop">
              <span class="name">
                <label for="mergeSourceQuery"><g:message code="batchCreationDocument.mergeSourceQuery.label" default="Merge Source Query" /></label>
              </span>
              <span class="value">
                <g:textArea name="mergeSourceQuery" rows="3" cols="80"></g:textArea>
              </span>
            </div>

            <g:actionSubmit controller="batchCreationDocument" action="save" value="Add" />
          </g:form>
        </div>
      </div>
    </div>
    <!-- /Documents -->

    <!-- Items -->
    <div class="dialog">
      <div id="itemtabs">
        <ul>
          <g:each var="i" in="${batchCreationConfigInstance.subItems}">
            <li><a href="#itabs-${i.id}">${i.instrument?.name}</a></li>
          </g:each>
          <g:if test="${unusedInstruments}">
            <li><a href="#itabs-new">New</a></li>
          </g:if>
        </ul>

        <!-- Form goes here -->
        <g:each var="i" in="${batchCreationConfigInstance.subItems}">
          <div id="itabs-${i.id}">
            <g:form method="post" controller="batchCreationItem" >
              <g:hiddenField name="i_id" value="${i?.id}" />

              <div class="prop">
                <span class="name">
                  <label for="instrument"><g:message code="batchCreationItem.instrument.label" default="Instrument"/></label>
                </span>
                <span class="value">
                  <g:select name="instrument.id" from="${unusedInstruments + [i.instrument]}" optionKey="id" value="${i.instrument?.id}"  />
                </span>
              </div>

              <div class="prop">
                <span class="name">
                  <label for="format"><g:message code="batchCreationItem.format.label" default="Format" /></label>
                </span>
                <span class="value">
                  <g:select name="format.id" from="${instrumentFormatInstanceList}" optionKey="id" value="${i.format?.id}"  />
                </span>
              </div>

              <div class="prop">
                <span class="name">
                  <label for="direction"><g:message code="batchCreationItem.direction.label" default="Direction" /></label>
                </span>
                <span class="value">
                  <g:select name="direction.id" from="${edu.umn.ncs.BatchDirection.list()}" optionKey="id" value="${i.direction?.id}"  />
                </span>
              </div>

              <div class="prop">
                <span class="name">
                  <label for="relation.id"><g:message code="BatchCreationItemRelation.label" default="Relation:" /></label>
                </span>
                <span class="value">
                  <g:select name="relation.id" from="${edu.umn.ncs.BatchCreationItemRelation.list()}" optionKey="id" value="${i.relation?.id}" />
                </span>

                <label for="parentInstrument"><g:message code="batchCreationItem.childOf.label" default="of" /></label>
                <span class="value">
                  <g:select name="parentInstrument.id" from="${attachableInstruments - [i.instrument]}" optionKey="id" value="${i.parentInstrument?.id}" />
                </span>
              </div>

              <g:actionSubmit action="update" value="Save" />
              <g:actionSubmit action="delete" value="Remove" />
            </g:form>
          </div>
        </g:each>

        <g:if test="${unusedInstruments}">
          <div id="itabs-new">
            <g:form method="post" controller="batchCreationItem" >
              <g:hiddenField name="batchCreationConfig.id" value="${batchCreationConfigInstance?.id}" />

              <div class="prop">
                <span class="name">
                  <label for="instrument"><g:message code="batchCreationItem.instrument.label" default="Instrument" /></label>
                </span>
                <span class="value">
                  <g:select name="instrument.id" from="${unusedInstruments}" optionKey="id" value=""  />
                </span>
              </div>

              <div class="prop">
                <span class="name">
                  <label for="format"><g:message code="batchCreationItem.format.label" default="Format" /></label>
                </span>
                <span class="value">
                  <g:select name="format.id" from="${instrumentFormatInstanceList}" optionKey="id" value=""  />
                </span>
              </div>

              <div class="prop">
                <span class="name">
                  <label for="direction"><g:message code="batchCreationItem.direction.label" default="Direction" /></label>
                </span>
                <span class="value">
                  <g:select name="direction.id" from="${edu.umn.ncs.BatchDirection.list()}" optionKey="id" value=""  />
                </span>
              </div>


              <div class="prop">
                <span class="name">
                  <label for="relation.id"><g:message code="BatchCreationItemRelation.label" default="Relation:" /></label>
                </span>
                <span class="value">
                  <g:select name="relation.id" from="${edu.umn.ncs.BatchCreationItemRelation.list()}" optionKey="id" />
                </span>

                <label for="parentInstrument"><g:message code="batchCreationItem.childOf.label" default="of" /></label>
                <span class="value">
                  <g:select name="parentInstrument.id" from="${attachableInstruments}" optionKey="id" />
                </span>
              </div>

              <g:actionSubmit controller="batchCreationItem" action="save" value="Add" />
            </g:form>
          </div>
        </g:if>
        <!-- /Items -->

      </div>
    </div>

  </div>
</body>
</html>
