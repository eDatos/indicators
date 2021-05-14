(function () {
    "use strict";

    App.views.WidgetCodeView = Backbone.View.extend({

        template: App.loadTemplate('code'),

        initialize: function () {
            this.model.on('change', this.render, this);
        },

        events: {
            "click .widget-import-netvibes-link": "onClickNetvibes"
        },

        _getUrl: function () {
            return serverURL;
        },

        _getHttpsUrl: function () {
            return "https:" + this._getUrl();
        },

        _getCode: function () {
            var url = this._getUrl();
            var code = _.extend(this.model.toJSON(), {
                el: "#istac-widget",
                url: url,
                visualizerUrl: visualizerUrl,
                apiUrl: apiUrl
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
                parameters: parameters
            };

            this.$el.html(this.template(templateContext));
            return this;
        },

        onClickNetvibes: function (e) {
            e.preventDefault();

            var code = this._getCode();
            var uwaCode = _.extend({}, code, { uwa: true });

            var permalink = {
                content: JSON.stringify(uwaCode)
            };

            var ajaxParameters = {
                url: permalinksUrlBase + "/v1.0/permalinks",
                type: "POST",
                data: JSON.stringify(permalink),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            };

            var captchaOptions = {
                captchaEl: ".widget-netvibes-captcha",
                buttonText: EDatos.common.I18n.translate("EMBED.ADD_TO_NETVIBES")
            };

            var self = this;
            var request = metamac.authentication.ajax(ajaxParameters, captchaOptions);
            request.done(function (response) {
                // INDISTAC-945 - Using https for avoiding netvibes problems with http. 
                // Widget can´t be embeded on http because it generates mixed content errors on the https netvibes dashboard
                var url = self._getHttpsUrl() + "/widgets/uwa/" + response.id;
                window.open(url, '_new');
            });

        }


    });

}());