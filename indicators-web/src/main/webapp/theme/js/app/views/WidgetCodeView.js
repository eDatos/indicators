(function () {
    "use strict";

    App.views.WidgetCodeView = Backbone.View.extend({

        template : App.loadTemplate('code'),

        initialize : function () {
            this.model.on('change', this.render, this);
        },

        _getUrl : function () {
            return serverURL + context;
        },

        _getCode : function () {
            var url = this._getUrl();
            var code = _.extend(this.model.toJSON(), {
                el : "#istac-widget",
                url : url,
                jaxiUrl : jaxiUrl
            });
            return code;
        },

        render : function () {
            var url = this._getUrl();
            var code = this._getCode();

            var parameters = $.param({options : JSON.stringify(code)});
            var uwaCode = _.extend({}, code, {uwa : true});
            var uwaParameters = $.param({options : JSON.stringify(uwaCode)});
            var templateContext = {
                script : url + '/theme/js/widgets/widget.min.all.js',
                code : JSON.stringify(code, null, 8),
                parameters : parameters,
                uwaParameters : uwaParameters,
                context : apiBaseUrl
            };

            this.$el.html(this.template(templateContext));
            return this;
        }


    });

}());