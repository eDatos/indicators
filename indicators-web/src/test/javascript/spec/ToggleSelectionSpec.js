describe("ToggleSelectionSpec", function(){

    it("defaults values", function(){
        var toggleSelection = new ToggleSelection();
        expect(toggleSelection.max).toEqual(0);
    });

    it("constructor", function(){
        var toggleSelection = new ToggleSelection({max : 3});
        expect(toggleSelection.max).toEqual(3);
    });

    it("multiple selection", function(){
        var toggleSelection = new ToggleSelection();
        toggleSelection.select("a");

        expect(toggleSelection.selection.length).toEqual(1);
        expect(toggleSelection.selection[0]).toEqual("a");

        toggleSelection.select("b");
        expect(toggleSelection.selection.length).toEqual(2);
        expect(toggleSelection.selection[0]).toEqual("a");
        expect(toggleSelection.selection[1]).toEqual("b");

        toggleSelection.select("a");
        expect(toggleSelection.selection.length).toEqual(1);
        expect(toggleSelection.selection[0]).toEqual("b");

        toggleSelection.select("b");
        expect(toggleSelection.selection.length).toEqual(0);
    });

    it("single selection", function(){
        var toggleSelection = new ToggleSelection({max : 1});
        var selected;

        selected = toggleSelection.select("a");
        expect(selected).toBeTruthy();
        expect(toggleSelection.selection.length).toEqual(1);
        expect(toggleSelection.selection[0]).toEqual("a");

        selected = toggleSelection.select("b");
        expect(selected).toBeTruthy();
        expect(toggleSelection.selection.length).toEqual(1);
        expect(toggleSelection.selection[0]).toEqual("b");

        selected =toggleSelection.select("a");
        expect(selected).toBeTruthy();
        expect(toggleSelection.selection.length).toEqual(1);
        expect(toggleSelection.selection[0]).toEqual("a");

        selected = toggleSelection.select("a");
        expect(selected).toBeFalsy();
        expect(toggleSelection.selection.length).toEqual(0);
    });

    it("events", function(){
        var toggleSelection = new ToggleSelection();
        var callback = jasmine.createSpy();
        toggleSelection.on("change", callback);
        toggleSelection.select("a");
        expect(callback).toHaveBeenCalled();
    });

    it("change max size", function(){
        var toggleSelection = new ToggleSelection();
        toggleSelection.select('a');
        toggleSelection.select('b');
        toggleSelection.select('c');

        expect(toggleSelection.selection.length).toEqual(3);

        toggleSelection.setMax(1);

        expect(toggleSelection.selection.length).toEqual(1);
        expect(toggleSelection.selection[0]).toEqual("a");
    });

    it("selection with discriminator", function(){
        var toggleSelection = new ToggleSelection({discriminator : 'id'});
        var a = new Backbone.Model({id : 'a'});
        var b = new Backbone.Model({id : 'b'});

        toggleSelection.select(a);
        toggleSelection.select(b);
        expect(toggleSelection.selection.length).toEqual(2);
        expect(toggleSelection.selection[0].get('id')).toEqual('a');
        expect(toggleSelection.selection[1].get('id')).toEqual('b');

        toggleSelection.select(b);
        expect(toggleSelection.selection.length).toEqual(1);
        expect(toggleSelection.selection[0].get('id')).toEqual('a');
    });

});