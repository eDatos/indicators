(function () {
    "use strict";

    var App = function () {
    };

    App.loadTemplate = function (name) {
        return Handlebars.templates[name];
    };

    App.models = {};
    App.collections = {};
    App.views = {};
    App.mixins = {};
    App.helpers = {};

    window.App = App;

}());
