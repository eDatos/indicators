describe("Systems Collection", function () {

    it("fetch", function () {
        var systemsCollection = new App.collections.IndicatorSystems();

        var req = systemsCollection.fetch();
        waitsFor(function () {
            return req.isResolved();
        }, "Request not completed", 10000);
        runs(function () {
            expect(systemsCollection.size()).toEqual(1);
        });
    });

});