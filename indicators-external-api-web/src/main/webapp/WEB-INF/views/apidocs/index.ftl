[#ftl]
[#include "/inc/includes.ftl"]
<!DOCTYPE html>
<html>
<head>
    <title>ISTAC | Indicators</title>
  <link href="${indicatorsExternalApiUrlBase}/apidocs/css/typography.css" media='screen' rel='stylesheet' type='text/css'/>
  <link href="${indicatorsExternalApiUrlBase}/apidocs/css/reset.css" media='screen' rel='stylesheet' type='text/css'/>
  <link href="${indicatorsExternalApiUrlBase}/apidocs/css/screen.css" media='screen' rel='stylesheet' type='text/css'/>
  <link href="${indicatorsExternalApiUrlBase}/apidocs/css/reset.css" media='print' rel='stylesheet' type='text/css'/>
  <link href="${indicatorsExternalApiUrlBase}/apidocs/css/print.css" media='print' rel='stylesheet' type='text/css'/>
  <link rel="shortcut icon" href="//www.gobiernodecanarias.org/gc/img/favicon.ico"/>
  <script src="${indicatorsExternalApiUrlBase}/apidocs/lib/jquery-1.8.0.min.js" type='text/javascript'></script>
  <script src="${indicatorsExternalApiUrlBase}/apidocs/lib/jquery.slideto.min.js" type='text/javascript'></script>
  <script src="${indicatorsExternalApiUrlBase}/apidocs/lib/jquery.wiggle.min.js" type='text/javascript'></script>
  <script src="${indicatorsExternalApiUrlBase}/apidocs/lib/jquery.ba-bbq.min.js" type='text/javascript'></script>
  <script src="${indicatorsExternalApiUrlBase}/apidocs/lib/handlebars-2.0.0.js" type='text/javascript'></script>
  <script src="${indicatorsExternalApiUrlBase}/apidocs/lib/underscore-min.js" type='text/javascript'></script>
  <script src="${indicatorsExternalApiUrlBase}/apidocs/lib/backbone-min.js" type='text/javascript'></script>
  <script src="${indicatorsExternalApiUrlBase}/apidocs/swagger-ui.js" type='text/javascript'></script>
  <script src="${indicatorsExternalApiUrlBase}/apidocs/lib/highlight.7.3.pack.js" type='text/javascript'></script>
  <script src="${indicatorsExternalApiUrlBase}/apidocs/lib/jsoneditor.min.js" type='text/javascript'></script>
  <script src="${indicatorsExternalApiUrlBase}/apidocs/lib/marked.js" type='text/javascript'></script>
  <script src="${indicatorsExternalApiUrlBase}/apidocs/lib/swagger-oauth.js" type='text/javascript'></script>
  
  <script src="${indicatorsExternalApiUrlBase}/apidocs/lang/translator.js" type='text/javascript'></script>
  <script src="${indicatorsExternalApiUrlBase}/apidocs/lang/es.js" type='text/javascript'></script>
      
    <script type="text/javascript">
    $(function () {
      var url = window.location.search.match(/url=([^&]+)/);
      if (url && url.length > 1) {
        url = decodeURIComponent(url[1]);
      } else {
          var baseUrl = document.location.href;
          //this removes the anchor at the end, if there is one
          baseUrl = baseUrl.substring(0, (baseUrl.indexOf("#") == -1) ? baseUrl.length : baseUrl.indexOf("#"));
          //this removes the query after the file name, if there is one
          baseUrl = baseUrl.substring(0, (baseUrl.indexOf("?") == -1) ? baseUrl.length : baseUrl.indexOf("?"));
          //this removes everything after the last slash in the path
          baseUrl = baseUrl.substring(0, (baseUrl.lastIndexOf("/") == -1) ? baseUrl.length : baseUrl.lastIndexOf("/"));

          url = baseUrl + "/docs";
      }

      // Pre load translate...
      if(window.SwaggerTranslator) {
        window.SwaggerTranslator.translate();
      }
      window.swaggerUi = new SwaggerUi({
        url: url,
        dom_id: "swagger-ui-container",
        supportedSubmitMethods: ['get', 'post', 'put', 'delete', 'patch'],
        onComplete: function(swaggerApi, swaggerUi){
          if(typeof initOAuth == "function") {
            initOAuth({
              clientId: "your-client-id",
              clientSecret: "your-client-secret-if-required",
              realm: "your-realms",
              appName: "your-app-name", 
              scopeSeparator: ",",
              additionalQueryStringParams: {}
            });
          }

          if(window.SwaggerTranslator) {
            window.SwaggerTranslator.translate();
          }

          $('pre code').each(function(i, e) {
            hljs.highlightBlock(e)
          });

        },
        onFailure: function(data) {
          log("Unable to Load SwaggerUI");
        },
        docExpansion: "none",
        jsonEditor: false,
        apisSorter: "alpha",
        defaultModelRendering: 'model',
        showRequestHeaders: false
      });

      window.swaggerUi.load();

      function log() {
        if ('console' in window) {
          console.log.apply(console, arguments);
        }
      }
  });
  </script>
  
  [#if apiStyleCssUrl?has_content]
    <link href="${apiStyleCssUrl}" media='screen' rel='stylesheet' type='text/css' />
  [/#if]
  
</head>
<body>
    
    ${apiStyleHeader!}
    
    <div class="swagger-section">
        <div id="message-bar" class="swagger-ui-wrap" data-sw-translate>&nbsp;</div>
        <div id="swagger-ui-container" class="swagger-ui-wrap"></div>
    </div>

    ${apiStyleFooter!}
</body>
</html>
