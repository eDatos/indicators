(function () {
    "use strict";

    App.models.IndicatorInstance = App.models.IndicatorBase.extend({
        idAttribute : "id",

        url : function () {
            return this.removeProtocol(this.get('selfLink'));
        }

    });

}());
