(function () {

    // This variable are loaded from index.ftl. On widget mode, they default to 'es'
    var currentLocale = typeof window.currentLocale !== 'undefined' ? window.currentLocale : 'es';
    var defaultLocale = typeof window.defaultLocale !== 'undefined' ? window.defaultLocale : 'es';

    EDatos.common.I18n = {
        translate: function (key, locale) {
            if (key === null) {
                console.warn("Tried to translate null key");
                return '';
            }

            // Current locale            
            var label = EDatos.common.helper.get(EDatos.common.translations[locale || currentLocale], key);
            if (label) {
                return label;
            }

            // Default locale
            label = EDatos.common.helper.get(EDatos.common.translations[defaultLocale], key);
            if (label) {
                return label;
            }

            console.warn('No translation found for "' + key + '"');
            return key;
        },

        getWidgetLocaleFromOptions: function (options) {
            if (!options.languages) {
                console.error("No se han cargado los idiomas permitidos. Esto no deberia pasar.");
                return "es";
            }

            var widgetLocale = options.locale  === "navigator" ? navigator.language : options.locale;
            var formattedWidgetLocaleIfValid = options.languages.find(function (language) {
                var languageRegex = new RegExp("^" + language + "\\b");
                return languageRegex.test(widgetLocale);
            });
            return formattedWidgetLocaleIfValid ? formattedWidgetLocaleIfValid : options.languages[0];
        },
    }

}());