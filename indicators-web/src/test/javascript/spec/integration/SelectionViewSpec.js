describe("SelectionView", function(){

    var click = function($el, i){
        var $aLink = $('.selectable-item:eq('+ i + ')', $el);
        $aLink.click();
        return $aLink;
    }

    var collection = new Backbone.Collection();
    collection.reset([
        {id : 0, name : 'a'},
        {id : 1, name : 'b'},
        {id : 2, name : 'c'},
        {id : 3, name : 'd'}
    ]);

    it("single selection", function(){
        var view = new SelectionView({max : 1, collection : collection});
        view.render();

        expect($('.selected', view.$el).length).toEqual(0);

        click(view.$el, 0);
        expect(view.selection().length).toEqual(1);
        expect(view.selection()[0].id).toEqual(0);

        expect($('.selected', view.$el).length).toEqual(1);

        click(view.$el, 3);
        expect(view.selection().length).toEqual(1);
        expect(view.selection()[0].id).toEqual(3);

        expect($('.selected', view.$el).length).toEqual(1);
    });

    it("multiple selection", function(){
        var view = new SelectionView({collection : collection});
        view.render();

        var $clicked;
        $clicked = click(view.$el, 0);
        expect(view.selection().length).toEqual(1);
        expect(view.selection()[0].id).toEqual(0);

        expect($('.selected', view.$el).length).toEqual(1);

        $clicked = click(view.$el, 3);
        expect(view.selection().length).toEqual(2);
        expect(view.selection()[0].id).toEqual(0);
        expect(view.selection()[1].id).toEqual(3);

        expect($('.selected', view.$el).length).toEqual(2);
    });

});