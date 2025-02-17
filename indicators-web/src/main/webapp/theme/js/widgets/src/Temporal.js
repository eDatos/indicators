(function ($, _, echarts) {

    Istac.widget.Temporal = function (options) {
        this.init(options);
    };

    Istac.widget.Temporal.prototype = _.extend({}, Istac.widget.Base.prototype, {

        CHART_TOOLTIP_BORDER_RADIUS: 4,

        parse: function (dataset) {
            var locale = this.locale;

            var measureValue = this.measures[0];
            var timeValues = dataset.getTimeValues();
            var timeValuesTitles = dataset.getTimeValuesTitles(locale);
            var geographicalValuesTitles = dataset.getGeographicalValuesTitles(locale);

            var values = [];
            var series = [];
            var colors = Istac.widget.helper.colorPaletteGenerator(this.geographicalValues.length);

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
                    var date = Istac.widget.DateParser.parse(timeValue, this.options.timeGranularities[0]);

                    data.push({
                        value: [date, value],
                        tooltip: {
                            formatter: '<div style="border: 1px solid ' + colors[i] + ';padding: 10px;border-radius: ' + this.CHART_TOOLTIP_BORDER_RADIUS + 'px;">' +
                                '<strong>' + valueStr + ' '  + unit + '</strong>' +
                                '<br/>' +
                                geoValueTitle +
                                '<br/>' +
                                timeValuesTitles[timeValue] +
                            '</div>'
                        }
                    });
                    values.push(value);
                }

                series.push({
                    name: geoValueTitle,
                    data: data,
                    type: 'line',
                    symbol: 'circle',
                    showAllSymbol: true,
                    selectedMode: 'single',
                    select: {
                        disabled: false,
                        itemStyle: {
                            opacity: 1
                        }
                    },
                    emphasis: {
                        disabled: true // Disabled until this issue is fixed: https://github.com/apache/echarts/issues/19958
                    },
                    itemStyle: {
                        opacity: 0,
                        color: colors[i]
                    },
                    lineStyle: {
                        color: colors[i]
                    }
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
                xAxis: {
                    type: 'time',
                    axisLabel: {
                        show: this.showLabels,
                        hideOverlap: true
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
                    selectedMode: false,
                    type: 'scroll',
                    animationDurationUpdate: 100,
                    left: "center",
                    width: "80%",
                    bottom: 10,
                    borderWidth: 1,
                    borderColor: "#909090",
                    borderRadius: 5,
                    animation: true,
                    orient: 'horizontal',
                    pageTextStyle: {
                        fontWeight: 'bold'
                    },
                    textStyle: {
                        fontSize: 10
                    },
                    itemWidth: 16
                },
                animation: false,
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'none'
                    },
                    extraCssText: 'padding: 0px; border-width: 0px;border-radius: ' + this.CHART_TOOLTIP_BORDER_RADIUS + 'px;',
                    formatter: function (seriesParams) {
                        var closesSerieToMouse = self._getClosestSeriesToMouse(seriesParams);
                        self._selectLineData(closesSerieToMouse.seriesIndex, closesSerieToMouse.dataIndex);
                        return echartsOptions.series[closesSerieToMouse.seriesIndex].data[closesSerieToMouse.dataIndex].tooltip.formatter;
                    }
                },
                grid: {
                    top: 15,
                    right: 10,
                    bottom: this.showLegend ? 50 : 10,
                    left: 20,
                    containLabel: true
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

            var self = this;
            this.chart.setOption(echartsOptions, true);
            this.chart.getZr().on("mousemove", function (p) {
                self.mouseCoords = [p.offsetX, p.offsetY];
            });
            this.chart.on("globalout", function() {
                self.chart.dispatchAction({type: 'unselect', seriesIndex: self.selectedSeriesIndex, dataIndex: self.selectedDataIndex});
            });
        },

        _getClosestSeriesToMouse: function (seriesParams) {
            var mouseChartCoords = this.chart.convertFromPixel('grid', this.mouseCoords);
            return seriesParams.reduce(function(closestSerieToMouse, serie) {
                return !!closestSerieToMouse && Math.abs(closestSerieToMouse.value[1] - mouseChartCoords[1]) < Math.abs(serie.value[1] - mouseChartCoords[1])
                    ? closestSerieToMouse
                    : serie;
            });
        },

        _selectLineData: function (seriesIndex, dataIndex) {
            this.chart.dispatchAction({type: 'unselect', seriesIndex: this.selectedSeriesIndex, dataIndex: this.selectedDataIndex});
            this.selectedSeriesIndex = seriesIndex;
            this.selectedDataIndex = dataIndex;
            this.chart.dispatchAction({type: 'select', seriesIndex: seriesIndex, dataIndex: dataIndex});
        },

        getChartId: function () {
            return this.options.id ? 'chart-' + this.options.id : 'chart';
        }
    });

}(window.jQuery, window._, echarts));
