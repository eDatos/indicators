(function ($) {
    Istac.widget.Temporal = function (options) {
        this.init(options);
    };

    Istac.widget.Temporal.prototype = _.extend({}, Istac.widget.Base.prototype, {

        parse : function (dataset) {
            var self = this;

            var locale = this.locale;

            var measureValue = this.measures[0];
            var timeValues = dataset.getTimeValues();
            var timeValuesTitles = dataset.getTimeValuesTitles(locale);
            var geographicalValues = this.geographicalValues;
            var geographicalValuesTitles = dataset.getGeographicalValuesTitles(locale);

            var legend = {};
            var values = {};
            var tooltips = {};
            for (var i = 0; i < geographicalValues.length; i++) {
                var serie = 'serie' + i;
                var geoValue = geographicalValues[i];
                var geoValueTitle = geographicalValuesTitles[geoValue];

                var data = [];
                var tooltip = [];
                for (var j = 0; j < timeValues.length; j++) {
                    var timeValue = timeValues[j];
                    var value = dataset.getObservation(geoValue, timeValue, measureValue);
                    var valueStr = dataset.getObservationStr(geoValue, timeValue, measureValue);

                    var unit = dataset.getUnit(geoValue, timeValue, measureValue);

                    data.push(value);
                    tooltip.push('<div><strong>' + valueStr + ' ' + unit +'</strong></div><div>' + geoValueTitle + '</div><div>' + timeValuesTitles[timeValue] + '</div>');
                }

                legend[serie] = geoValueTitle;
                values[serie] = data;
                tooltips[serie] = tooltip;
            }

            //limit the number of labels (eje x)
            var totalLabels = 5;
            var division = Math.floor(timeValues.length / totalLabels);
            var labels = [];
            for (var i = 0; i < timeValues.length; i++) {
                if (i % division === 0) {
                    var timeValue = timeValues[i];
                    labels.push(timeValuesTitles[timeValue]);
                } else {
                    labels.push('');
                }
            }

            var maxValue = _.chain(values).values().flatten().compact().max().value();
            var minValue = _.chain(values).values().flatten().compact().min().value();

            var chartData = {
                labels : labels,
                values : values,
                legend : legend,
                tooltips : tooltips,
                maxValue : maxValue,
                minValue : minValue
            };
            return chartData;
        },


        _getIndicatorInstanceCode : function () {
            var instances = this.options.instances;
            if(instances && instances.length > 0) {
                return instances[0];
            }
        },

        render : function () {
            this.el.addClass('istac-widget-temporal');
            if(this.datasets && this.datasets.length > 0) {
                var chartData = this.parse(this.datasets[0]);
                this.renderChart(chartData);
            }
            this.updateTitle();
        },

        chartColors : function (chartData) {
            var nSeries = chartData.labels.length;
            var colors = Istac.widget.helper.colorPaletteGenerator(nSeries);

            var chartColors = {};
            _.each(colors, function (color, i) {
                chartColors["serie" + i] = { color : color};
            });

            return chartColors;
        },

        renderChart : function (chartData) {
            var $chartContainer = $('<div id="chart"></div>');
            $chartContainer.css('width', this.width - 20);
            $chartContainer.css('height', this.height);
            this.contentContainer.html($chartContainer);

            var renderData = chartData;
            var colors = this.chartColors(chartData);
            var legendElements = Istac.widget.helper.getKeys(chartData.legend);

            var chartWidth = this.width - 20;
            var chartHeight = 200;

            var legendWidth = this.width - 20;
            var legendHeight = this.showLegend ? legendElements.length * 20 : 0;

            var chartTopMargin = 20;
            var labelHeight = this.showLabels ? 40 : 10;
            var legendTop = chartTopMargin + chartHeight + labelHeight;

            var chartBottomMargin = legendHeight + labelHeight;
            var containerHeight = chartTopMargin + chartHeight + chartBottomMargin;

            var chartOptions = {
                type : "line",
                margins : [chartTopMargin, 1, chartBottomMargin, 65],
                defaultSeries : {
                    plotProps : {
                        "stroke-width" : 1.5
                    },
                    dot : true,
                    rounded : false,
                    stacked : false,
                    dotProps : {
                        r : 0,
                        stroke : "white",
                        "stroke-width" : 0,
                        opacity : 0
                    },
                    highlight : {
                        newProps : {
                            r : 3,
                            opacity : 1
                        },
                        overlayProps : {
                            fill : "white",
                            opacity : 0.2
                        }
                    },
                    tooltip : {
                        active : true,
                        width : 200,
                        height : 60,
                        roundedCorners : 5,
                        padding : [6, 6] /* y, x */,
                        offset : [20, 0] /* y, x */,
                        frameProps : { fill : "white", "stroke-width" : 1 },
                        contentStyle : { "font-family" : "Arial", "font-size" : "12px", "line-height" : "16px", color : "black" }
                    }
                },
                series : colors,
                defaultAxis : {
                    labels : true,
                    labelsDistance : 15
                },
                axis : {
                    l : {
                        labelsDistance : 5,
                        labelsFormatHandler : function (label) {
                            var res = label.toString().replace("\.", ",");
                            res = Istac.widget.helper.addThousandSeparator(res);
                            return res;
                        },
                        min : renderData.minValue,
                        max : renderData.maxValue
                    }
                },
                features : {
                    grid : {
                        draw : [true, false],
                        forceBorder : true,
                        props : {
                            "stroke-dasharray" : "-"
                        }
                    }
                },
                width : chartWidth,
                height : containerHeight,
                labels : renderData.labels,
                values : renderData.values,
                tooltips : renderData.tooltips
            };

            if (this.showLegend) {
                chartOptions.features.legend = {
                    width : legendWidth,
                    height : legendHeight,
                    x : 0,
                    y : legendTop,
                    dotType : "circle",
                    dotProps : {
                        stroke : "white",
                        "stroke-width" : 2
                    },
                    borderProps : {
                        fill : "#fff",
                        "stroke-width" : 0,
                        "stroke-opacity" : 0
                    },
                    textProps : { font: '10px Arial', fill: "#000" }
                }
                chartOptions.legend = renderData.legend;
            }

            if (!this.showLabels) {
                chartOptions.axis.x = {
                    labels : false
                }
            }

            $chartContainer.chart(chartOptions);

        }

    });

}(jQuery));