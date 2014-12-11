(function($) {

    Istac.widget.helper = {};

    Istac.widget.helper.tooltip = function($el, text, tooltipId) {
        var $tooltip = $(".istact-widget-tooltip");

        $tooltip = $('<p class="istact-widget-tooltip ' + tooltipId + '"></p>');
        $("body").append($tooltip);

        var xOffset = 10;
        var yOffset = 20;

        if (text) {
            $tooltip.text(text);

            var fnHoverIn = function(e) {
                $tooltip.css("top", (e.pageY - xOffset) + "px").css("left", (e.pageX + yOffset) + "px").fadeIn("fast");
            };
            var fnHoverOut = function() {
                $tooltip.fadeOut("fast");
            };

            $el.hover(fnHoverIn, fnHoverOut);
            $el.mousemove(function(e) {
                $tooltip.css("top", (e.pageY - xOffset) + "px").css("left", (e.pageX + yOffset) + "px");
            });
        }
    };

    Istac.widget.helper.addThousandSeparator = function(nStr) {
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

    Istac.widget.helper.getHost = function(url) {
        var l = document.createElement("a");
        l.href = url;
        return l.host;
    };

    Istac.widget.helper.absoluteUrl = function(url) {
        var a = document.createElement('a');
        a.href = url;
        return a.href;
    };

    Istac.widget.helper.getKeys = function(hash) {
        var keys = [];
        for ( var i in hash) {
            if (hash.hasOwnProperty(i)) {
                keys.push(i);
            }
        }
        return keys;
    };

    Istac.widget.helper.firstKey = function(hash) {
        var key;
        for ( var i in hash) {
            if (hash.hasOwnProperty(i)) {
                key = i;
                break;
            }
        }
        return key;
    };

    Istac.widget.helper.firstValue = function(hash) {
        var key = this.firstKey(hash);
        var result;
        if (key) {
            result = hash[key];
        }
        return result;
    }

    var colorHueDistributor = function(n) {
        var result = [ 0 ];
        var total = 400;
        var divisions = 3;
        var step = total / divisions;
        var offset = 0;

        for (var i = 1; i < n; i++) {
            if ((i != 0) && ((i * step) % total === 0)) {
                offset = step / 2;
            }
            result[i] = (result[i - 1] + step + offset) % total;
            if ((i != 0) && ((i * step) % total === 0)) {
                step = step / 2;
            }
        }

        result = _.map(result, function(color) {
            return color / total;
        });

        return result;
    };

    var hsvToRgb = function(h, s, v) {
        var r, g, b;

        var i = Math.floor(h * 6);
        var f = h * 6 - i;
        var p = v * (1 - s);
        var q = v * (1 - f * s);
        var t = v * (1 - (1 - f) * s);

        switch (i % 6) {
            case 0:
                r = v, g = t, b = p;
                break;
            case 1:
                r = q, g = v, b = p;
                break;
            case 2:
                r = p, g = v, b = t;
                break;
            case 3:
                r = p, g = q, b = v;
                break;
            case 4:
                r = t, g = p, b = v;
                break;
            case 5:
                r = v, g = p, b = q;
                break;
        }

        return [ r * 255, g * 255, b * 255 ];
    };

    var componentToHex = function(c) {
        var hex = c.toString(16);
        return hex.length == 1 ? "0" + hex : hex;
    };

    var rgbToHex = function(r, g, b) {
        return "#" + componentToHex(r) + componentToHex(g) + componentToHex(b);
    };

    Istac.widget.helper.colorPaletteGenerator = function(n) {
        var hues = colorHueDistributor(n);
        return _.map(hues, function(hue) {
            var rgb = hsvToRgb(hue, 0.84, 0.84);
            rgb = _.map(rgb, function(component) {
                return Math.round(component);
            });
            return rgbToHex.apply(null, rgb);
        });
    }

}(jQuery));