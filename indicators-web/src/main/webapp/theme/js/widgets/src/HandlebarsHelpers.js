(function () {

    // {{#ifEquals sampleString "This is a string"}}
    Handlebars.registerHelper('ifEquals', function (arg1, arg2, options) {
        return (arg1 == arg2) ? options.fn(this) : options.inverse(this);
    });

    // This helper is used on the widget AND on the app
    Handlebars.registerHelper('translate', function (key) {
        return EDatos.common.I18n.translate(key);
    });

}());