(function () {

    Istac.widget.loader = {

        css : function (cssId, url) {
            var doc = document;
            if (!doc.getElementById(cssId)) {
                var head = doc.getElementsByTagName('head')[0];
                var link = doc.createElement('link');
                link.id = cssId;
                link.rel = 'stylesheet';
                link.type = 'text/css';
                link.href = url;
                link.media = 'all';
                head.appendChild(link);
            }
        },

        js : function (condition, url) {
            if (condition) {
                var head = document.getElementsByTagName('head')[0],
                    script = document.createElement('script');
                script.src = url;
                head.appendChild(script);
            }
        },

        all : function (url) {
            this.css('istac-widget-css', url + '/theme/js/widgets/widgets.css');
            this.js(!window.jQuery, url + '/theme/js/widgets/libs/jquery-1.7.1.js');
            this.js(!window.Raphael, url + '/theme/js/widgets/libs/raphael-min.js');
            this.js(!(window.jQuery && window.jQuery.elycharts), url + '/theme/js/widgets/libs/elycharts.min.js');
        }
    };

}());