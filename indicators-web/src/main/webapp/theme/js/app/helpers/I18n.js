(function () {
    "use strict";

    App.helpers.I18n = {
        getLabel : function (internationalString) {
            if (internationalString === null) {
                return '';
            }

            // Current locale
            var label = App.helpers.I18n.getLabelLocale(internationalString, currentLocale);
            if (label) {
                return label;
            }

            // Default locale
            label = App.helpers.I18n.getLabelLocale(internationalString, defaultLocale);
            if (label) {
                return label;
            }

            // Any locale
            if (internationalString.texts.size !== 0) {
                return internationalString.texts[0].label;
            }

            return '';
        },

        getLabelLocale : function (internationalString, locale) {
            if (internationalString === null || locale === null) {
                return '';
            }
            var localisedString = internationalString[locale];
            if (localisedString) {
                return localisedString;
            }
            return '';
        }
    };
}());
