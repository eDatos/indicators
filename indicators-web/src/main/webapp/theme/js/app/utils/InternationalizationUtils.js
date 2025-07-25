(function (_) {
    "use strict";

    App.utils.InternationalizationUtils = {

        localizeLabel: function (labels) {
            if (_.isEmpty(labels)) {
                return "";
            }

            if (_.has(labels, 'text')) {
                return labels.text.reduce(function (chosenLabel, localizedLabel) {
                    if (localizedLabel.lang === currentLocale) {
                        return localizedLabel.value;
                    } else if (chosenLabel) {
                        return chosenLabel;
                    } else if (localizedLabel.lang === defaultLocale) {
                        return localizedLabel.value;
                    }
                });
            } else {
                return labels[currentLocale] || labels["__default__"]
            }
        }
    };
}(_));
