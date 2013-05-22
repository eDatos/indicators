describe("NaturalScale", function () {

    it("scale test1", function () {
        var result = Istac.widget.NaturalScale.scale({ymin : 2517, ymax : 56340});
        expect(result).toEqual({ytop : 75000, ydown : 0, ranges : 3});
    });

    it("scale test2", function () {
        var result = Istac.widget.NaturalScale.scale({ymin : -26.33, ymax : 29.82});
        expect(result).toEqual({ytop : 50, ydown : -50, ranges : 4});
    });

    it("scale test3", function () {
        var result = Istac.widget.NaturalScale.scale({ymin : 63, ymax : 188});
        expect(result).toEqual({ytop : 200, ydown : 50, ranges : 3});
    });

    it("scale test4", function () {
        var result = Istac.widget.NaturalScale.scale({ymin : 255, ymax : 768});
        expect(result).toEqual({ytop : 1000, ydown : 250, ranges : 3});
    });

    it("scale test5", function () {
        var result = Istac.widget.NaturalScale.scale({ymin : 0, ymax : 1200});
        expect(result).toEqual({ytop : 1500, ydown : 0, ranges : 3});
    });

});
