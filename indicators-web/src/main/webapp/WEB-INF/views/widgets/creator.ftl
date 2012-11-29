[#ftl]
[#include "/inc/includes.ftl"]
[@template.base]

<div id="widget-creator">

    <div id="widget-preview-content" class="widget"></div>

    <div class="tabbable" id="widget-options-tabs">
        <ul class="tab-navigation nav-tabs">
            <li><a href="#" data-tab="data">Datos</a></li>
            <li><a href="#" data-tab="style">Estilos</a></li>
            <li><a href="#" data-tab="export">Exportar</a></li>
        </ul>
        <div class="tab-content"></div>
    </div>
</div>

<!-- libs-->
<script src="[@spring.url "/theme/js/libs/underscore.string.min-v.2.0.0-57.js"/]"></script>
<script src="[@spring.url "/theme/js/libs/handlebars-1.0.0.beta.6.js"/]"></script>
<script src="[@spring.url "/theme/js/libs/Backbone.ModelBinder.js"/]"></script>
<script src="[@spring.url "/theme/js/libs/select2/select2.js"/]"></script>

<!-- App -->
<script src="[@spring.url "/theme/js/app/App.js"/]"></script>
<script src="[@spring.url "/theme/js/app/Templates.js"/]"></script>

<!-- helpers -->
<script src="[@spring.url "/theme/js/app/helpers/I18n.js"/]"></script>
<script src="[@spring.url "/theme/js/app/helpers/HandlebarsHelpers.js"/]"></script>

<!-- Mixins-->
<script src="[@spring.url "/theme/js/app/mixins/JsonpSync.js"/]"></script>


<!-- Model -->
<script src="[@spring.url "/theme/js/app/models/Subject.js"/]"></script>
<script src="[@spring.url "/theme/js/app/models/WidgetOptions.js"/]"></script>
<script src="[@spring.url "/theme/js/app/models/IndicatorSystem.js"/]"></script>
<script src="[@spring.url "/theme/js/app/models/GeographicalGranularity.js"/]"></script>
<script src="[@spring.url "/theme/js/app/models/GeographicalValue.js"/]"></script>
<script src="[@spring.url "/theme/js/app/models/IndicatorBase.js"/]"></script>
<script src="[@spring.url "/theme/js/app/models/Indicator.js"/]"></script>
<script src="[@spring.url "/theme/js/app/models/IndicatorInstance.js"/]"></script>

<script src="[@spring.url "/theme/js/app/collections/Measures.js"/]"></script>
<script src="[@spring.url "/theme/js/app/collections/IndicatorsBase.js"/]"></script>
<script src="[@spring.url "/theme/js/app/collections/IndicatorsInstances.js"/]"></script>
<script src="[@spring.url "/theme/js/app/collections/IndicatorSystems.js"/]"></script>
<script src="[@spring.url "/theme/js/app/collections/ToggleSelection.js"/]"></script>
<script src="[@spring.url "/theme/js/app/collections/Subjects.js"/]"></script>
<script src="[@spring.url "/theme/js/app/collections/Indicators.js"/]"></script>
<script src="[@spring.url "/theme/js/app/collections/GeographicalGranularities.js"/]"></script>
<script src="[@spring.url "/theme/js/app/collections/GeographicalValues.js"/]"></script>


<!-- Widget -->
<script src="[@spring.url "/theme/js/widgets/libs/Class.js"/]"></script>
<script src="[@spring.url "/theme/js/widgets/libs/raphael-min.js"/]"></script>
<script src="[@spring.url "/theme/js/widgets/libs/elycharts.min.js"/]"></script>
<script src="[@spring.url "/theme/js/widgets/libs/jquery.sparkline.js"/]"></script>

<script src="[@spring.url "/theme/js/widgets/src/Templates.js"/]"></script>
<script src="[@spring.url "/theme/js/widgets/src/Istac.js"/]"></script>
<script src="[@spring.url "/theme/js/widgets/src/Helper.js"/]"></script>
<script src="[@spring.url "/theme/js/widgets/src/Dataset.js"/]"></script>
<script src="[@spring.url "/theme/js/widgets/src/DatasetRequestBuilder.js"/]"></script>
<script src="[@spring.url "/theme/js/widgets/src/Base.js"/]"></script>
<script src="[@spring.url "/theme/js/widgets/src/LastData.js"/]"></script>
<script src="[@spring.url "/theme/js/widgets/src/Temporal.js"/]"></script>
<script src="[@spring.url "/theme/js/widgets/src/Loader.js"/]"></script>
<script src="[@spring.url "/theme/js/widgets/src/Factory.js"/]"></script>

<!-- Vistas -->
<script src="[@spring.url "/theme/js/app/views/TabView.js"/]"></script>

<script src="[@spring.url "/theme/js/app/views/Select2View.js"/]"></script>
<script src="[@spring.url "/theme/js/app/views/WidgetCodeView.js"/]"></script>
<script src="[@spring.url "/theme/js/app/views/WidgetDataOptionsLastDataView.js"/]"></script>
<script src="[@spring.url "/theme/js/app/views/WidgetDataOptionsRecentView.js"/]"></script>
<script src="[@spring.url "/theme/js/app/views/WidgetDataOptionsTemporalView.js"/]"></script>
<script src="[@spring.url "/theme/js/app/views/WidgetPreviewView.js"/]"></script>
<script src="[@spring.url "/theme/js/app/views/WidgetStyleOptionsView.js"/]"></script>
<script src="[@spring.url "/theme/js/app/views/WidgetView.js"/]"></script>


<script>
    var apiBaseUrl = "[@spring.url ""/]";
    var apiContext = apiBaseUrl + "/api/indicators/v1.0";
    var jaxiUrl = "${jaxiUrlBase}";

    var options = {};
    [#if RequestParameters.type??]
    options.type = '${RequestParameters.type}';
    [/#if]

    var widgetView = new App.views.WidgetView(options);
    widgetView.render();

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
