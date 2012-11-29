describe("Subjects", function () {

    it("fetch", function () {
        var subjects = new App.collections.Subjects();
        var req = subjects.fetch();

        waitsFor(function () {
            return req.isResolved();
        }, "Request not completed", 1000);

        runs(function () {
            expect(subjects.size()).toEqual(4);
        });
    });

});