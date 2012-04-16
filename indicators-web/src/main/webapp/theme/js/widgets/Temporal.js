/**
 *
 */

Istac.Widgets.Temporal = Istac.Widgets.Base.extend({
    init : function (options) {
        this._super(options);

        //TODO debounce not using underscore library (Copy the code?)
        this.render = _.debounce(this.render, 300);
    },

    setWidth : function (width) {
        this._super(width);
        this.render();
    },

    render : function () {
        var $chartContainer = $('<div id="chart"></div>');
        $chartContainer.css('width', this.width);
        $chartContainer.css('height', this.height);
        this.contentContainer.html($chartContainer);

        var width = this.width;
        var legendWith = 80;
        var legendMargin = 20;
        $chartContainer.chart({
            type : "line",
            margins : [10, 10, 20, 50],
            defaultSeries : {
                plotProps : {
                    "stroke-width" : 4
                },
                dot : true,
                dotProps : {
                    stroke : "white",
                    "stroke-width" : 2
                }
            },
            series : {
                serie1 : {
                    color : "#3478B0"
                },
                serie2 : {
                    color : "#EBCC5C"
                }
            },
            defaultAxis : {
                labels : true
            },
            features : {
                grid : {
                    draw : [true, false],
                    props : {
                        "stroke-dasharray" : "-"
                    }
                },
                legend : {
                    horizontal : false,
                    width : legendWith,
                    height : 50,
                    x : width - legendWith - legendMargin,
                    y : 220,
                    dotType : "circle",
                    dotProps : {
                        stroke : "white",
                        "stroke-width" : 2
                    },
                    borderProps : {
                        opacity : 0.3,
                        fill : "#c0c0c0",
                        "stroke-width" : 0,
                        "stroke-opacity" : 0
                    }
                }
            },
            width : this.width,
            height : 400,
            tooltips : {
                serie1 : ["a", "b", "c", "d"],
                serie2 : ["a", "b", "c", "d"]
            },
            values : {
                serie1 : [28, 30, 62, 56],
                serie2 : [28, 15, 57, 81]
            },
            legend : {
                serie1 : "Serie 1",
                serie2 : "Serie 2"
            }
        });

    }
});