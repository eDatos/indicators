(function () {
    "use strict";

    App.models.Indicator = App.models.IndicatorBase.extend({

        url : function () {
            return this.removeProtocol(this.get('selfLink'));
        }

    });



}());