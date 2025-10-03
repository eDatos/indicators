(function ($) {
    "use strict";

    var EDatos = function () {
    };

    EDatos.common = {};

    window.EDatos = EDatos;

    $.ajaxPrefilter(function(options, originalOptions, jqXHR) {
        jqXHR.setRequestHeader('apiKey', apiKey);     
    });

}(window.jQuery));
