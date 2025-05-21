(function (_) {
    "use strict";

    var validTypes = ["lastData", "temporal", "recent"];

    App.models.WidgetOptions = Backbone.Model.extend({

        initialize: function () {
            this._bindCalculatedValues();
        },

        defaults: {
            title: '',
            type: 'lastData', // temporal, lastData, recent
            width: '100%',
            widthQuantity: App.constants.WidgetConstants.DEFAULT_PERCENTAGE_WIDTH,
            widthUnit: '%', // or px
            headerColor: '#0F5B95',
            titleColor: '#FFFFFF',
            borderColor: '#EBEBEB',
            textColor: '#000000',
            indicatorNameColor: "#003366",
            groupType: 'subject',	// or system
            indicatorsMain: "all", // or onlyMain
            indicatorSystem: '',
            subjectCode: '',
            indicatorsSelection: 'select', // or recent
            indicators: [],
            instances: [],
            nrecent: 4,
            measures: [],
            geographicalValues: [],
            showLabels: false,
            showLegend: false,
            showSparkline: true,
            shadow: true,
            borderRadius: true,
            style: 'custom', //custom, gobcan,
            gobcanStyleColor: 'blue', //blue, green
            sideView: false,
            scale: 'natural-lib',
            showEmbedMoreLink: true
        },

        validate: function (attrs) {
            if (!_.contains(validTypes, attrs.type)) {
                return EDatos.common.I18n.translate("ERROR.INVALID_WIDGET_TYPE");
            }
        },

        _bindCalculatedValues: function () {
            this.on("change:style", function () {
                if (this.get('style') === 'gobcan') {
                    this.set({
                        textColor: this.defaults.textColor,
                        indicatorNameColor: this.defaults.indicatorNameColor,
                        widthQuantity: this.get("sideView") ? 151 : 100,
                        widthUnit: this.get("sideView") ? 'px' : '%'
                    });
                    this.trigger("change:gobcanStyleColor");
                }
            }, this);

            this.on("change:gobcanStyleColor", function () {
                var color = this.get('gobcanStyleColor');
                if (color === "blue") {
                    this.set({
                        headerColor: "#0F5B95",
                        titleColor: "#FFFFFF"
                    });
                } else if (color === "green") {
                    this.set({
                        headerColor: "#457A0E",
                        titleColor: "#FFFFFF"
                    });
                }
            });

        },

        getVisibleOptions: function () {
            return _.omit(this.toJSON(), ['widthQuantity', 'widthUnit']);
        }
    });

}(window._));
