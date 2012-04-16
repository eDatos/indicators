/**
 *
 */

Istac.Widgets.Factory = function(options){
    options = options || {};

    console.log('Factory ' + options.type);

    if(options.type === 'temporal'){
        return new Istac.Widgets.Temporal(options);
    }else{
        return new Istac.Widgets.LastData(options);
    }
};