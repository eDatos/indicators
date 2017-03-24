(function () {
    "use strict";

    App.collections.Measures = Backbone.Collection.extend({

        resetDefaults : function () {
            this.reset([
                {id : 'ABSOLUTE', text : 'Dato'},
                {id : 'ANNUAL_PERCENTAGE_RATE', text : 'Tasa variación anual'},
                {id : 'ANNUAL_PUNTUAL_RATE', text : 'Variación anual'},
                {id : 'INTERPERIOD_PERCENTAGE_RATE', text : 'Tasa variación interperiódica'},
                {id : 'INTERPERIOD_PUNTUAL_RATE', text : 'Variación interperiódica'}
            ]);
        }

    });

}());