(function () {

    // This variable are loaded from index.ftl. On widget mode, they default to 'es'
    var currentLocale = typeof window.currentLocale !== 'undefined' ? currentLocale : 'es';
    var defaultLocale = typeof window.defaultLocale !== 'undefined' ? defaultLocale : 'es';

    EDatos.common.I18n = {
        translate: function (key) {
            if (key === null) {
                console.warn("Tried to translate null key");
                return '';
            }

            // Current locale            
            var label = EDatos.common.helper.get(EDatos.common.translations[currentLocale], key);
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
        }
    }

}());