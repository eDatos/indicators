var DatasetRequestBuilder = Istac.widget.DatasetRequestBuilder;

describe("DatasetRequestBuilder", function () {

    var apiUrl = 'http://www.api.com';
    var options = {};
    var datasetRequestBuilder;

    beforeEach(function () {

        options.selectedInstances = {
            type : 'lastData',
            groupType : 'system',
            instances : ['INS1', 'INS2'],
            indicators : [],
            indicatorSystem : 'System1',
            subjectCode : '',
            nrecent : 0,
            geographicalValues : ['ES']
        };

        options.selectedIndicators = {
            type : 'lastData',
            groupType : 'subject',
            instances : [],
            indicators : ['IN1', 'IN2'],
            indicatorSystem : '',
            subjectCode : 'Subject1',
            nrecent : 0,
            geographicalValues : ['ES']
        };

        options.recentInstances = {
            type : 'recent',
            groupType : 'system',
            instances : [],
            indicators : [],
            indicatorSystem : 'System1',
            subjectCode : '',
            nrecent : 4,
            geographicalValues : ['ES']
        };

        options.recentIndicators = {
            type : 'recent',
            groupType : 'subject',
            instances : [],
            indicators : [],
            indicatorSystem : '',
            subjectCode : 'Subject1',
            nrecent : 4,
            geographicalValues : ['ES']
        };

        options.temporal = {
            type : 'temporal',
            groupType : 'system',
            instances : ['INS1'],
            indicators : [],
            indicatorSystem : 'System1',
            subjectCode : '',
            nrecent : 0,
            geographicalValues : ['ES', 'EN', 'RU']
        };

        datasetRequestBuilder = new DatasetRequestBuilder({apiUrl : apiUrl});
    });

    it("should prepare request for selected instances", function () {
        var request = datasetRequestBuilder.request(options.selectedInstances);
        expect(request).toEqual(apiUrl + '/indicatorsSystems/System1/indicatorsInstances/?q=id IN ("INS1", "INS2")&fields=%2Bdata,%2Bmetadata&representation=GEOGRAPHICAL[ES]');
    });

    it("should prepare request for selected instance", function () {
        options.selectedInstances.instances = ['INS1'];
        var request = datasetRequestBuilder.request(options.selectedInstances);
        expect(request).toEqual(apiUrl + '/indicatorsSystems/System1/indicatorsInstances/?q=id EQ "INS1"&fields=%2Bdata,%2Bmetadata&representation=GEOGRAPHICAL[ES]');
    });

    it("should prepare request for selected instances different instances", function () {
        options.selectedInstances.instances = ['a', 'b'];
        var request = datasetRequestBuilder.request(options.selectedInstances);
        expect(request).toEqual(apiUrl + '/indicatorsSystems/System1/indicatorsInstances/?q=id IN ("a", "b")&fields=%2Bdata,%2Bmetadata&representation=GEOGRAPHICAL[ES]');
    });

    it("should prepare request for selected instances different indicatorSystem", function () {
        options.selectedInstances.indicatorSystem = 'System2';
        var request = datasetRequestBuilder.request(options.selectedInstances);
        expect(request).toEqual(apiUrl + '/indicatorsSystems/System2/indicatorsInstances/?q=id IN ("INS1", "INS2")&fields=%2Bdata,%2Bmetadata&representation=GEOGRAPHICAL[ES]');
    });

    it("should prepare request for selected indicators", function () {
        var request = datasetRequestBuilder.request(options.selectedIndicators);
        expect(request).toEqual(apiUrl + '/indicators/?q=id IN ("IN1", "IN2")&fields=%2Bdata,%2Bmetadata&representation=GEOGRAPHICAL[ES]');
    });

    it("should prepare request for selected indicator", function () {
        options.selectedIndicators.indicators = ['IN1'];
        var request = datasetRequestBuilder.request(options.selectedIndicators);
        expect(request).toEqual(apiUrl + '/indicators/?q=id EQ "IN1"&fields=%2Bdata,%2Bmetadata&representation=GEOGRAPHICAL[ES]');
    });

    it("should prepare request for different selected indicators", function () {
        options.selectedIndicators.indicators = ['a', 'b'];
        var request = datasetRequestBuilder.request(options.selectedIndicators);
        expect(request).toEqual(apiUrl + '/indicators/?q=id IN ("a", "b")&fields=%2Bdata,%2Bmetadata&representation=GEOGRAPHICAL[ES]');
    });

    it("should prepare request for lastupdated instances filtered by geographicalValue and systemCode", function () {
        var request = datasetRequestBuilder.request(options.recentInstances);
        expect(request).toEqual(apiUrl + '/indicatorsSystems/System1/indicatorsInstances/?q=geographicalValue EQ "ES"&order=update DESC, id DESC&limit=4&fields=%2Bdata,%2Bmetadata&representation=GEOGRAPHICAL[ES]');
    });

    it("should prepare request for lastupdated instances filtered by geographicalValue and systemCode with different systemCode", function () {
        options.recentInstances.indicatorSystem = 'System2';
        var request = datasetRequestBuilder.request(options.recentInstances);
        expect(request).toEqual(apiUrl + '/indicatorsSystems/System2/indicatorsInstances/?q=geographicalValue EQ "ES"&order=update DESC, id DESC&limit=4&fields=%2Bdata,%2Bmetadata&representation=GEOGRAPHICAL[ES]');
    });

    it("should prepare request for lastupdated instances filtered by geographicalValue and systemCode with different geographicalValue", function () {
        options.recentInstances.geographicalValues = ['EN'];
        var request = datasetRequestBuilder.request(options.recentInstances);
        expect(request).toEqual(apiUrl + '/indicatorsSystems/System1/indicatorsInstances/?q=geographicalValue EQ "EN"&order=update DESC, id DESC&limit=4&fields=%2Bdata,%2Bmetadata&representation=GEOGRAPHICAL[EN]');
    });

    it("should prepare request for lastupdated instances filtered by geographicalValue and systemCode with different limit", function () {
        options.recentInstances.nrecent = 20;
        var request = datasetRequestBuilder.request(options.recentInstances);
        expect(request).toEqual(apiUrl + '/indicatorsSystems/System1/indicatorsInstances/?q=geographicalValue EQ "ES"&order=update DESC, id DESC&limit=20&fields=%2Bdata,%2Bmetadata&representation=GEOGRAPHICAL[ES]');
    });

    it("should prepare request for lastupdated indicators filtered by geographicalValue and subjectCode", function () {
        var request = datasetRequestBuilder.request(options.recentIndicators);
        expect(request).toEqual(apiUrl + '/indicators/?q=subjectCode EQ "Subject1" AND geographicalValue EQ "ES"&order=update DESC, id DESC&limit=4&fields=%2Bdata,%2Bmetadata&representation=GEOGRAPHICAL[ES]');
    });

    it("should prepare request for lastupdated indicators filtered by geographicalValue and subjectCode with different subjectCode", function () {
        options.recentIndicators.subjectCode = 'Subject2';
        var request = datasetRequestBuilder.request(options.recentIndicators);
        expect(request).toEqual(apiUrl + '/indicators/?q=subjectCode EQ "Subject2" AND geographicalValue EQ "ES"&order=update DESC, id DESC&limit=4&fields=%2Bdata,%2Bmetadata&representation=GEOGRAPHICAL[ES]');
    });

    it("should prepare request for lastupdated indicators filtered by geographicalValue and subjectCode with different geographicalValue", function () {
        options.recentIndicators.geographicalValues = ['EN'];
        var request = datasetRequestBuilder.request(options.recentIndicators);
        expect(request).toEqual(apiUrl + '/indicators/?q=subjectCode EQ "Subject1" AND geographicalValue EQ "EN"&order=update DESC, id DESC&limit=4&fields=%2Bdata,%2Bmetadata&representation=GEOGRAPHICAL[EN]');
    });

    it("should prepare request for lastupdated indicators filtered by geographicalValue and subjectCode with different limit", function () {
        options.recentIndicators.nrecent = 20;
        var request = datasetRequestBuilder.request(options.recentIndicators);
        expect(request).toEqual(apiUrl + '/indicators/?q=subjectCode EQ "Subject1" AND geographicalValue EQ "ES"&order=update DESC, id DESC&limit=20&fields=%2Bdata,%2Bmetadata&representation=GEOGRAPHICAL[ES]');
    });

    it("should prepare request for temporal", function () {
        var request = datasetRequestBuilder.request(options.temporal);
        expect(request).toEqual(apiUrl + '/indicatorsSystems/System1/indicatorsInstances/?q=id EQ "INS1"&fields=%2Bdata,%2Bmetadata&representation=GEOGRAPHICAL[ES|EN|RU]');
    });

    it("should prepare request for temporal with different instances", function () {
        options.temporal.instances = ['INS2'];
        var request = datasetRequestBuilder.request(options.temporal);
        expect(request).toEqual(apiUrl + '/indicatorsSystems/System1/indicatorsInstances/?q=id EQ "INS2"&fields=%2Bdata,%2Bmetadata&representation=GEOGRAPHICAL[ES|EN|RU]');
    });

    describe("invalid parameters", function () {
        it("should return undefined if no type", function () {
            options.selectedInstances.type = undefined;
            var request = datasetRequestBuilder.request(options.selectedInstances);
            expect(request).toBeUndefined();
        });

        it("should return undefined if invalid type", function () {
            options.selectedInstances.type = 'INVALID';
            var request = datasetRequestBuilder.request(options.selectedInstances);
            expect(request).toBeUndefined();
        });

        it("should return undefined if type !== temporal and no groupType", function () {
            options.selectedInstances.groupType = 'INVALID';
            var request = datasetRequestBuilder.request(options.selectedInstances);
            expect(request).toBeUndefined();
        });

        it("should return undefined if groupType = system and no indicatorSystem", function () {
            options.selectedInstances.indicatorSystem = undefined;
            var request = datasetRequestBuilder.request(options.selectedInstances);
            expect(request).toBeUndefined();
        });

        it("should return undefined if no instances", function () {
            options.selectedInstances.instances = [];
            var request = datasetRequestBuilder.request(options.selectedInstances);
            expect(request).toBeUndefined();
        });

        it("should return undefined if groupType = subject and no indicators", function () {
            options.selectedIndicators.indicators = undefined;
            var request = datasetRequestBuilder.request(options.selectedIndicators);
            expect(request).toBeUndefined();
        });

        it("should return undefined if type=recent and no nrecent", function () {
            options.recentInstances.nrecent = undefined;
            var request = datasetRequestBuilder.request(options.recentInstances);
            expect(request).toBeUndefined();
        });

        it("should return undefined if type=recent and no indicatorSystem", function () {
            options.recentInstances.indicatorSystem = undefined;
            var request = datasetRequestBuilder.request(options.recentInstances);
            expect(request).toBeUndefined();
        });

        it("should return undefined if type=recent and no geogrpahicalValues", function () {
            options.recentInstances.geographicalValues = [];
            var request = datasetRequestBuilder.request(options.recentInstances);
            expect(request).toBeUndefined();
        });

        it("should return undefined if type=recent and no subjectCode", function () {
            options.recentIndicators.subjectCode = undefined;
            var request = datasetRequestBuilder.request(options.recentIndicators);
            expect(request).toBeUndefined();
        });

        it("should return undefined if type=recent and no geographicalValues", function () {
            options.recentIndicators.geographicalValues = [];
            var request = datasetRequestBuilder.request(options.recentIndicators);
            expect(request).toBeUndefined();
        });

        it("should return undefined if type=temporal and no indicator system", function () {
            options.temporal.indicatorSystem = undefined;
            var request = datasetRequestBuilder.request(options.temporal);
            expect(request).toBeUndefined();
        });

        it("should return undefined if type=temporal and no instances", function () {
            options.temporal.instances = [];
            var request = datasetRequestBuilder.request(options.temporal);
            expect(request).toBeUndefined();
        });

    })

});