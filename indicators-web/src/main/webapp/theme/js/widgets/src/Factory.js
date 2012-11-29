(function ($) {
    Istac.widget.Factory = function (options) {
        options = options || {};

        var showError = function (msg) {
            $(options.el).text(msg);
        };

        if (options.hasOwnProperty('url')) {
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
                return widget;
            } else {
                showError("Tipo de widget no soportado");
            }
        } else {
            showError("Error, no se ha especificado la url del servicio web");
        }
    };

    //Global export
    window.IstacWidget = Istac.widget.Factory;

}(jQuery));

