/**
 *
 */

var WidgetCodeView = Backbone.View.extend({

    initialize : function(){
        this.template = templateManager.get('codeTmpl');
        this.model.on('change', this.render, this);
    },

    render : function(){
        var model = this.model.toJSON();

        var code = [];

        code.push('<div id="istac-widget"></div>');
        code.push('<script src="' + serverURL + context + '/theme/js/widgets/widget.min.all.js"></script>');
        code.push('<script>');
        code.push('new IstacWidget({');

        var options = [];


        options.push('     el : "#istac-widget"');
        options.push('     type : "' + model.type + '"');
        options.push('     title : "' + model.title + '"');
        options.push('     width : ' + model.width);
        options.push('     borderColor : "' + model.borderColor + '"');
        options.push('     textColor : "' + model.textColor + '"');
        options.push('     systemId : "' + model.systemId + '"');
        options.push('     indicators : ' + JSON.stringify(model.indicators));
        options.push('     geographicalValues : ' + JSON.stringify(model.geographicalValues));
        options.push('     measures : ' + JSON.stringify(model.measures));
        options.push('     url : "' + serverURL  + context + '"');
        options.push('     jaxiUrl : "' + jaxiUrl + '"');

        if(model.type === "temporal"){
            var showLabels = model.showLabels === true;
            options.push('     showLabels : ' + showLabels);

            var showLegend = model.showLegend === true;
            options.push('     showLegend : ' + showLegend);
        }

        code.push(options.join(", \n"));
        code.push('});');
        code.push('</script>');

        var htmlCode = code.join('\n');
        this.$el.html(this.template({code : htmlCode}));
        return this;
    }
});