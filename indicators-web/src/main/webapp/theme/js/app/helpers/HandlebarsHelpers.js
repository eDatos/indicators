(function () {

    Handlebars.registerHelper('getLabel', function (key) {
        return App.helpers.I18n.getLabel(key);
    });

    var toOptions = function (context) {
        var ret = '';
        for (var i = 0; i < context.length; i++) {
            var option = '<option value="' + context[i].value + '"';
            option += '>' + context[i].text + '</option>';
            ret += option;
        }
        return ret;
    };

    Handlebars.registerHelper('options', function (context) {
        return new Handlebars.SafeString(toOptions(context));
    });

    Handlebars.registerHelper('groupedOptions', function (context) {
        var ret = '';
        for (var i = 0; i < context.length; i++) {
            var item = context[i];
            ret += "<optgroup label='" + item.title + "'>";
            ret += toOptions(item.values);
            ret += "</optgroup>";
        }
        return new Handlebars.SafeString(ret);
    });

    Handlebars.registerHelper('context', function (context) {
        return apiBaseUrl;
    });
    
}());