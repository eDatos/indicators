(function ($) {

    Istac.widget.helper = {};

    Istac.widget.helper.tooltip = function ($el, text) {
        var $tooltip = $(".istact-widget-tooltip");

        // Lazy init
        if ($tooltip.length === 0) {
            $tooltip = $('<p class="istact-widget-tooltip"></p>');
            $("body").append($tooltip);
        }

        var xOffset = 10;
        var yOffset = 20;

        if (text) {
            $tooltip.text(text);

            var fnHoverIn = function (e) {
                $tooltip
                    .css("top", (e.pageY - xOffset) + "px")
                    .css("left", (e.pageX + yOffset) + "px")
                    .fadeIn("fast");
            };
            var fnHoverOut = function () {
                $tooltip.fadeOut("fast");
            };

            $el.hover(fnHoverIn, fnHoverOut);
            $el.mousemove(function (e) {
                $tooltip
                    .css("top", (e.pageY - xOffset) + "px")
                    .css("left", (e.pageX + yOffset) + "px");
            });
        }
    };

    Istac.widget.helper.addThousandSeparator = function (nStr) {
        nStr += '';
        var x = nStr.split(',');
        var x1 = x[0];
        var x2 = x.length > 1 ? ',' + x[1] : '';
        var rgx = /(\d+)(\d{3})/;
        while (rgx.test(x1)) {
            x1 = x1.replace(rgx, '$1' + '.' + '$2');
        }
        return x1 + x2;
    };

    Istac.widget.helper.getHost = function (url) {
        var l = document.createElement("a");
        l.href = url;
        return l.host;
    };

    Istac.widget.helper.getKeys = function (hash) {
        var keys = [];
        for (var i in hash) {
            if (hash.hasOwnProperty(i)) {
                keys.push(i);
            }
        }
        return keys;
    };

    Istac.widget.helper.firstKey = function (hash) {
        var key;
        for (var i in hash) {
            if (hash.hasOwnProperty(i)) {
                key = i;
                break;
            }
        }
        return key;
    };

    Istac.widget.helper.firstValue = function (hash) {
        var key = this.firstKey(hash);
        var result;
        if (key) {
            result = hash[key];
        }
        return result;
    }

}(jQuery));