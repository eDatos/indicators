(function (_) {
    "use strict";

    App.views.WidgetCodeView = Backbone.View.extend({

        template: App.loadTemplate('code'),

        initialize: function () {
            this.model.on('change', this.render, this);
        },

        _getUrl: function () {
            return serverURL;
        },

        _getHttpsUrl: function () {
            return "https:" + this._getUrl();
        },

        _getCode: function () {
            var url = this._getUrl();
            var id = crypto.randomUUID();
            var code = _.extend(this.model.getVisibleOptions(), {
                el: "#indicators-widget-" + id,
                url: url,
                visualizerUrl: visualizerUrl,
                apiUrl: apiUrl,
                id: id
            });
            return code;
        },

        render: function () {
            var url = this._getUrl();
            var code = this._getCode();

            var parameters = $.param({ options: JSON.stringify(code) });
            var templateContext = {
                script: url + '/theme/js/widgets/widget.min.all.js',
                code: JSON.stringify(code, null, 8),
                parameters: parameters,
                indicatorWidgetId: code.el.substring(1)
            };

            this.$el.html(this.template(templateContext));
            return this;
        }
    });

}(window._));
