(function ($) {

    var showError = function (key) {
        $(options.el).text(EDatos.common.I18n.translate(key));
    };

    Istac.widget.Factory = function (options, initCallback, afterRenderCallback) {
        options = options || {};

        options.afterRenderCallback = afterRenderCallback;

        if (options.hasOwnProperty('url')) {
            var url = options.url + "/widgets/external/configuration";

            var configRequest = $.ajax({
                method: "GET",
                dataType: "jsonp",
                jsonp: '_callback',
                url: url
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
                    showError("ERROR.INVALID_WIDGET_TYPE");
                }

                if (initCallback) {
                    initCallback(widget);
                }
                Istac.widget.analytics.trackPageView(options);
            });
        } else {
            showError("ERROR.URL_NOT_PROVIDED");
        }

    };

    //Global export
    window.IstacWidget = Istac.widget.Factory;

}(jQuery));

