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

                options.languages = Istac.widget.configuration['metamac.internationalization.languages'];
                options.defaultLocale = options.languages[0];
                EDatos.common.I18n.setDefaultLocale(options.defaultLocale);

                options.locale = EDatos.common.I18n.getWidgetLocaleFromOptions(options);

                Istac.widget.loader.js(true, Istac.widget.configuration['metamac.analytics.script.url'],
                    { type: 'application/javascript', appId : 'indicators-widget' });

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
            var errorLocale = options.locale === "navigator" ? navigator.language : (options.locale || "es");
            showError(options.el, "ERROR.URL_NOT_PROVIDED", errorLocale.substring(0,2));
        }

    };

    //Global export
    window.IstacWidget = Istac.widget.Factory;

}(window.jQuery));

