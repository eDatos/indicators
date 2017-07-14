(function ($) {

    var showError = function (msg) {
        $(options.el).text(msg);
    };

    Istac.widget.Factory = function (options, initCallback, afterRenderCallback) {
        options = options || {};
        
        options.afterRenderCallback = afterRenderCallback;

        if (options.hasOwnProperty('url')) {
            var url = options.url + "/widgets/external/configuration";

            var configRequest = $.ajax({
                method : "GET",
                dataType : "jsonp",
                jsonp : '_callback',
                url : url
            });

            configRequest.success(function (configuration) {
                Istac.widget.configuration = configuration;

                if (!options.uwa) {
                    Istac.widget.loader.all(options.url);
                }

                var widget;
                if (options.type === 'temporal') {
                    widget = new Istac.widget.Temporal(options);
                } else if (options.type === 'lastData' || options.type === 'recent') {
                    widget = new Istac.widget.LastData(options);
                }

                if (widget) {
                    widget.render();
                } else {
                    showError("Tipo de widget no soportado");
                }
                
                if (initCallback) {
                	initCallback(widget);
                }
                Istac.widget.analytics.trackPageView(options);
            });
        } else {
            showError("Error, no se ha especificado la url del servicio web");
        }
        
    };

    //Global export
    window.IstacWidget = Istac.widget.Factory;

}(jQuery));

