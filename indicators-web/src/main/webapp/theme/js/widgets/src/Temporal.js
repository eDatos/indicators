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
                    tooltip.push('<div>' + geoValueTitle + '</div><div>' + timeValuesTitles[timeValue] + '</div><div>' + valueStr + ' ' + unit +'</div>');
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

            var chartData = {
                labels : labels,
                values : values,
                legend : legend,
                tooltips : tooltips
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
        },

        chartColors : function (chartData) {
            var result = {
                serie0 : {
                    color : "#4F81BD"
                },
                serie1 : {
                    color : "#FFC000"
                },
                serie2 : {
                    color : "#92D050"
                },
                serie3 : {
                    color : "#F79646"
                },
                serie4 : {
                    color : "#C00000"
                },
                serie5 : {
                    color : "#8064A2"
                },
                serie6 : {
                    color : "#808080"
                },
                serie7 : {
                    color : "#00B0F0"
                }
            };
            return result;
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
                margins : [chartTopMargin, 20, chartBottomMargin, 50],
                defaultSeries : {
                    plotProps : {
                        "stroke-width" : 1
                    },
                    dot : false,
                    dotProps : {
                        stroke : "white",
                        "stroke-width" : 2
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
                    labelsDistance : 20
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
                    }
                }
                chartOptions.legend = renderData.legend;
            }

            if (!this.showLabels) {
                chartOptions.axis = {
                    x : {
                        labels : false
                    }
                }
            }

            $chartContainer.chart(chartOptions);

        }

    });

}(jQuery));