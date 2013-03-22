(function () {
    "use strict";

    var validTypes = ["lastData", "temporal", "recent"];

    var CENTRAL_WIDTH = 423;
    var LATERAL_WIDTH = 266;

    App.models.WidgetOptions = Backbone.Model.extend({

        defaults : {
            title : '',
            type : 'lastData', // temporal, lastData, recent
            width : CENTRAL_WIDTH,
            headerColor : '#0F5B95',
            borderColor : '#EBEBEB',
            textColor : '#000000',
            groupType : 'system', // or subject
            indicatorSystem : '',
            subjectCode : '',
            indicatorsSelection : 'select', // or recent
            indicators : [],
            instances : [],
            nrecent : 4,
            measures : [],
            geographicalValues : [],
            showLabels : false,
            showLegend : false,
            showSparkline : true,
            shadow: true,
            borderRadius : true,
            style : 'custom', //custom, gobcan,
            gobcanStyleColor : 'blue' //blue, green
        },

        validate : function (attrs) {
            if (!_.contains(validTypes, attrs.type)) {
                return "Tipo de widget inválido";
            }
        }

    });

}());