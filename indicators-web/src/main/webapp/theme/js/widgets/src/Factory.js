(function ($) {

    var showError = function (el, key, locale) {
        $(el).text(EDatos.common.I18n.translate(key, locale));
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

            configRequest.success(function onSuccess(configuration) {
                Istac.widget.configuration = configuration;

                options.languages = Istac.widget.configuration['metamac.internationalization.languages'].split(',').filter(function(language) { return language });
                options.locale = EDatos.common.I18n.getWidgetLocaleFromOptions(options);

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
                    showError(options.el, "ERROR.INVALID_WIDGET_TYPE", options.locale);
                }

                if (initCallback) {
                    initCallback(widget);
                }
                Istac.widget.analytics.trackPageView(options);
            });
        } else {
            showError(options.el, "ERROR.URL_NOT_PROVIDED", options.locale === "navigator" ? navigator.language : (options.locale || "es"));
        }

    };

    //Global export
    window.IstacWidget = Istac.widget.Factory;

}(window.jQuery));

