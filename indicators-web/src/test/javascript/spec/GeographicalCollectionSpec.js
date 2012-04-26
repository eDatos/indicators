/**
 *
 */


describe("GeographicalCollection", function(){

    it("get element by code", function(){
        var collection = new GeographicalCollection();
        collection.reset([
            {code : "FR",
                latitude : 12.5656233,
                longitude : -15.46464565},
            {code : "ES",
                latitude : 12.5656233,
                longitude : -15.46464565}
        ]);

        var item = collection.get('ES');
        expect(item.id).toEqual('ES');

        var item = collection.get('FR');
        expect(item.id).toEqual('FR');
    });

});