(function () {
    App.mixins.JsonSync = {
        sync : function (method, model, options) {
            this.trigger('syncStart', model);
            options.timeout = 1000000;
            options.dataType = "json";

            return Backbone.sync(method, model, options);
        }
    };
}());
