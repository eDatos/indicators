(function ($) {

    var measuresLabels = {
        'ABSOLUTE': 'Dato',
        'ANNUAL_PERCENTAGE_RATE': 'Tasa variación anual',
        'ANNUAL_PUNTUAL_RATE': 'Variación anual',
        'INTERPERIOD_PERCENTAGE_RATE': 'Tasa variación interperiódica',
        'INTERPERIOD_PUNTUAL_RATE': 'Variación interperiódica'
    };

    Istac.widget.LastData = function (options) {
        this.init(options);
    };

    Istac.widget.LastData.prototype = _.extend(
        {},
        Istac.widget.Base.prototype,
        {
            _limitMaxSparkLine: function (arr, localMax) {
                var globalMax = parseInt(Istac.widget.configuration["indicators.widgets.sparkline.max"]);
                var max = localMax && localMax < globalMax ? localMax : globalMax;
                return _.last(arr, max);
            },

            parseDataset: function (dataset) {
                var lastTimeValue = dataset.getLastTimeValue();
                var geographicalValue = this.geographicalValues[0];

                var self = this;
                var anySparkline = _.chain(this.measures).map(function (measure) {
                    return self.options['sparkline_' + measure];
                }).reduce(function (memo, current) {
                    return memo || current;
                }, false).value();

                var observations = _.map(this.measures, function (measure) {
                    var values = dataset.getObservationsByGeoAndMeasure(geographicalValue, measure);
                    values = _.map(values, function (value) { // The library needs explicit null to draw a gap
                        return value === undefined ? "null" : value;
                    });

                    var value = dataset.getObservationStr(geographicalValue, lastTimeValue, measure);
                    var unit = dataset.getUnit(measure, this.locale);

                    var showSparkline = this.options['sparkline_' + measure];
                    var sparklineType = this.options['sparklineType_' + measure];
                    var max = parseInt(this.options['sparklineMax_' + measure]);

                    var timeValues = dataset.getTimeValues();
                    var timeValuesTitles = dataset.getTimeValuesTitles();

                    var timeTitles = _.map(timeValues, function (timeValue) {
                        return timeValuesTitles[timeValue];
                    });

                    values = this._limitMaxSparkLine(values, max);
                    timeTitles = this._limitMaxSparkLine(timeTitles, max);

                    return {
                        anySparkline: anySparkline,
                        showSparkline: showSparkline,
                        type: sparklineType,
                        values: values.join(','),
                        value: value,
                        unit: unit,
                        timeTitles: timeTitles.join(','),
                        measure: measuresLabels[measure],
                        hasValue: value !== null && value !== "-"
                    };
                }, this);

                var temporalLabel = dataset.getTimeValuesTitles(this.locale)[lastTimeValue];

                var visualizerUrl = this._getVisualizerIndicatorUrl(dataset, geographicalValue);

                return {
                    title: dataset.getTitle(this.locale),
                    titleLink: visualizerUrl,
                    temporalLabel: temporalLabel,
                    observations: observations,
                    description: dataset.getDescription(this.locale)
                };
            },

            _getVisualizerIndicatorUrl: function (dataset, geographicalValue) {
                var visualizerUrl = "";
                if (this.options.groupType === 'system') {
                    visualizerUrl = this.getVisualizerUrlForIndicatorInstance(dataset.request.id, this.indicatorSystem);
                } else {
                    visualizerUrl = this.getVisualizerUrlForIndicator(dataset.request.id);
                }

                visualizerUrl += '&measure=' + this.measures.join(",");
                visualizerUrl += "&geo=" + geographicalValue;

                return visualizerUrl;
            },

            getVisualizerUrlForIndicator: function (indicatorId) {
                return Istac.widget.configuration['metamac.portal.web.external.visualizer'] + '/data.html?resourceType=indicator&resourceId=' + indicatorId;
            },

            getVisualizerUrlForIndicatorInstance: function (indicatorInstanceId, indicatorSystem) {
                return Istac.widget.configuration['metamac.portal.web.external.visualizer'] + '/data.html?resourceType=indicatorInstance&resourceId=' + indicatorInstanceId + '&indicatorSystem=' + indicatorSystem;
            },

            render: function () {
                this.renderTable(this.datasets);
                this.updateTitle();

                this.onAfterRender();
            },

            addTooltips: function ($el) {
                $el.find('[data-tooltip]').each(function (i, row) {
                    var $row = $(row);
                    var tooltipId = 'row-' + i;
                    Istac.widget.helper.tooltip($row, $row.data('tooltip'), tooltipId);
                });
            },

            addSparklines: function ($el) {
                if (this.options.showSparkline) {
                    var sparklineOptions = {
                        width: this.options.sparklineWidth + "px",
                        height: this.options.sparklineHeight + "px",
                        lineColor: this.options.headerColor,
                        fillColor: this.options.sparklineFillColor,
                        lineWidth: 1.5,
                        highlightLineColor: null,
                        spotRadius: 0,
                        barWidth: this.options.sparklineBarWidth,
                        barSpacing: this.options.sparklineBarSpacing,
                        tooltipFormatter: function (sparkline, options, fields) {
                            var type = sparkline.$el.data('type');
                            if (type == 'line') {
                                return _buildFormatter(sparkline, fields.x, fields.y);
                            }
                            if (type == 'bar' && fields[0]) {
                                return _buildFormatter(sparkline, fields[0].offset, fields[0].value);
                            }
                            return "";
                        }
                    };

                    $el.find('.inlinesparkline-line').sparkline('html', _.extend({}, sparklineOptions, { type: 'line' }));
                    $el.find('.inlinesparkline-bar').sparkline('html', _.extend({}, sparklineOptions, { type: 'bar' }));
                    $el.find('.inlinesparkline').sparkline('html', sparklineOptions);
                }

                function _buildFormatter(sparkline, x, y) {
                    if (y == null) { return ''; }
                    var time = sparkline.$el.data('time');
                    var timeTitle = time.split(",")[x];
                    var unit = sparkline.$el.data('unit');
                    var value = Istac.widget.helper.addThousandSeparator(y);
                    return "<strong>" + value + " " + unit + "</strong><br>" + timeTitle;
                }
            },

            orderDatasetsBySelectionOrder: function (datasets) {
                var selectedOptions = this.options.groupType === 'system' ? this.options.instances : this.options.indicators;
                return _.sortBy(datasets, function (dataset) {
                    return _.indexOf(selectedOptions, dataset.request.id);
                }, this);
            },

            renderTable: function (datasets) {
                var context = {};
                context.measures = _.map(this.measures, function (measure) {
                    return measuresLabels[measure];
                });

                datasets = this.orderDatasetsBySelectionOrder(datasets);

                context.datasets = _.map(datasets, this.parseDataset, this);

                _.each(context.datasets, function (dataset, i) {
                    dataset.isOdd = i % 2 === 1;
                    dataset.cssClass = dataset.isOdd ? "odd" : "even";
                });


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
