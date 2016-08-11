/**
 * TemplateManager
 */

TemplateManager = function(){

    this.templates = {};

    this.loadTemplatesInPage = function(){
        var self = this;
        var templates = $('script[type="text/html"]');
        $.each(templates, function(i, template){
            var $template = $(template);
            var id = $template.attr('id');
            var content = $template.html();
            self.templates[id] = _.template(content);
        });
    },

    this.get = function(id){
        return this.templates[id];
    }

    return this;
}