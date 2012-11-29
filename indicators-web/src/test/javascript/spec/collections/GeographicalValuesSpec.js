describe("GeographicalValues", function () {
    it("should fetch by subjectCode", function () {
        var collection = new App.collections.GeographicalValues();
        var req = collection.fetchBySubjectCodeAndGeographicalGranularityCode("EDUCACION", "PAISES");

        waitsFor(function () {
            return req.isResolved();
        }, 1000);

        runs(function () {
            expect(collection.toJSON()).toEqual([]);
        });
    });

    it("should fetch by indicatorSystemCode", function () {
        var collection = new App.collections.GeographicalValues();
        var req = collection.fetchByIndicatorSystemCodeAndGeographicalGranularityCode("OPER_0001", "MUNICIPIOS");

        waitsFor(function () {
            return req.isResolved();
        }, 1000);

        runs(function () {
            expect(collection.toJSON()).toEqual([]);
        });
    });
});