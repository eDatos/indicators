[#ftl]
[#include "/inc/includes.ftl"]
<!DOCTYPE html>
<html>
<head>
    <title>Swagger UI</title>
    <link href='http://fonts.googleapis.com/css?family=Droid+Sans:400,700' rel='stylesheet' type='text/css'/>
    <link href='[@spring.url "/apidocs/css/hightlight.default.css"/]' media='screen' rel='stylesheet' type='text/css'/>
    <link href='[@spring.url "/apidocs/css/screen.css"/]' media='screen' rel='stylesheet' type='text/css'/>
    <script src='[@spring.url "/apidocs/lib/jquery-1.8.0.min.js"/]' type='text/javascript'></script>
    <script src='[@spring.url "/apidocs/lib/jquery.slideto.min.js"/]' type='text/javascript'></script>
    <script src='[@spring.url "/apidocs/lib/jquery.wiggle.min.js"/]' type='text/javascript'></script>
    <script src='[@spring.url "/apidocs/lib/jquery.ba-bbq.min.js"/]' type='text/javascript'></script>
    <script src='[@spring.url "/apidocs/lib/handlebars-1.0.rc.1.js"/]' type='text/javascript'></script>
    <script src='[@spring.url "/apidocs/lib/underscore-min.js"/]' type='text/javascript'></script>
    <script src='[@spring.url "/apidocs/lib/backbone-min.js"/]' type='text/javascript'></script>
    <script src='[@spring.url "/apidocs/lib/swagger.js"/]' type='text/javascript'></script>
    <script src='[@spring.url "/apidocs/swagger-ui.js"/]' type='text/javascript'></script>
    <script src='[@spring.url "/apidocs/lib/highlight.7.3.pack.js"/]' type='text/javascript'></script>

    <style type="text/css">
        .swagger-ui-wrap {
            max-width: 960px;
            margin-left: auto;
            margin-right: auto;
        }

        .icon-btn {
            cursor: pointer;
        }

        #message-bar {
            min-height: 30px;
            text-align: center;
            padding-top: 10px;
        }

        .message-success {
            color: #89BF04;
        }

        .message-fail {
            color: #cc0000;
        }
    </style>

    <script type="text/javascript">
        $(function () {
            window.swaggerUi = new SwaggerUi({
                discoveryUrl:"${appBaseUrl}/apidocs/apis",
                apiKey:"special-key",
                dom_id:"swagger-ui-container",
                supportHeaderParams: false,
                supportedSubmitMethods: ['get', 'post', 'put'],
                onComplete: function(swaggerApi, swaggerUi){
                	if(console) {
                        console.log("Loaded SwaggerUI")
                        console.log(swaggerApi);
                        console.log(swaggerUi);
                    }
                  $('pre code').each(function(i, e) {hljs.highlightBlock(e)});
                },
                onFailure: function(data) {
                	if(console) {
                        console.log("Unable to Load SwaggerUI");
                        console.log(data);
                    }
                },
                docExpansion: "none"
            });

            window.swaggerUi.load();
        });

    </script>
</head>

<body>

<div id="message-bar" class="swagger-ui-wrap">
    &nbsp;
</div>

<div id="swagger-ui-container" class="swagger-ui-wrap">

</div>

</body>

</html>