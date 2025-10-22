(function ($) {
    "use strict";

    var EDatos = function () {
    };

    EDatos.common = {};
    EDatos.common.apiKey = typeof apiKey !== 'undefined' ? apiKey : '';

    window.EDatos = EDatos;

    $.ajaxPrefilter(function(options, originalOptions, jqXHR) {
        if (EDatos.common.apiKey) {
            jqXHR.setRequestHeader('api-key', EDatos.common.apiKey);
        }
    });

}(window.jQuery));
