/**
 *
 */

var WidgetCodeView = Backbone.View.extend({
    initialize : function(){
        this.model.on('change', this.render, this);
        this.render();
    },

    render : function(){
        var model = this.model.toJSON();

        var code = [];
        code.push('<div id="istac-widget"></div>');
        code.push('<script src="http://istac.com....../widgets/widget.min.js"></script>');
        code.push('<script>');
        code.push('new IstacWidgets({');
        code.push('     el : "#istac-widget",');
        code.push('     type : "' + model.type + '",');
        code.push('     title : "' + model.title + '",');
        code.push('     width : ' + model.width + ',');
        code.push('     borderColor : "' + model.borderColor + '",');
        code.push('     textColor : "' + model.textColor + '",');
        code.push('     systemId : "' + model.systemId + '",');
        code.push('     indicators : ' + JSON.stringify(model.indicators) + ',');
        code.push('     geographicalValues : ' + JSON.stringify(model.geographicalValues) + ',');
        code.push('     measures : ' + JSON.stringify(model.measures) + ' ');
        code.push('});');
        code.push('</script>');
        $(this.el).text(code.join('\n'));
    }
});