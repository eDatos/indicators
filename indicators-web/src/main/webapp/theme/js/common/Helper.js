(function () {

    EDatos.common.helper = {};

    EDatos.common.helper.get = function (obj, stringPath) {
        var path = stringPath.split('.');
        var length = path.length;
        for (var i = 0; i < length; i++) {
            if (obj == null) return void 0;
            obj = obj[path[i]];
        }
        return length ? obj : void 0;
    }

}());