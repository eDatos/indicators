(function ($, _) {
    "use strict";

    Istac.widget.Constants = {

        charts: {
            gap: 5,
            axis: {
                rotate: 70,
                width: 50,
                maxWidth: 190
            },
            grid: {
                top: 15,
                right: 10,
                left: 20,
                bottomWithLegend: 50,
                bottomWithoutLegend: 10
            },
            legend: {
                animationDuration: 100,
                fontSize: 10,
                padding: 5,
                itemWidth: 16,
                itemHeight: 15,
                bottom: 10,
                width: "80%",
                border: {
                    width: 1,
                    color: "#909090",
                    radius: 5,
                }
            },
            tooltip: {
                maxWidth: 300,
                border: {
                    radius: 4,
                }
            }
        }
    };

}());
