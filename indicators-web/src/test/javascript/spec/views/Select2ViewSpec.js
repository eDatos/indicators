describe("Select2View", function () {

    var $el, collection, view;

    beforeEach(function () {
        $el = $('');
        collection = new Backbone.Collection([
            {code : 'c1', title : 'C1'},
            {code : 'c2', title : 'C2'}
        ]);

        view = new App.views.Select2View({
            el : $el,
            collection : collection,
            idAttribute : 'code',
            textAttribute : 'title',
            multiple : false,
            width : "600px"
        });
    });

    afterEach(function () {
        $el.remove();
    });

    var getSelectData = function () {
        return view.$input.data().select2.opts.data.results;
    };

    it("should render collection on create", function () {
        var data = getSelectData();
        expect(_.pluck(data, 'code')).toEqual(collection.pluck('code'));
    });

    it("should update the input data automatically", function () {
        var data = getSelectData();
        expect(_.pluck(data, 'code'), collection.pluck('code'));

        collection.reset([
            {code : 'c3', title : 'C3'}
        ]);
        data = getSelectData();
        expect(_.pluck(data, 'code')).toEqual(collection.pluck('code'));
    });

    it("should trigger change event on input change", function () {
        var changeSpy = jasmine.createSpy();
        view.on('change', changeSpy);
        view.$input.trigger('change');
        expect(changeSpy).toHaveBeenCalled();
    });

});
