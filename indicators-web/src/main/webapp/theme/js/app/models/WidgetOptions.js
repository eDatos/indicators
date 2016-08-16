(function () {
    "use strict";

    var validTypes = ["lastData", "temporal", "recent"];

    var CENTRAL_WIDTH = 423;
    var LATERAL_WIDTH = 266;

    App.models.WidgetOptions = Backbone.Model.extend({

        initialize : function () {
            this._bindCalculatedValues();
        },

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
            shadow : true,
            borderRadius : true,
            style : 'custom', //custom, gobcan,
            gobcanStyleColor : 'blue', //blue, green
            sideView : false,
            scale : 'natural-lib'
        },

        validate : function (attrs) {
            if (!_.contains(validTypes, attrs.type)) {
                return "Tipo de widget inválido";
            }
        },

        _bindCalculatedValues : function () {
            this.on("change:style", function () {
                if (this.get('style') === 'gobcan') {
                    var width = this.get('sideView') ? 151 : 423;
                    this.set({textColor : '#000000', width : width});
                    this.trigger("change:gobcanStyleColor");
                }
            }, this);

            this.on("change:gobcanStyleColor", function () {
                var color = this.get('gobcanStyleColor');
                if (color === "blue") {
                    this.set({headerColor : "#0F5B95"});
                } else if (color === "green") {
                    this.set('headerColor', "#457A0E");
                }
            });

        }

    });

}());