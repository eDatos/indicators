(function ($, _) {
    "use strict";

    Istac.widget.Constants = {

        charts: {
            gap: 5,
            bigGap: 15,
            dataAxisWidth: 1.5,
            axis: {
                fontSize: 11,
                maxWidth: 190
            },
            grid: {
                horizontalMargin: 15,
                topMargin: 10
            },
            lines: {
                symbol: {
                    size: 5
                },
                xAxisTicks: 6,
                dataZoom: {
                    horizontal: {
                        id: "xAxisDataZoom",
                        height: 80,
                        widgetHeight: 20,
                        end: 100,
                        partialEnd: 48,
                        bottom: 8
                    },
                    vertical: {
                        id: "yAxisDataZoom",
                        width: 80,
                        widgetWidth: 20,
                        bottomMargin: 20,
                        topMargin: 8,
                        end: 100
                    }
                },
                highlight: {
                    point: {
                        border: {
                            width: 7,
                            color: "#ACACAC88"
                        }
                    }
                }
            },
            tooltipLines: {
                tooltip: {
                    fontSize: 11
                }
            },
            legend: {
                fontSize: 12,
                exportFontSize: 10,
                padding: 5,
                itemWidth: 16,
                itemHeight: 15,
                gap: 6
            },
            title: {
                fontSize: 9,
                marginRight: 10
            },
            tooltip: {
                className: 'chart-tooltip',
                exportClassName: 'export-chart-tooltip',
                padding: 7,
                fontSize: 12,
                maxWidth: 300,
                borderWidth: 1
            }
        }
    };

}());
