(function ($, _, echarts) {

    Istac.widget.Temporal = function (options) {
        this.init(options);

        var self = this;
        const updateChartSize = _.debounce(_.bind(this._updateChartSize, this), 200);
        const resizeObserver = new ResizeObserver(function (entries) {
            entries.forEach(function (entry) {
                if (entry.target === self.el[0]) {
                    updateChartSize();
                }
            });
        });

        resizeObserver.observe(this.el[0]);
    };

    Istac.widget.Temporal.prototype = _.extend({}, Istac.widget.Base.prototype, {

        CHART_TOOLTIP_BORDER_RADIUS: 4,

        parse: function (dataset) {
            var locale = this.locale;

            var measureValue = this.measures[0];
            this.timeRepresentations = dataset.getTimeRepresentations();
            var timeValuesTitles = dataset.getTimeValuesTitles(locale);
            var geographicalValues = Object.keys(dataset.data.dimension.GEOGRAPHICAL.representation.index);
            var geographicalValuesTitles = dataset.getGeographicalValuesTitles(locale);

            var values = [];
            var series = [];
            var colors = Istac.widget.helper.colorPaletteGenerator(geographicalValues.length);

            for (var i = 0; i < geographicalValues.length; i++) {
                var geoValue = geographicalValues[i];
                var geoValueTitle = geographicalValuesTitles[geoValue];
                if (_.isString(geoValueTitle)) {
                    geoValueTitle = geoValueTitle.trim();
                }

                var data = [];
                for (var j = 0; j < this.timeRepresentations.length; j++) {
                    var timeValue = this.timeRepresentations[j].code;
                    var value = dataset.getObservation(geoValue, timeValue, measureValue);
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

        _updateChartSize: function () {
            if (!this.chart) {
                return;
            }
            var chartContainer = $("#" + this.getChartId())
            this.chart.resize({
                width: chartContainer.width(),
                height: chartContainer.height()
            });
        },

        renderChart: function (chartData) {
            this.$chartContainer = $('<div id="' + this.getChartId() + '"></div>');
            this.$chartContainer.css('width', this.width - 20);
            this.$chartContainer.css('height', 250);
            this.contentContainer.html(this.$chartContainer);

            echarts.registerLocale("es", EDatos.common.I18n.translate("ECHARTS", "es"));
            echarts.registerLocale("ca", EDatos.common.I18n.translate("ECHARTS", "ca"));
            this.chart = echarts.init(this.$chartContainer[0], null, { renderer: 'canvas', locale: this.options.locale });

            const self = this;
            const representations = this.timeRepresentations;
            const parsedValues = representations.map(function (representation) {
                return Istac.widget.DateParser.parse(representation.code, self.options.timeGranularities[0]);
            });

            var echartsOptions = {
                xAxis: {
                    type: 'time',
                    axisLabel: {
                        show: this.showLabels,
                        hideOverlap: true,
                        rotate: 70,
                        align: "right",
                        verticalAlign: "top",
                        width: 50,
                        overflow: "truncate",
                        customValues: parsedValues,
                        formatter: function (epoch) {
                            // For some reason, formatter does not receive only the customValues setted before, but also
                            // more values that are not in the customValues array, probably intercalated by eCharts itself
                            const index = parsedValues.indexOf(epoch);

                            // The idea is to show the label of the original value, not the parsed one (which is an epoch).
                            // Only the labels of the values present in the graph will be shown.
                            if (index > -1) {
                                return self._getLabel(representations[index].title, self.options.locale);
                            }
                        }
                    },
                    axisTick: {
                        show: this.showLabels,
                        alignWithLabel: true
                    }
                },
                yAxis: {
                    axisLabel: {
                        formatter: this._axisLabelFormatter
                    }
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
                    itemWidth: 16,
                    itemHeight: Istac.widget.Constants.charts.legend.itemHeight,
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


            var gridBottom = echartsOptions.legend.bottom + (this.showLegend ? this._getLegendHeight() + Istac.widget.Constants.charts.gap : 0);

            var extendedChartOptions = {
                xAxis: {
                    axisLabel:{
                        width: this._getXAxisLabelWidth(this._getChartDomEl().clientHeight - gridBottom - echartsOptions.grid.top) + 20,
                    }
                }
            }
            Istac.widget.helper.deepExtend(echartsOptions, extendedChartOptions);

            if (_.isFinite(chartData.minValue) && _.isFinite(chartData.maxValue)) {
                if (this.options.scale === "minmax") {
                    echartsOptions.yAxis.min = chartData.minValue;
                    echartsOptions.yAxis.max = chartData.maxValue;
                    echartsOptions.yAxis.interval = (chartData.maxValue - chartData.minValue) / 4;
                } else if (this.options.scale === "natural") {
                    var scale = Istac.widget.NaturalScale.scale({ ymin: chartData.minValue, ymax: chartData.maxValue });
                    echartsOptions.yAxis.min = scale.ydown;
                    echartsOptions.yAxis.max = scale.ytop;
                    echartsOptions.yAxis.interval = (scale.ytop - scale.ydown) / scale.ranges;
                }
            }

            this.chart.setOption(echartsOptions, true);
            this.chart.getZr().on("mousemove", function (p) {
                self.mouseCoords = [p.offsetX, p.offsetY];
            });
            this.chart.on("globalout", function() {
                self.chart.dispatchAction({type: 'unselect', seriesIndex: self.selectedSeriesIndex, dataIndex: self.selectedDataIndex});
            });
        },

        _getClosestSeriesToMouse: function (seriesParams) {
            if (!this.mouseCoords) {
                return seriesParams[0];
            }
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
        },

        _axisLabelFormatter: function (value) {
            if (isNaN(value)) {
                return value;
            }
            var axisLabel = value.toString().replace("\.", ",");
            axisLabel = Istac.widget.helper.addThousandSeparator(axisLabel)
            return axisLabel;
        },

        _getLabel : function (iString, locale) {
            if (iString) {
                return iString[locale] || iString["__default__"];
            }
        },

        _getXAxisLabelWidth(gridHeight) {
            return Math.min(Istac.widget.Constants.charts.axis.maxWidth, (gridHeight)/4);
        },

        _getChartDomEl: function () {
            return this.$chartContainer ? this.$chartContainer[0] : undefined;
        },

        _getLegendHeight: function () {
            return Istac.widget.Constants.charts.legend.itemHeight + Istac.widget.Constants.charts.legend.padding * 2;
        },
    });

}(window.jQuery, window._, window.echarts));
