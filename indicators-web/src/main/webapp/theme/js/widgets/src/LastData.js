(function ($) {

    var measuresLabels = {
        'ABSOLUTE' : 'Dato',
        'ANNUAL_PERCENTAGE_RATE' : 'Tasa variación anual',
        'ANNUAL_PUNTUAL_RATE' : 'Variación anual',
        'INTERPERIOD_PERCENTAGE_RATE' : 'Tasa variación interperiódica',
        'INTERPERIOD_PUNTUAL_RATE' : 'Variación interperiódica'
    };

    Istac.widget.LastData = function (options) {
        this.init(options);
    };

    Istac.widget.LastData.prototype = _.extend(
        {},
        Istac.widget.Base.prototype,
        {
            parseDataset : function (dataset) {
                var lastTimeValue = dataset.getLastTimeValue();
                var geographicalValue = this.geographicalValues[0];

                var observations = _.map(this.measures, function (measure) {
                    var values = dataset.getObservationsByGeoAndMeasure(geographicalValue, measure);
                    values = _.map(values, function (value) { // The library needs explicit null to draw a gap
                        return value === undefined ? "null" : value;
                    });
                    values.push("null"); //Extra null to improve visibility of last element

                    var value = dataset.getObservationStr(geographicalValue, lastTimeValue, measure);
                    var unit = dataset.getUnit(geographicalValue, lastTimeValue, measure);

                    var showSparkline = this['sparkline_' + measure];

                    var timeValues = dataset.getTimeValues();
                    var timeValuesTitles = dataset.getTimeValuesTitles();

                    var timeTitles = _.map(timeValues, function (timeValue) {
                        return timeValuesTitles[timeValue];
                    });

                    return {
                        showSparkline : showSparkline,
                        values : values.join(','),
                        value : value,
                        unit : unit,
                        timeTitles : timeTitles.join(','),
                        measure : measuresLabels[measure],
                        hasValue : value !== null &&  value !== "-"
                    };
                }, this);

                var temporalLabel = dataset.getTimeValuesTitles(this.locale)[lastTimeValue];

                var jaxiBaseUrl = Istac.widget.configuration['indicators.jaxi.url'];

                var jaxiUrl = this.options.groupType === 'system' ?
                    jaxiBaseUrl + '/tabla.do?instanciaIndicador=' + dataset.request.id + '&sistemaIndicadores=' + this.indicatorSystem + '&accion=html' :
                    jaxiBaseUrl + '/tabla.do?indicador=' + dataset.request.id + '&accion=html';

                return {
                    title : dataset.getTitle(this.locale),
                    jaxiUrl : jaxiUrl,
                    temporalLabel : temporalLabel,
                    observations : observations,
                    description : dataset.getDescription(this.locale)
                };
            },

            render : function () {
                this.renderTable(this.datasets);
                this.updateTitle();
            },

            addTooltips : function ($el) {
                $el.find('[data-tooltip]').each(function (i, row) {
                    var $row = $(row);
                    Istac.widget.helper.tooltip($row, $row.data('tooltip'));
                });
            },

            addSparklines : function ($el) {
                if (this.options.showSparkline) {
                    var self = this;
                    var sparklineOptions = {
                        type : this.options.sparklineType,
                        width : this.options.sparklineWidth + "px",
                        height : this.options.sparklineHeight + "px",
                        lineColor : this.options.headerColor,
                        fillColor : this.options.sparklineFillColor,
                        lineWidth: 1.5,
                        highlightLineColor: null,
                        spotRadius : 0,
                        tooltipFormatter : function (sparkline, options, fields) {
                            if (fields.y !== null) {
                                var time = sparkline.$el.data('time');
                                var timeTitle = time.split(",")[fields.x];
                                var unit = sparkline.$el.data('unit');
                                var value = Istac.widget.helper.addThousandSeparator(fields.y);
                                return "<strong>" + value + " " + unit + "</strong><br>" + timeTitle;
                            }
                            return "";
                        }
                    };
                    $el.find('.inlinesparkline').sparkline('html', sparklineOptions);
                }
            },

            renderTable : function (datasets) {
                var context = {};
                context.measures = _.map(this.measures, function (measure) {
                    return measuresLabels[measure];
                });
                context.datasets = _.map(datasets, this.parseDataset, this);

                this.el.toggleClass("istac-widget-lateral", this.options.sideView);
                this.el.toggleClass("istac-widget-lastData", !this.options.sideView);
                var template = this.options.sideView ? Handlebars.templates['last-data-lateral'] : Handlebars.templates['last-data-table'];

                var $el = $(template(context));
                this.contentContainer.html($el);

                this.addTooltips($el);
                this.addSparklines($el);
            }

        }
    );

}(jQuery));
