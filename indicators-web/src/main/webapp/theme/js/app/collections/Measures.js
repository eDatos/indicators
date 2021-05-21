(function () {
    "use strict";

    App.collections.Measures = Backbone.Collection.extend({

        resetDefaults: function () {
            this.reset([
                { id: 'ABSOLUTE', text: EDatos.common.I18n.translate('MEASURE.ABSOLUTE') },
                { id: 'ANNUAL_PERCENTAGE_RATE', text: EDatos.common.I18n.translate('MEASURE.ANNUAL_PERCENTAGE_RATE') },
                { id: 'ANNUAL_PUNTUAL_RATE', text: EDatos.common.I18n.translate('MEASURE.ANNUAL_PUNTUAL_RATE') },
                { id: 'INTERPERIOD_PERCENTAGE_RATE', text: EDatos.common.I18n.translate('MEASURE.INTERPERIOD_PERCENTAGE_RATE') },
                { id: 'INTERPERIOD_PUNTUAL_RATE', text: EDatos.common.I18n.translate('MEASURE.INTERPERIOD_PUNTUAL_RATE') }
            ]);
        }

    });

}());