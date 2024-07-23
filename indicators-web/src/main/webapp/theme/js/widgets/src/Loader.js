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

        js : function (condition, url, options) {
            if (condition) {
                var head = document.getElementsByTagName('head')[0];
                var script = document.createElement('script');
                Object.keys(options).forEach(function(key) {
                    script.setAttribute(key, options[key]);
                });
                script.src = url;
                head.appendChild(script);
            }
        },

        all : function (url) {
            this.css('istac-widget-css', url + '/theme/js/widgets/widgets.css');
        }
    };

}());