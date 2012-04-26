/**
 *
 */

var IndicatorsView = SelectionView.extend({
    getLabel : function(item){
        return getLabel(item.get('title'));
    }
});



