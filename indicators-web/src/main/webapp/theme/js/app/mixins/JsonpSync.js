(function () {
    App.mixins.JsonpSync = {
        sync : function (method, model, options) {
            this.trigger('syncStart', model);
            options.timeout = 1000000;
            options.dataType = "jsonp";
            options.jsonp = '_callback';

            return Backbone.sync(method, model, options);
        }
    };
}());
