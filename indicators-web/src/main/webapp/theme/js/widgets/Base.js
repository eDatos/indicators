/**
 *
 */

var Istac = Istac || {};
Istac.Widgets = Istac.Widgets || {};

(function ($, undefined) {
    Istac.Widgets.Base = Class.extend({
        _defaultOptions : {
            title : 'default title',
            width : 300,
            height : 400,
            borderColor : '#3478B0',
            textColor : '#000000',
            indicators : [],
            visibleData : {absolute : true, interanual : true, interperiodic : true}
        },

        init : function (options) {
            console.log('base init');

            options = options || defaultOptions;
            this.el = $(options.el);

            //TODO indicator data from webservice

            this.indicators = options.indicators || [];
            this.visibleData = options.visibleData || this._defaultOptions.visibleData;

            //Create containers
            //TODO empty the container
            this.titleContainer = $('<div class="istac-widget-title"></div>');
            this.contentContainer = $('<div class="istac-widget-content"></div>');
            this.el.html(this.titleContainer).append(this.contentContainer);
            this.el.addClass("istac-widget");
            this.el.addClass(this.containerClass);

            //Initialize style
            this.setTextColor(options.textColor);
            this.setBorderColor(options.borderColor);
            this.setTitle(options.title);
            this.setWidth(options.width);

            //Render the table content
            this.render();
        },

        setTextColor : function (textColor) {
            this.textColor = textColor;
            this.el.css('color', textColor);
        },

        _getContrast50 : function (hexcolor) {
            if (hexcolor && hexcolor.length > 0) {
                if (hexcolor[0] === '#') {
                    hexcolor = hexcolor.substring(1);
                }
                return (parseInt(hexcolor, 16) > 0xffffff / 2) ? '#333' : 'white';
            }
        },

        setBorderColor : function (borderColor) {
            this.borderColor = borderColor;

            this.titleContainer.css('background-color', borderColor);
            var contrastColor = this._getContrast50(borderColor);
            this.titleContainer.css('color', contrastColor);
            this.el.css('border-color', borderColor);
        },

        setWidth : function (width) {
            this.width = width;
            this.el.css('width', width);
        },

        setTitle : function (title) {
            this.title = title;
            this.titleContainer.text(title);
        },

        setIndicators : function (indicators) {
            this.indicators = indicators;
            this.render();
        },

        setVisibleData : function (visibleData) {
            this.visibleData = visibleData;
            this.render();
        },

        render : function () {
            console.log('base render');
        }
    });
})(jQuery);