function getLabel (internationalString) {

    if (internationalString == null) {
        return '';
    }

    // Current locale
    var label = getLabelLocale(internationalString, currentLocale);
    if (label) {
        return label;
    }
    // Default locale
    label = getLabelLocale(internationalString, defaultLocale);
    if (label) {
        return label;
    }
    // Any locale
    if (internationalString.texts.size !== 0) {
        return internationalString.texts[0].label;
    }

    return '';
}

function getLabelLocale(internationalString, locale) {
    if (internationalString === null || locale === null) {
        return '';
    }
    var localisedString = internationalString[locale];
    if (localisedString) {
        return localisedString;
    }
    return '';
}

function containsLowerCase (a, b) {
    return a.toLowerCase().indexOf(b.toLowerCase()) != -1;
}

function getIndicatorInstanceSubelementDimension (subelementDimension) {

    if (subelementDimension == null) {
        return '';
    }
    if (subelementDimension.title != null) {
        return getLabel(subelementDimension.title);
    } else {
        return subelementDimension.code;
    }
}