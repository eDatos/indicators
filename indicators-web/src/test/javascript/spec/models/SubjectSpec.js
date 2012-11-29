describe("Subject", function () {

    it("should fetch and return the instances collection", function () {
        var subject = new App.models.Subject({id : 'EDUCACION'});
        var req = subject.fetch();

        waitsFor(function () {
            return req.isResolved();
        }, "Subject request", 1000);

        runs(function () {
            expect(subject.get('title')).toEqual('Educación');
        });

    });

});