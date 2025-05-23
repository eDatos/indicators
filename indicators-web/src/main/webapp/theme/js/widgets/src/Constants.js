(function ($, _) {
    "use strict";

    Istac.widget.Constants = {
        colors: {
            istacBlueWhite: "#B3D9FF",
            istacBlueLight: "#56B5E6",
            istacBlueMedium: "#1C547E",
            istacBlueDark: "#003366",
            istacYellow: '#EBCC5C',

            istacWhite: "#FFFFFF",
            istacGreyLight: "#EBEBEB",
            istacGreyMedium: "#ACACAC",
            istacGreyDark: "#808080",
            istacBlack: "#222222",

            hiddenText: "#FFFFFD"
        },

        font: {
            family: {
                sansSerif: "Helvetica,Arial,sans-serif",
                serif: "serif"
            },
            body: {
                size: "11px"
            },
            title: {
                size: "13px"
            }
        },

        visualization: {
            type: {
                INDICATOR: "indicator",
                INDICATOR_INSTANCE: "indicatorInstance",
                DATASET: "dataset",
                QUERY: "query"
            }
        },

        metadata: {
            defaultDecimals: 2
        },

        attributes: {
            attachmentLevels: {
                PRIMARY_MEASURE: "PRIMARY_MEASURE",
                DIMENSION: "DIMENSION",
                DATASET: "DATASET"
            }
        },

        maxUrlQueryLength: 1700,

        dimension: {
            sortBy: {
                OBSERVATION: "OBSERVATION",
                REPRESENTATION: "REPRESENTATION"
            }
        },

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
