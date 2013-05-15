(function () {

    Istac.widget.NaturalScale = {
        scale : function (options) {
            var ymax = options.ymax;
            var ymin = options.ymin;

            var rango = Math.abs(ymax - ymin);
            var n = 0;
            while (Math.pow(10, n) < rango) {
                n = n + 1;
            }

            var m;
            if (rango < 5 * Math.pow(10, n - 1)) {
                m = Math.pow(10, n - 1);
            } else {
                m = 5 * Math.pow(10, n - 1) / 2;
            }

            var ydown;
            if (ymin >= 0) {
                ydown = Math.floor(ymin / m) * m;
            } else {
                ydown = (Math.ceil(ymin / m) - 1) * m;
            }

            var ytop;
            if (ymax > 0) {
                ytop = (Math.floor(ymax / m) + 1) * m;
            } else {
                ytop = Math.ceil(ymax / m) * m;
            }

            if (ydown === ymin && ymin !== 0) {
                ydown = ymin - 1;
            }

            if (ytop === ymax && ymax !== 0) {
                ytop = ymax - 1;
            }

            var ranges = Math.abs(ytop - ydown) / m;
            return {ytop : ytop, ydown : ydown, ranges : ranges};
        }
    };

}());