describe("Color Generator", function () {

    it("", function () {
        var colors = Istac.widget.helper.colorPaletteGenerator(50);
        console.log(colors);


        _.each(colors, function (color, i) {
            $("body").append('<div class="block" style="width: 30px; height: 30px; position:absolute; background-color: '+ color + '; top:' + (30 * i) + 'px"></div>');
        });

        //expect(colorArrayGenerator(22)).toEqual([0, 120, 240, 60, 180, 300, 30, 90, 150, 210, 270, 330, 15, 45, 75, 105, 135, 165, 195, 225, 255, 285]);
    });


});
