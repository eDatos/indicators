[#ftl]
[#macro base page_title='app.title' extra_keywords='' page_description='app.description']
    <!DOCTYPE html>
    <html>
    <head>
        [#include "/layout/header.ftl"]

        <link rel="stylesheet" href="${serverURL}/theme/css/reset.css" type="text/css" media="screen, projection"/>
        [#if portalDefaultStyleCssUrl?has_content]
            <link href="${portalDefaultStyleCssUrl}" media='screen' rel='stylesheet' type='text/css'/>
        [#else]
            <!-- portalStyleCssUrl is empty -->
        [/#if]
        <link rel="icon" href="${faviconUrl}"/>
    </head>
    <body>

    <!-- begin: #cabecera -->
    ${portalDefaultStyleHeader!}
    <!-- end: #cabecera -->

    <link rel="stylesheet" href="${serverURL}/theme/css/libs/jquery-ui/jquery-ui-1.8.18.custom.css" type="text/css"
          media="screen, projection"/>
    <link rel="stylesheet" href="${serverURL}/theme/js/libs/select2/select2.css" type="text/css"
          media="screen, projection"/>
    <link rel="stylesheet" href="${serverURL}/theme/js/libs/colorpicker/css/colorpicker.min.css" type="text/css"
          media="screen, projection"/>
    <link rel="stylesheet" href="${serverURL}/theme/css/main.css" type="text/css" media="screen, projection"/>
    <link rel="stylesheet" href="${serverURL}/theme/js/widgets/widgets.css" type="text/css" media="screen, projection"/>

    <script src="${serverURL}/theme/js/vendor.min.js"></script>

    <!-- Global variables -->
    <script type="text/javascript">
        var serverURL = "${serverURL}";
        var currentLocale = "[@apph.locale /]";
        var defaultLocale = "${defaultLocale}";
        var srmRestUrl = "${srmExternalApiUrlBase}" + '/v1.0';
        var apiKey = "${apiKey}"
        const {srmAgency, srmResource, srmVersion} = splitUrnScheme("${defaultCategorySchemeUrn}");
        [#if indicatorsExternalApiUrlBase??]
        var apiUrl = "${indicatorsExternalApiUrlBase}" + '/v1.0';
        [/#if]
        var visualizerUrl = "${visualizerApplicationExternalUrlBase}";

        function splitUrnScheme(urn) {
            const tripletIdentifier = removeUrnPrefix(urn);
            return splitUrnWithoutPrefix(tripletIdentifier);
        }

        function removeUrnPrefix(urn) {
            if (!urn || urn.length === 0) {
                throw new Error("URN is empty");
            }
            const splitUrn = urn.split("=");
            if (splitUrn.length !== 2) {
                throw new Error("URN is not valid");
            }
            return splitUrn[1];
        }

        function splitUrnWithoutPrefix(identifier) {
            const srmAgency = identifier.substring(0, identifier.indexOf(":"));
            const srmResource = identifier.substring(identifier.indexOf(":") + 1, identifier.indexOf("("));
            const srmVersion = identifier.substring(identifier.indexOf("(") + 1, identifier.indexOf(")"));
            return {srmAgency, srmResource, srmVersion};
        }

        $.ajaxPrefilter(function( options, originalOptions, jqXHR ) {
            jqXHR.setRequestHeader('api-key', apiKey);     
        });
    </script>

    <div class="indicators-main">
        [#nested]
    </div>

    <!-- begin: pie -->
    ${portalDefaultStyleFooter!}
    <!-- end: pie -->

    <script type="text/javascript">
        (function (i, s, o, g, r, a, m) {
            i['GoogleAnalyticsObject'] = r;
            i[r] = i[r] || function () {
                (i[r].q = i[r].q || []).push(arguments)
            }, i[r].l = 1 * new Date();
            a = s.createElement(o),
                m = s.getElementsByTagName(o)[0];
            a.async = 1;
            a.src = g;
            m.parentNode.insertBefore(a, m)
        })(window, document, 'script', 'https://www.google-analytics.com/analytics.js', 'ga');

        ga('create', '${analyticsGoogleTrackingId}', 'auto');
        ga('send', 'pageview');
    </script>
    </body>
    </html>
[/#macro]
