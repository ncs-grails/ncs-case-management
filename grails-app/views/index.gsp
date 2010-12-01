<html>
  <head>
    <title>Case Management - National Children's Study</title>
    <meta name="layout" content="ncs" />
    <style type="text/css" media="screen">

      #nav {
        margin-top:20px;
        margin-left:30px;
        width:228px;
        float:left;

      }
      .homePagePanel * {
        margin:0px;
      }
      .homePagePanel .panelBody ul {
        list-style-type:none;
        margin-bottom:10px;
      }
      .homePagePanel .panelBody h1 {
        text-transform:uppercase;
        font-size:1.1em;
        margin-bottom:10px;
      }
      .homePagePanel .panelBody {
        background: url(images/leftnav_midstretch.png) repeat-y top;
        margin:0px;
        padding:15px;
      }
      .homePagePanel .panelBtm {
        background: url(images/leftnav_btm.png) no-repeat top;
        height:20px;
        margin:0px;
      }

      .homePagePanel .panelTop {
        background: url(images/leftnav_top.png) no-repeat top;
        height:11px;
        margin:0px;
      }
      h2 {
        margin-top:15px;
        margin-bottom:15px;
        font-size:1.2em;
      }
      #pageBody {
        margin-left:280px;
        margin-right:20px;
      }
    </style>
  </head>
  <body>
    <div id="nav">
      <div class="homePagePanel">
        <div class="panelTop"></div>
        <div class="panelBody">
          <h1>Application Status</h1>
          <ul>
            <li>App version: <g:meta name="app.version"></g:meta></li>
            <li>Grails version: <g:meta name="app.grails.version"></g:meta></li>
            <li>Groovy version: ${org.codehaus.groovy.runtime.InvokerHelper.getVersion()}</li>
            <li>JVM version: ${System.getProperty('java.version')}</li>
            <li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
            <li>Domains: ${grailsApplication.domainClasses.size()}</li>
            <li>Services: ${grailsApplication.serviceClasses.size()}</li>
            <li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
          </ul>
          <h1>Installed Plugins</h1>
          <ul>
            <g:set var="pluginManager"
                   value="${applicationContext.getBean('pluginManager')}"></g:set>

            <g:each var="plugin" in="${pluginManager.allPlugins}">
              <li>${plugin.name} - ${plugin.version}</li>
            </g:each>

          </ul>
        </div>
        <div class="panelBtm"></div>
      </div>
    </div>
    <div id="pageBody">
      <h1>Welcome to the National Children's Study</h1>

      <p>This application is available for generating and tracking study
			documents and tracking the location of study participants.</p>

      <div id="pageList" class="dialog">
        <h2>Configuration</h2>
        <dl class="menu">
          <dt><g:link controller="instrument" action="list">Instruments</g:link></dt>
          <dd>
				  Manage the instruments used to interact with study subjects.
				  Instruments include such items as invitations, surveys, phone
				  calls, events, etc...
          </dd>
          <dt><g:link controller="instrumentHistory" action="list">Instrument Version Tracking</g:link></dt>
          <dd>
				  Manage instrument comments, revisions, and approvals.
          </dd>
          <dt><g:link controller="batchCreationConfig" action="list">Configure Batch Generation</g:link></dt>
          <dd>
				  Configure the way that batches of instruments are generated
				  in the system so that they can be properly tracked
          </dd>
          <dt><g:link controller="mailingSchedule" action="list">Tweak Mailing Schedule</g:link></dt>
		  <dd>
            Manage mailing dates and quota for the instrument.
		  </dd>
        </dl>

        <h2>Data Creation / Entry</h2>
        <dl class="menu">
          <dt><g:link controller="documentGeneration">Document Generation</g:link></dt>
          <dd>
			Generate instrument batches either automatically or by hand.
          </dd>
          <dt><g:link controller="receiptItems" action="index">Receipt Items</g:link></dt>
          <dd>
			Receipt documents
          </dd>
          <dt><g:link controller="batch" action="entry">Mail Date Entry</g:link></dt>
          <dd>
			Enter mail dates for generated batches
          </dd>
        </dl>

        <h2>Analysis</h2>
        <dl class="menu">
          <dt><g:link controller="report" action="index">Reports</g:link></dt>
		  <dd>
            View Reports
		  </dd>
        </dl>

      </div>

    </div>
  </body>
</html>
