describe('Date Parser', function () {

    function testParse (input, output) {
        expect(Istac.widget.DateParser.parse(input)).toEqual(output);
        var d1 = new Date(Istac.widget.DateParser.parse(input));
        var d2 = new Date(output);
        if (d1.getTime() !== d2.getTime()) {
            console.log(d1, d2);
        }

    }

    var date = function (year, month, day, hour = 0, minute = 0, second = 0) {
        return moment({year : year, month : month, day : day, hour : hour, minute : minute, second : second}).utc().valueOf();
    };

    it('should format yearly date', function () {
        testParse("1999", date(1999, 0, 1));
    });

    it('should format biyearly date', function () {
        testParse("1999H1", date(1999, 5, 1));
        testParse("1999H2", date(1999, 11, 1));
    });

    it('should format Quarterly date', function () {
        testParse("1999Q1", date(1999, 2, 1));
        testParse("1999Q2", date(1999, 5, 1));
        testParse("1999Q3", date(1999, 8, 1));
        testParse("1999Q4", date(1999, 11, 1));
    });
    
    it('should format Four monthly date', function () {
        testParse("1999T1", date(1999, 3, 1));
        testParse("1999T2", date(1999, 7, 1));
        testParse("1999T3", date(1999, 11, 1));
    });

    it('should format Montly date', function () {
        testParse("1999M01", date(1999, 0, 1));
        testParse("1999M02", date(1999, 1, 1));
        testParse("1999M09", date(1999, 8, 1));
        testParse("1999M12", date(1999, 11, 1));
        testParse("2000M02", date(2000, 1, 1)); //año bisiesto
    });

    it('should format Weekly date', function () {
        testParse("2013W02", date(2013, 0, 6));
        testParse("2013W20", date(2013, 4, 12));
    });

    it('should format Daily date', function () {
        testParse("20130113", date(2013, 0, 13));
        testParse("20131209", date(2013, 11, 9));
    });
    
    it('should format Hourly date', function () {
        testParse("2013-07-24T13:21:52", date(2013, 6, 24, 13, 21, 52));
        testParse("2013-01-01T00:00:00", date(2013, 0, 1, 0, 0, 0));
        testParse("2013-01-31T23:59:59", date(2013, 0, 31, 23, 59, 59));
        testParse("2013-12-31T23:59:59", date(2013, 11, 31, 23, 59, 59));
        testParse("2019-08-28T11:30:59", date(2019, 7, 28, 11, 30, 59));
    });

});