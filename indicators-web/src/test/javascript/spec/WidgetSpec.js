describe("Widget", function(){

    var apiUrl = "http://rcorrod:8070/indicators-web/api/indicators/v1.0",
        timeout = 4000,
        system = "operacion0001",
        indicator = "2fc7012d-9509-4444-b4cd-5f752a86e005";

    it("fetch dataset structure and data", function(){
        var dataset = new IstacDataset(apiUrl);

        var finish = false;

        var request = dataset.fetch(system, indicator, function(){
            finish = true;
        });

        waitsFor(function(){
            return request.isResolved();
        }, "Request not completed", timeout);
        runs(function(){
            expect(dataset.structure).not.toBeNull();
            expect(dataset.data).not.toBeNull();
        });
    });

    it("should not fail on undefined callback", function(){
        var dataset = new IstacDataset(apiUrl);
        var request = dataset.fetch(system, indicator);
        waitsFor(function(){
            return request.isResolved();
        }, "Promise not resolved on undefined callback", timeout);
    });

    it("no fetch when undefined system id or indicator instance", function(){
        var dataset = new IstacDataset(apiUrl);

        var finish = false;
        var request = dataset.fetch(null, null, function(){
            finish = true;
        });

        waitsFor(function(){
            return request.isResolved();
        }, "", timeout);
        runs(function(){
            expect(dataset.data).toEqual({});
            expect(dataset.structure).toEqual({});
        });
    });

    /*
     TODO No make a dataset request
     it("getValues", function(){
     var dataset = new IstacDataset(apiUrl);
     var finish = false;
     dataset.fetch(system, indicator, function(){
     finish = true;
     });

     waitsFor(function(){
     return finish;
     }, "Request not completed", 100);
     runs(function(){
     expect(dataset.getTimeValues()).toEqual(["2006"]);
     expect(dataset.getGeographicalValues()).toEqual(["FR"]);
     expect(dataset.getLastTimeValue()).toEqual("2006");
     });
     });
     */

    it("getValues fron undefined dataset", function(){
        var dataset = new IstacDataset(apiUrl);
        expect(dataset.getTimeValues()).toEqual([]);
        expect(dataset.getGeographicalValues()).toEqual([]);
        expect(dataset.getLastTimeValue()).toBeNull();
        expect(dataset.getObservation('ES', '2010', 'ABSOLUTE')).toBeNull();
    });

    it("getGeographicalValuesTitles", function(){
        dataset = new IstacDataset(apiUrl);
        var request = dataset.fetch(system, indicator);
        waitsFor(function(){
            return request.isResolved();
        }, "Request not completed", timeout);
        runs(function(){
            var geographicalTitles = dataset.getGeographicalValuesTitles("es");
            expect(geographicalTitles.ES702).toEqual("  Santa Cruz de Tenerife");
            expect(geographicalTitles.ES521).toEqual("  Alicante/Alacant");

            var geographicalTitles = dataset.getGeographicalValuesTitles("en");
            expect(geographicalTitles.ES702).toEqual("  Santa Cruz de Tenerife");
            expect(geographicalTitles.ES521).toEqual("  Alicante/Alacant");

            var geographicalTitles = dataset.getGeographicalValuesTitles("ru");
            expect(geographicalTitles.ES702).toEqual("  Santa Cruz de Tenerife");
            expect(geographicalTitles.ES521).toEqual("  Alicante/Alacant");
        });
    });

    it("getTimeValuesTitles", function(){
        dataset = new IstacDataset(apiUrl);
        var request = dataset.fetch(system, indicator);
        waitsFor(function(){
            return request.isResolved();
        }, "Request not completed", timeout);
        runs(function(){
            var timeTitles = dataset.getTimeValuesTitles("es");
            expect(timeTitles["2011M01"]).toEqual("2011 Enero");
            expect(timeTitles["2002M05"]).toEqual("2002 Mayo");

            var timeTitles = dataset.getTimeValuesTitles("en");
            expect(timeTitles["2011M01"]).toEqual("2011 January");
            expect(timeTitles["2002M05"]).toEqual("2002 May");

            var timeTitles = dataset.getTimeValuesTitles("ru");
            expect(timeTitles["2011M01"]).toEqual("2011 January");
            expect(timeTitles["2002M05"]).toEqual("2002 May");
        });
    });

    it("parse temporal", function(){
        var widget = new IstacWidgetTemporal({
            el : "#istac-widget",
            type : "temporal",
            title : "Istac Widget",
            width : 613,
            borderColor : "#3478B0",
            textColor : "#000000",
            systemId : system,
            indicators : [indicator],
            geographicalValues : ["ES111"],
            measures : ["ABSOLUTE"],
            url : "http://rcorrod:8070/indicators-web"
        });


        var dataset = new IstacDataset(apiUrl);
        var request = dataset.fetch(system, indicator);

        waitsFor(function(){
            return request.isResolved();
        }, "Request not completed", timeout);
        runs(function(){
            console.log(dataset);
            var chartData = widget.parse(dataset);
            console.log(chartData);
        });
    });

});