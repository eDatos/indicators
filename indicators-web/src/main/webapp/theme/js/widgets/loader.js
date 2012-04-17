/**
 *
 */

function loadCSS(cssId, url){
    var doc = document;
    if (!doc.getElementById(cssId))
    {
        var head  = doc.getElementsByTagName('head')[0];
        var link  = doc.createElement('link');
        link.id   = cssId;
        link.rel  = 'stylesheet';
        link.type = 'text/css';
        link.href = url;
        link.media = 'all';
        head.appendChild(link);
    }
}

function lazyLoadJquery(){
    if(!window.jQuery){
        document.write('<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"><\/script>');
    }
}

loadCSS('istac-widget-css', 'http://localhost:8080/indicators-web/theme/css/widgets.css');
lazyLoadJquery();