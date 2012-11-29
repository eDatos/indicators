/**
 *
 */
describe("Indicators Instances Collection", function () {

    var SYSTEM_ID = "OPER_0001";

    it("fetch", function () {
        var indicatorsCollection = new App.collections.IndicatorsInstances();

        var req = indicatorsCollection.fetchBySystemCodeAndGeographicalValueCode(SYSTEM_ID, 'ES');

        waitsFor(function () {
            return req.isResolved();
        }, "Request not completed", 10000);

        runs(function () {
            expect(indicatorsCollection.size()).toEqual(2);
        });
    });

});