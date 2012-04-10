function getLabel(internationalString, locale) {
	if (internationalString == null) {
		return '';
	}
	var localisedString = _.find(internationalString.texts, function(text) {
		return text.locale == locale;
	});
	if (localisedString) {
		return localisedString.label;
	}
	if (internationalString.texts.size != 0) {
		return internationalString.texts[0].label;
	}
	return '';
};

function containsLowerCase(a, b) {
	return a.toLowerCase().indexOf(b.toLowerCase()) != -1;
};