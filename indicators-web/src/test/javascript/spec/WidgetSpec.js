describe("Widget", function(){

    it("fetch dataset structure and data", function(){
        var dataset = new IstacDataset();

        var finish = false;

        var request = dataset.fetch("Operacion01", "7ad3d5cb-50ef-4daa-966f-2876bbce0f62", function(){
            finish = true;
        });

        waitsFor(function(){
            return request.isResolved();
        }, "Request not completed", 10000);
        runs(function(){
            expect(dataset.structure).not.toBeNull();
            expect(dataset.data).not.toBeNull();
        });

    });

    it("should not fail on undefined callback", function(){
        var dataset = new IstacDataset();
        var request = dataset.fetch("Operacion01", "7ad3d5cb-50ef-4daa-966f-2876bbce0f62");
        waitsFor(function(){
            return request.isResolved();
        }, "Promise not resolved on undefined callback", 10000);
    });

    it("no fetch when undefined system id or indicator instance", function(){
        var dataset = new IstacDataset();

        var finish = false;
        var request = dataset.fetch(null, null, function(){
            finish = true;
        });

        waitsFor(function(){
            return request.isResolved();
        }, "", 10);
        runs(function(){
            expect(dataset.data).toEqual({});
            expect(dataset.structure).toEqual({});
        });
    });

    it("getValues", function(){
        var dataset = new IstacDataset();
        var finish = false;
        dataset.fetch("Operacion01", "7ad3d5cb-50ef-4daa-966f-2876bbce0f62", function(){
            finish = true;
        });

        waitsFor(function(){
            return finish;
        }, "Request not completed", 10000);
        runs(function(){
            expect(dataset.getTimeValues()).toEqual(["2006"]);
            expect(dataset.getGeographicalValues()).toEqual(["FR"]);
            expect(dataset.getLastTimeValue()).toEqual("2006");
        });
    });

    it("getValues fron undefined dataset", function(){
        var dataset = new IstacDataset();
        expect(dataset.getTimeValues()).toEqual([]);
        expect(dataset.getGeographicalValues()).toEqual([]);
        expect(dataset.getLastTimeValue()).toBeNull();
        expect(dataset.getObservation('ES', '2010', 'ABSOLUTE')).toBeNull();
    });

});