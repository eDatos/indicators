(function ($, _, echarts) {

    Istac.widget.Temporal = function (options) {
        this.init(options);
    };

    Istac.widget.Temporal.prototype = _.extend({}, Istac.widget.Base.prototype, {

        parse: function (dataset) {
            var locale = this.locale;

            var measureValue = this.measures[0];
            var timeValues = dataset.getTimeValues();
            var timeValuesTitles = dataset.getTimeValuesTitles(locale);
            var geographicalValuesTitles = dataset.getGeographicalValuesTitles(locale);

            var values = [];
            var series = [];

            for (var i = 0; i < this.geographicalValues.length; i++) {
                var geoValue = this.geographicalValues[i];
                var geoValueTitle = geographicalValuesTitles[geoValue];
                if (_.isString(geoValueTitle)) {
                    geoValueTitle = geoValueTitle.trim();
                }

                var data = [];
                for (var j = 0; j < timeValues.length; j++) {
                    var timeValue = timeValues[j];
                    var value = dataset.getObservation(geoValue, timeValue, measureValue) || null;
                    var valueStr = dataset.getObservationStr(geoValue, timeValue, measureValue);
                    var unit = dataset.getUnit(measureValue, locale, this.options.defaultLocale);
                    var date = Istac.widget.DateParser.parse(timeValue);

                    data.push({
                        value: [date, value],
                        tooltip: {
                            formatter: '<strong>' + valueStr + ' ' + unit + '</strong><br/>' + geoValueTitle + '<br/>' + timeValuesTitles[timeValue]
                        }
                    });
                    values.push(value);
                }

                series.push({
                    name: geoValueTitle,
                    data: data,
                    type: 'line',
                    symbol: 'circle'
                });
            }

            return {
                maxValue: _.chain(values).filter(_.isNumber).max().value(),
                minValue: _.chain(values).filter(_.isNumber).min().value(),
                series: series
            };
        },

        render: function () {
            this.el.addClass('istac-widget-temporal');
            if (this.datasets && this.datasets.length > 0) {
                var chartData = this.parse(this.datasets[0]);
                this.renderChart(chartData);
            }
            this.updateTitle();

            this.onAfterRender();
        },

        renderChart: function (chartData) {
            var $chartContainer = $('<div id="' + this.getChartId() + '"></div>');
            $chartContainer.css('width', this.width - 20);
            $chartContainer.css('height', 250);
            this.contentContainer.html($chartContainer);
            this.chart = echarts.init($chartContainer[0], null, { renderer: 'canvas' });

            var echartsOptions = {
                color: Istac.widget.helper.colorPaletteGenerator(chartData.series.length),
                xAxis: {
                    type: 'time',
                    axisLabel: {
                        show: this.showLabels
                    },
                    axisTick: {
                        show: this.showLabels,
                        alignWithLabel: true
                    }
                },
                yAxis: {
                },
                legend: {
                    show: this.showLegend,
                    textStyle: {
                        fontSize: 10
                    }
                },
                animation: false,
                tooltip: {
                    trigger: 'item'
                },
                series: chartData.series
            };

            if (_.isFinite(chartData.minValue) && _.isFinite(chartData.maxValue)) {
                if (this.options.scale === "minmax") {
                    echartsOptions.yAxis.tickPositioner = function () {
                        var step = (chartData.maxValue - chartData.minValue) / 4;
                        return _.range(chartData.minValue, chartData.maxValue + step, step);
                    }
                } else if (this.options.scale === "natural-lib") {
                    var min = chartData.minValue;
                    var max = chartData.maxValue;
                    if (min < 0 && max > 0) {
                        var limit = Math.max(Math.abs(min), Math.abs(max));
                        echartsOptions.yAxis.min = -limit;
                        echartsOptions.yAxis.max = +limit;
                    } else if (min < 0 && max < 0) {
                        echartsOptions.yAxis.min = min;
                        echartsOptions.yAxis.max = 0;
                    } else if (min > 0 && max > 0) {
                        echartsOptions.yAxis.min = 0;
                        echartsOptions.yAxis.max = max;
                    }
                } else if (this.options.scale === "natural") {
                    var scale = Istac.widget.NaturalScale.scale({ ymin: chartData.minValue, ymax: chartData.maxValue });
                    echartsOptions.yAxis.tickPositioner = function () {
                        var step = (scale.ytop - scale.ydown) / scale.ranges;
                        return _.range(scale.ydown, scale.ytop + step, step);
                    };
                }
            }

            this.chart.setOption(echartsOptions, true);
        },

        getChartId: function () {
            return this.options.id ? 'chart-' + this.options.id : 'chart';
        }
    });

}(window.jQuery, window._, echarts));
