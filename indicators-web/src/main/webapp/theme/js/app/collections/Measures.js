(function () {
    "use strict";

    App.collections.Measures = Backbone.Collection.extend({

        defaultsMeasures: [
            { id: 'ABSOLUTE', text: EDatos.common.I18n.translate('MEASURE.ABSOLUTE') },
            { id: 'ANNUAL_PERCENTAGE_RATE', text: EDatos.common.I18n.translate('MEASURE.ANNUAL_PERCENTAGE_RATE') },
            { id: 'ANNUAL_PUNTUAL_RATE', text: EDatos.common.I18n.translate('MEASURE.ANNUAL_PUNTUAL_RATE') },
            { id: 'INTERPERIOD_PERCENTAGE_RATE', text: EDatos.common.I18n.translate('MEASURE.INTERPERIOD_PERCENTAGE_RATE') },
            { id: 'INTERPERIOD_PUNTUAL_RATE', text: EDatos.common.I18n.translate('MEASURE.INTERPERIOD_PUNTUAL_RATE') }
        ],

        resetDefaults: function () {
            this.reset(this.defaultsMeasures);
        },

        resetFromRepresentations: function (measureRepresentations) {
            var representationsCodes = measureRepresentations.map(representation => representation.code);
            this.reset(_.filter(this.defaultsMeasures, function (measure) {
                return _.contains(representationsCodes, measure.id);
            }));
        }
    });
}());
