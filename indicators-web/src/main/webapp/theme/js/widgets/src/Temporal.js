(function ($) {

    Highcharts.setOptions({
        lang : {
            months : ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
                'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
            weekdays : ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sabado'],
            shortMonths : ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
            thousandsSep : '.',
            decimalPoint : ','
        }
    });

    Istac.widget.Temporal = function (options) {
        this.init(options);
    };

    Istac.widget.Temporal.prototype = _.extend({}, Istac.widget.Base.prototype, {

        parse : function (dataset) {
            var locale = this.locale;

            var measureValue = this.measures[0];
            var timeValues = dataset.getTimeValues();
            var timeValuesTitles = dataset.getTimeValuesTitles(locale);
            var geographicalValues = this.geographicalValues;
            var geographicalValuesTitles = dataset.getGeographicalValuesTitles(locale);

            var legend = {};
            var values = [];
            var series = [];

            for (var i = 0; i < geographicalValues.length; i++) {
                var geoValue = geographicalValues[i];
                var geoValueTitle = geographicalValuesTitles[geoValue];

                var data = [];
                var tooltip = [];
                for (var j = 0; j < timeValues.length; j++) {
                    var timeValue = timeValues[j];
                    var value = dataset.getObservation(geoValue, timeValue, measureValue);
                    var valueStr = dataset.getObservationStr(geoValue, timeValue, measureValue);
                    var unit = dataset.getUnit(measureValue);

                    var date = Istac.widget.DateParser.parse(timeValue);

                    data.push([date, value]);
                    values.push(value);

                    if (_.isString(geoValueTitle)) {
                        geoValueTitle = geoValueTitle.trim();
                    }
                    tooltip.push('<strong>' + valueStr + ' ' + unit + '</strong><br/>' + geoValueTitle + '<br/>' + timeValuesTitles[timeValue]);
                }

                series.push({
                    name : geoValueTitle,
                    data : data,
                    tooltip : tooltip
                })
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

            var isNumber = function (n) {
                return _.isNumber(n)
            };

            var maxValue = _.chain(values).filter(isNumber).max().value();
            var minValue = _.chain(values).filter(isNumber).min().value();

            var chartData = {
                labels : labels,
                values : values,
                legend : legend,
                maxValue : maxValue,
                minValue : minValue,
                series : series
            };
            return chartData;
        },

        _getIndicatorInstanceCode : function () {
            var instances = this.options.instances;
            if (instances && instances.length > 0) {
                return instances[0];
            }
        },

        render : function () {
            this.el.addClass('istac-widget-temporal');
            if (this.datasets && this.datasets.length > 0) {
                var chartData = this.parse(this.datasets[0]);
                this.renderChart(chartData);
            }
            this.updateTitle();
        },

        chartColors : function (chartData) {
            var nSeries = _.keys(chartData.values).length;
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
            $chartContainer.css('height', 350);
            this.contentContainer.html($chartContainer);

            var highchartsOptions = {
                chart : {
                    type : 'line'
                },
                colors : Istac.widget.helper.colorPaletteGenerator(chartData.series.length),
                title : {
                    text : ''
                },
                xAxis : {
                    type : 'datetime'
                },
                yAxis : {
                    title : {
                        text : ''
                    }
                },
                tooltip : {
                    formatter : function () {
                        var index = _.indexOf(this.series.xData, this.x);
                        return chartData.series[this.series.index].tooltip[index];
                    }
                },
                legend : {
                    enabled : false
                },
                credits : {
                    enabled : false
                },
                exporting : {
                    enabled : false
                },
                plotOptions : {
                    line : {
                        animation : false,
                        marker : {
                            enabled : false
                        }
                    }
                },
                series : chartData.series
            };

            if (!this.showLabels) {
                _.extend(highchartsOptions.xAxis, {
                    lineWidth : 0,
                    minorGridLineWidth : 0,
                    lineColor : 'transparent',
                    labels : {
                        enabled : false
                    },
                    minorTickLength : 0,
                    tickLength : 0
                });
            }

            if (this.showLegend) {
                highchartsOptions.legend.enabled = true;
            }

            if (_.isFinite(chartData.minValue) && _.isFinite(chartData.maxValue)) {
                if (this.options.scale === "minmax") {
                    highchartsOptions.yAxis.tickPositioner = function () {
                        var step = (chartData.maxValue - chartData.minValue) / 4;
                        return _.range(chartData.minValue, chartData.maxValue + step, step);
                    }
                } else if (this.options.scale == "natural-lib") {
                    var min = chartData.minValue;
                    var max = chartData.maxValue;
                    if (min < 0 && max > 0) {
                        var limit = Math.max(Math.abs(min), Math.abs(max));
                        highchartsOptions.yAxis.min = -limit;
                        highchartsOptions.yAxis.max = +limit;
                    } else if (min < 0 && max < 0) {
                        highchartsOptions.yAxis.min = min;
                        highchartsOptions.yAxis.max = 0;
                    } else if (min > 0 && max > 0) {
                        highchartsOptions.yAxis.min = 0;
                        highchartsOptions.yAxis.max = max;
                    }
                } else if (this.options.scale == "natural") {
                    var scale = Istac.widget.NaturalScale.scale({ymin : chartData.minValue, ymax : chartData.maxValue });
                    highchartsOptions.yAxis.tickPositioner = function () {
                        var step = (chartData.maxValue - chartData.minValue) / scale.ranges;
                        return _.range(scale.ydown, scale.ytop, step);
                    };
                }
            }

            $chartContainer.highcharts(highchartsOptions);
        }

    });

}(jQuery));