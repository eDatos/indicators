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

                    var value = dataset.getObservationStr(geographicalValue, lastTimeValue, measure);
                    var unit = dataset.getUnit(geographicalValue, lastTimeValue, measure);

                    var showSparkline = this['sparkline_' + measure];

                    return {
                        showSparkline : showSparkline,
                        values : values.join(','),
                        value : value,
                        unit : unit,
                        measure : measuresLabels[measure]
                    };
                }, this);

                var temporalLabel = dataset.getTimeValuesTitles(this.locale)[lastTimeValue];
                return {
                    title : dataset.getTitle(this.locale),
                    jaxiUrl : this.jaxiUrl + '/tabla.do?instanciaIndicador=' + dataset.request.id + '&sistemaIndicadores=' + this.indicatorSystem + '&accion=html',
                    temporalLabel : temporalLabel,
                    observations : observations,
                    description : dataset.getDescription(this.locale)
                };
            },

            render : function () {
                this.renderTable(this.datasets);
            },

            addTooltips : function ($el) {
                $el.find('[data-tooltip]').each(function (i, row) {
                    var $row = $(row);
                    Istac.widget.helper.tooltip($row, $row.data('tooltip'));
                });
            },

            addSparklines : function ($el) {
                if (this.options.showSparkline) {
                    var sparklineOptions = {
                        type : this.options.sparklineType,
                        width : this.options.sparklineWidth + "px",
                        height : this.options.sparklineHeight + "px",
                        lineColor : this.options.headerColor,
                        fillColor : this.options.sparklineFillColor,
                        spotRadius : 0
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
