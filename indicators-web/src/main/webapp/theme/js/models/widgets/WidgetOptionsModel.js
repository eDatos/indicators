/**
 *
 */

var WidgetOptionsModel = Backbone.Model.extend({
    defaults : {
        title : 'Poner aquí el título del widget',
        type : 'lastData',
        width : 300,
        borderColor : '#0058b0',
        textColor : '#000000',
        systemId : '',
        indicators : [],
        measures : [],
        geographicalValues : [],
        showLabels : false,
        showLegend : false
    }
});
