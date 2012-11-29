(function () {
    App.mixins.JsonpSync = {
        sync : function (method, model, options) {
            options.timeout = 2000;
            options.dataType = "jsonp";
            options.jsonp = '_callback';
            return Backbone.sync(method, model, options);
        }
    };
}());
