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
        code.push('     el : "#istac-widget",');
        code.push('     type : "' + model.type + '",');
        code.push('     title : "' + model.title + '",');
        code.push('     width : ' + model.width + ',');
        code.push('     borderColor : "' + model.borderColor + '",');
        code.push('     textColor : "' + model.textColor + '",');
        code.push('     systemId : "' + model.systemId + '",');
        code.push('     indicators : ' + JSON.stringify(model.indicators) + ',');
        code.push('     geographicalValues : ' + JSON.stringify(model.geographicalValues) + ',');
        code.push('     measures : ' + JSON.stringify(model.measures) + ', ');
        code.push('     url : "' + serverURL  + context + '" ');
        code.push('});');
        code.push('</script>');

        var htmlCode = code.join('\n');
        this.$el.html(this.template({code : htmlCode}));
        return this;
    }
});