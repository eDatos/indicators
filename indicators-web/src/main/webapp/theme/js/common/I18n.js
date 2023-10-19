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

        setCurrentLocale: function (locale) {
          currentLocale = locale;
        },

        setDefaultLocale: function (locale) {
            defaultLocale = locale;
        },

        startsWithVowelOrH: function(string) {
            var vowelAndHRegex = '^[aieouhAIEOUH].*';
            return string.match(vowelAndHRegex);
        },

        getWidgetLocaleFromOptions: function (options) {
            if (!options.languages) {
                console.error("No se han cargado los idiomas permitidos. Esto no deberia pasar.");
                return "es";
            }

            var widgetLocale = options.locale  === "navigator" ? navigator.language : options.locale;
            var formattedWidgetLocale = widgetLocale ? widgetLocale.substring(0,2) : null;
            return formattedWidgetLocale && options.languages.includes(formattedWidgetLocale) ? formattedWidgetLocale : options.languages[0];
        },
    }

}());
