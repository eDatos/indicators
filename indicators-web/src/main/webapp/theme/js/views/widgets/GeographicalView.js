/**
 *
 */
GeographicalView = SelectionView.extend({



    getLabel : function(geographicalValue){
        var res;
        if(geographicalValue.has('title')){
            res = getLabel(geographicalValue.get('title'));
        }else{
            res = geographicalValue.get('code');
        }

        return res;
    }

});
