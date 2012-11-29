describe("Indicators Collection", function () {

    it("should fetch the indicators list", function () {
        var indicators = new App.collections.Indicators();
        var req = indicators.fetch();

        waitsFor(function () {
            return req.isResolved();
        }, 2000);

        runs(function () {
            expect(indicators.size()).toEqual(5);
            var ids = indicators.pluck("code");
            expect(ids).toEqual(['CODE-2', 'CODE-1', 'CODE-5', 'CODE-4', 'CODE-3']);
        });

    });

});