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
          href="http://www.netvibes.com/themes/uwa/style.css"/>
    <script type="text/javascript"
            src="http://www.netvibes.com/js/UWA/load.js.php?env=Standalone"></script>

    <title>Istac widget</title>
    <link rel="icon" type="image/png" href="http://www.gobcan.es/gcc/img/favicon.ico"/>

    <!-- Add your UWA preferences as needed -->
    <widget:preferences>
    </widget:preferences>

    <style type="text/css">
        [#include "widgets.css" parse=false]
    </style>

    <script type="text/javascript">
        [#include "widget.min.all.js" parse=true];
    </script>

    <script type="text/javascript">
        $.noConflict();
        var loaded = false;
        widget.onLoad = function () {
            loaded = true;

            widget.addBody("<div id='istac-widget'></div>");
            jQuery('#istac-widget').addClass('istac-widget-uwa');

            var options = ${options};

            var istacWidget = new IstacWidget(options);
            widget.setTitle(istacWidget.title);
        };

        setTimeout(function () {
            if(!loaded) {
                widget.onLoad();
            }
        }, 2000);

    </script>

</head>
<body>

</body>
</html>