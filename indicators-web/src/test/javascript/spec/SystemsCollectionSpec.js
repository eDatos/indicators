describe("Systems Collection", function(){

    it("fetch", function(){
        var systemsCollection = new SystemsCollection();

        var req = systemsCollection.fetchCurrentPage();
        waitsFor(function() {
            return req.isResolved();
        }, "Request not completed", 10000);
        runs(function(){
            expect(systemsCollection.size()).toEqual(2);
        });
    })

});