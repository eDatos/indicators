[#ftl]
[#include "/inc/includes.ftl"]
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:widget="http://www.netvibes.com/ns/">
<head>

    <meta name="author" content="ISTAC"/>
    <meta name="description" content="ISTAC"/>

    <meta name="apiVersion" content="1.0"/>
    <meta name="autoRefresh" content="20"/>
    <meta name="debugMode" content="false"/>


    <link rel="stylesheet" type="text/css"
          href="http://uwa.netvibes.com/lib/c/UWA/assets/css/standalone.css"/>
    <script type="text/javascript"
            src="http://uwa.netvibes.com/lib/c/UWA/js/UWA_Standalone_Alone.js"></script>

    <title>Istac widget</title>
    <link rel="icon" type="image/png" href="http://www.gobcan.es/gcc/img/favicon.ico"/>

    <!-- Add your UWA preferences as needed -->
    <widget:preferences>
    </widget:preferences>

    <link rel="stylesheet" type="text/css" href="[@spring.url "/theme/js/widgets/widgets.css"/]"/>
    <script type="text/javascript" src="[@spring.url "/theme/js/widgets/widget.min.all.js"/]"></script>

    <script type="text/javascript">
        widget.onLoad = function () {
            widget.addBody("<div id='istac-widget' class='istac-widget-uwa'></div>");
            var metamacPortalPermalinksApiUrlBase = "${metamacPortalPermalinksEndpoint}";
            $.getJSON(metamacPortalPermalinksApiUrlBase + "/v1.0/permalinks/${permalinkId}").done(function (options) {
                IstacWidget(options, function (istacWidget) {
                    widget.setTitle(istacWidget.title);
                });
            });
        };
    </script>

</head>
<body>

</body>
</html>