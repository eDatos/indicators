(function (_) {
    "use strict";

    var validTypes = ["lastData", "temporal", "recent"];

    App.models.WidgetOptions = Backbone.Model.extend({

        initialize: function () {
            this._bindCalculatedValues();
            this.set("height", this.get("type") === "temporal" ? '328px' : 'auto');
            this.set("heightQuantity", this.get("type") === "temporal" ? '328' : '');
            this.set("heightUnit", this.get("type") === "temporal" ? 'px' : 'auto');
            this.set("groupType",  this.get("type") === "recent" ? 'subject' : 'allValues');
        },

        defaults: {
            title: '',
            type: 'lastData', // temporal, lastData, recent
            width: '100%',
            widthQuantity: App.constants.WidgetConstants.DEFAULT_PERCENTAGE_WIDTH,
            widthUnit: '%', // or px
            height: 'auto',
            heightQuantity: '',
            heightUnit: 'auto', // px, %, auto
            headerColor: '#0F5B95',
            titleColor: '#FFFFFF',
            borderColor: '#EBEBEB',
            textColor: '#000000',
            indicatorNameColor: "#003366",
            groupType: 'allValues', // or system (or subject just for legacy widgets)
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
                        widthQuantity: 100,
                        widthUnit: '%',
                        heightQuantity: this.get("type") === "temporal" ? '328' : '',
                        heightUnit: this.get("type") === "temporal" ? 'px' : 'auto'
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
                } else if (color === "lightBlue") {
                    this.set({
                        headerColor: "#C4D0DC",
                        titleColor: "#333333"
                    });
                } else if (color === "green") {
                    // Probably belonging to DREM
                    this.set({
                        headerColor: "#457A0E",
                        titleColor: "#FFFFFF"
                    });
                } else if (color === "lightBlue") {
                    this.set({
                        headerColor: "#C4D0DC",
                        titleColor: "#333333"
                    });
                }
            });

        },

        getVisibleOptions: function () {
            return _.omit(this.toJSON(), ['widthQuantity', 'widthUnit', 'heightQuantity', 'heightUnit']);
        }
    });

}(window._));
