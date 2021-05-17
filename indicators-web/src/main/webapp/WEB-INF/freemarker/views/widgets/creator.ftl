[#ftl]
[#include "/includes.ftl"]

[@template.base page_title='page.widgets.title' extra_keywords="page.widgets.keywords" page_description=description]

<div id="widget-creator" class="edatos-indicators">

    <div id="widget-preview-content" class="widget"></div>

    <div class="tabbable" id="widget-options-tabs">
        <ul class="tab-navigation nav-tabs">
            <li><a href="#" data-tab="data">[@apph.messageEscape 'entity.widgets.tab.data'/]</a></li>
            <li><a href="#" data-tab="style">[@apph.messageEscape 'entity.widgets.tab.styles'/]</a></li>
            <li><a href="#" data-tab="export">[@apph.messageEscape 'entity.widgets.tab.export'/]</a></li>
        </ul>
        <div class="tab-content"></div>
    </div>
</div>

<script src="${serverURL}/theme/js/app.min.js"></script>
<script src="${visualizerExternalUrlBase}/js/authentication.js"></script>

<script>
    var apiUrl = "${indicatorsExternalApiUrlBase}" + "/v1.0";
    var visualizerUrl = "${visualizerExternalUrlBase}";
    var permalinksUrlBase = "${permalinksUrlBase}"

    var options = {};
    [#if RequestParameters.type??]
    options.type = '${RequestParameters.type?js_string}';
    [/#if]



    $(function () {
        var widgetView = new App.views.WidgetView(options);
        widgetView.render();
    });

</script>

<!--[if lt IE 8]>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/chrome-frame/1/CFInstall.min.js"></script>
<script>
    // The conditional ensures that this code will only execute in IE,
    // Therefore we can use the IE-specific attachEvent without worry
    window.attachEvent("onload", function() {
        CFInstall.check({
            mode: "overlay"
         });
    });
</script>
<![endif]-->

[/@template.base]
