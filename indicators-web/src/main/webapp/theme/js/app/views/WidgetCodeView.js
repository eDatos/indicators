(function (_) {
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
        },

        onClickNetvibes: function (e) {
            e.preventDefault();

            var permalinkAjaxParameters = {
                type: "POST",
                data: JSON.stringify({
                    content: JSON.stringify(_.extend({}, this._getCode(), { uwa: true }))
                }),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            };

            var self = this;
            if (typeof Edatos !== 'undefined' && Edatos.UserManagement) {
                Edatos.UserManagement.prepareRequestWithEdatosAuthentication({...permalinkAjaxParameters, url: permalinksUrlBase + "/v1.0/permalinks"}).then(ajaxSettings => {
                    $.ajax(ajaxSettings).done(permalink => self._openNetvibesInNewTab(permalink)).fail(() => self._requestPermalinkWithCaptcha(permalinkAjaxParameters));
                });
            } else {
                self._requestPermalinkWithCaptcha(permalinkAjaxParameters);
            }
        },

        _openNetvibesInNewTab: function (permalink) {
            // INDISTAC-945 - Using https for avoiding netvibes problems with http.
            // Widget can´t be embeded on http because it generates mixed content errors on the https netvibes dashboard
            var url = this._getHttpsUrl() + "/widgets/uwa/" + permalink.id;
            window.open(url, '_new');
        },

        _requestPermalinkWithCaptcha: function (permalinkAjaxParameters) {
            if (typeof Edatos !== 'undefined' && Edatos.captcha) {
                var permalinkRequestFunction = function (url) {
                    return new Promise(function (resolve, reject) {
                        $.ajax({...permalinkAjaxParameters, url: url}).done(resolve).fail(reject);
                    });
                };

                var captchaOptions = {
                    captchaId: "widget-netvibes-captcha",
                    action: "indicators_permalink",
                    buttonText: EDatos.common.I18n.translate("EMBED.ADD_TO_NETVIBES"),
                    labelText:  EDatos.common.I18n.translate("CAPTCHA.LABEL")
                };

                Edatos.captcha.showCaptchaWithButton(
                    permalinkRequestFunction,
                    permalinksUrlBaseWithProtocol + "/v1.0/permalinks",
                    captchaOptions
                ).then(permalink => this._openNetvibesInNewTab(permalink));
            } else {
                $.ajax({...permalinkAjaxParameters, url: permalinksUrlBase + "/v1.0/permalinks"}).done(permalink => self._openNetvibesInNewTab(permalink));
            }
        }
    });

}(window._));
