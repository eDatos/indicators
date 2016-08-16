(function () {
    "use strict";

    App.collections.Measures = Backbone.Collection.extend({

        resetDefaults : function () {
            this.reset([
                {id : 'ABSOLUTE', text : 'Dato'},
                {id : 'ANNUAL_PERCENTAGE_RATE', text : 'Tasa Variación Anual'},
                {id : 'ANNUAL_PUNTUAL_RATE', text : 'Variación Anual'},
                {id : 'INTERPERIOD_PERCENTAGE_RATE', text : 'Tasa Variación Interperiódica'},
                {id : 'INTERPERIOD_PUNTUAL_RATE', text : 'Variación Interperiódica'}
            ]);
        }

    });

}());