[#ftl]
[#include "/includes.ftl"]
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:widget="http://www.netvibes.com/ns/">
<head>

    <meta name="author" content="${organisation}"/>
    <meta name="description" content="${organisation}"/>

    <meta name="apiVersion" content="1.0"/>
    <meta name="autoRefresh" content="20"/>
    <meta name="debugMode" content="false"/>


    <script type="text/javascript" src="${serverURL}/theme/js/libs/jquery-1.7.1.js"></script>
    
    <link rel="stylesheet" type="text/css"
          href="//uwa.netvibes.com/lib/c/UWA/assets/css/standalone.css"/>
    <title>${organisation} | [@apph.messageEscape 'page.widgets.title' /]</title>

    <!-- Add your UWA preferences as needed -->
    <widget:preferences>
    </widget:preferences>

    <link rel="stylesheet" type="text/css" href="${serverURL}/theme/js/widgets/widgets.css"/>
    <script type="text/javascript" src="${serverURL}/theme/js/widgets/widget.min.all.js"></script>

    <script type="text/javascript">
        var permalinksUrlBase = "${permalinksUrlBase}";
        var req = $.ajax({
            url : permalinksUrlBase + "/v1.0/permalinks/${permalinkId?js_string}.json",
            dataType : 'jsonp',
            jsonp : "_callback"
        });
        req.success(function (options) {
            var body = document.getElementsByTagName('body')[0];
            var netvibesScript = document.createElement('script');
            netvibesScript.type = "text/javascript";
            netvibesScript.src = "//uwa.netvibes.com/lib/c/UWA/js/UWA_Standalone_Alone.js";
            netvibesScript.async = false;
            body.appendChild(netvibesScript);
            netvibesScript.onload = function () {
                UWA.i18n({
                    'es': {
                        'Subscribe to this app': 'Suscríbete a esta aplicación',
                        'Settings': 'Ajustes',
                        'Refresh' : 'Actualizar'
                    },
                    'en': {
                        'Subscribe to this app': 'Subscribe to this app',
                        'Settings': 'Settings',
                        'Refresh' : 'Refresh'
                    },
                    'ca': {
                        'Subscribe to this app': 'Subscriu-te a aquesta aplicació',
                        'Settings': 'Configuració',
                        'Refresh' : 'Actualitzar'
                    }
                }[EDatos.common.I18n.getWidgetLocaleFromOptions(options)]);
                widget.onLoad = function () {
                    widget.addBody("<div id='" + options.el.substring(1) + "' class='istac-widget-uwa edatos-indicators'></div>");
                    EdatosIndicatorsWidget(options, null, function (edatosIndicatorsWidget) {
                        widget.setTitle(edatosIndicatorsWidget.title);
                    });
                };
            }
        });
    </script>

    <script type="text/javascript">               
        //Analytics
        (function (i, s, o, g, r, a, m) {
        i['GoogleAnalyticsObject'] = r; i[r] = i[r] || function () {
            (i[r].q = i[r].q || []).push(arguments)
        }, i[r].l = 1 * new Date(); a = s.createElement(o),
            m = s.getElementsByTagName(o)[0]; a.async = 1; a.src = g; m.parentNode.insertBefore(a, m)
        })(window, document, 'script', 'https://www.google-analytics.com/analytics.js', 'ga');
        
        ga('create', '${analyticsGoogleTrackingId}', 'auto');
        ga('send', 'pageview');
    </script>
</head>
<body>

</body>
</html>
