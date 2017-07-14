//Analytics
(function (i, s, o, g, r, a, m) {
i['GoogleAnalyticsObject'] = r; i[r] = i[r] || function () {
    (i[r].q = i[r].q || []).push(arguments)
}, i[r].l = 1 * new Date(); a = s.createElement(o),
    m = s.getElementsByTagName(o)[0]; a.async = 1; a.src = g; m.parentNode.insertBefore(a, m)
})(window, document, 'script', 'https://www.google-analytics.com/analytics.js', 'ga');

(function() {

    Istac.widget.analytics = {};

    Istac.widget.analytics.trackPageView = function(options) {
        ga('create', Istac.widget.configuration["metamac.analytics.google.tracking_id"], { 'alwaysSendReferrer': true });
        ga('set', 'referrer', window.location.href);
        ga('set', 'location', buildExampleUrl(options));
        ga('send', 'pageview');
    };
    
    function buildExampleUrl(options) {
        return options.url + '/widgets/example?options=' + JSON.stringify(options);
    }
}());
