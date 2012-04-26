/**
 *
 */
describe("Indicators Collection", function(){

    it("fetch", function(){
        var indicatorsCollection  = new IndicatorsCollection();
        indicatorsCollection.setSystemId('Operacion01');

        var req = indicatorsCollection.fetch();

        waitsFor(function() {
            return req.isResolved();
        }, "Request not completed", 10000);
        runs(function(){
            expect(indicatorsCollection.size()).toEqual(4);
        });
    });

    function allDeferredDone(deferreds){
        for(var i = 0; i < deferreds.length; i++ ){
            if(!deferreds[i].isResolved()) return false;
        }
        return true;
    }

    it("Indicators geographical values", function(){
        var indicatorsCollection  = new IndicatorsCollection();
        indicatorsCollection.setSystemId('Operacion01');

        var allIndicatorInstancesFetched = false;
        var req = indicatorsCollection.fetch();

        waitsFor(function(){
            return req.isResolved();
        }, "Indicators instances not completed", 10000);

        runs(function(){
            var requests = [req];
            indicatorsCollection.each(function(indicator){
                if(!indicator.isFetched()){
                    var req = indicator.fetch();
                    requests.push(req);
                }
            });

            waitsFor(function() {
                return allDeferredDone(requests);
            }, "Request not completed", 10000);
            runs(function(){
                var geographicalValues = indicatorsCollection.getGeographicalValues();
                expect(geographicalValues.length).toEqual(2);
                expect(geographicalValues[0].code).toEqual('ES');
                expect(geographicalValues[1].code).toEqual('FR');
            });
        })

    });

});