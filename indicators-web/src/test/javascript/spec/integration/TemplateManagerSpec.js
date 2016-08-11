describe("Template Manager", function(){

    function insertTemplate(id){
        $('body').append('<script id="' + id +'" type="text/html">Content ' + id + '</script>');
    }

    it("should load all templates in page", function(){
        insertTemplate('tmpl1');
        insertTemplate('tmpl2');
        insertTemplate('tmpl3');

        var templateManager = new TemplateManager();
        templateManager.loadTemplatesInPage();

        expect(templateManager.get('tmpl1')()).toEqual('Content tmpl1');
        expect(templateManager.get('tmpl2')()).toBeDefined('Content tmpl2');
        expect(templateManager.get('tmpl3')()).toBeDefined('Content tmpl3');


        $('#tmpl1').remove();
        $('#tmpl2').remove();
        $('#tmpl3').remove();
    });
});