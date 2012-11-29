describe("GeographicalGranularities", function () {

    it("fetch", function () {
        var collection = new App.collections.GeographicalGranularities();
        var req = collection.fetch();

        waitsFor(function () {
            return req.isResolved();
        }, 1000);

        runs(function () {
            var expected = [
                { "code" : "COUNTRIES", "title" : "Países"},
                {"code" : "REGIONS", "title" : "Comunidades autónomas"},
                {"code" : "PROVINCES", "title" : "Provincias"},
                {"code" : "MUNICIPALITIES", "title" : "Municipios"}
            ];
            expect(collection.toJSON()).toEqual(expected);
        });
    });

});