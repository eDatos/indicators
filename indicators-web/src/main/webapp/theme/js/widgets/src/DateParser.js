(function (moment) {
    "use strict";

    Istac.widget.DateParser = {

        REGEXPS: {
            BIYEARLY: /^\d{4}-S(\d{1,2})$/,
            FOUR_MONTHLY: /^\d{4}-T(\d{1,2})$/,
            QUARTERLY: /^\d{4}-Q(\d{1,2})$/,
            MONTHLY: /^\d{4}-\d{2}$/,
            DAILY: /^\d{4}-D(\d{1,3})$/
        },
        MONTHS: {
            BIYEARLY: 6,
            FOUR_MONTHLY: 4,
            QUARTERLY: 3
        },

        dateParsers: {
            YEARLY: function (stringDate) {
                return moment(stringDate, "YYYY").endOf('year'); // parse format YYYY-A1 too
            },
            BIYEARLY: function (stringDate) {
                var matchs = stringDate.match(Istac.widget.DateParser.REGEXPS.BIYEARLY);
                if (matchs && matchs[1]) {
                    var monthBeginNumber = (matchs[1] - 1) * Istac.widget.DateParser.MONTHS.BIYEARLY;

                    var momentDate = moment(stringDate, 'YYYY');
                    momentDate.month(monthBeginNumber + Istac.widget.DateParser.MONTHS.BIYEARLY - 1);

                    return momentDate.endOf('month');
                }
            },
            FOUR_MONTHLY: function (stringDate) {
                var matchs = stringDate.match(Istac.widget.DateParser.REGEXPS.FOUR_MONTHLY);
                if (matchs && matchs[1]) {
                    var monthBeginNumber = (matchs[1] - 1) * Istac.widget.DateParser.MONTHS.FOUR_MONTHLY;

                    var momentDate = moment(stringDate, 'YYYY');
                    momentDate.month(monthBeginNumber + Istac.widget.DateParser.MONTHS.FOUR_MONTHLY - 1);

                    return momentDate.endOf('month');
                }
            },
            QUARTERLY: function (stringDate) {
                var matchs = stringDate.match(Istac.widget.DateParser.REGEXPS.QUARTERLY);
                if (matchs && matchs[1]) {
                    var monthBeginNumber = (matchs[1] - 1) * Istac.widget.DateParser.MONTHS.QUARTERLY;

                    var momentDate = moment(stringDate, 'YYYY');
                    momentDate.month(monthBeginNumber + Istac.widget.DateParser.MONTHS.QUARTERLY - 1);

                    return momentDate.endOf('month');
                }
            },
            MONTHLY: function (stringDate) { // 2018-M10, 2018-10
                return moment(stringDate, (Istac.widget.DateParser.REGEXPS.MONTHLY).test(stringDate) ? 'YYYY-MM' : "YYYY-'M'MM").endOf('month');
            },
            WEEKLY: function (stringDate) { // 2018-W10
                return moment(stringDate, "YYYY-'W'WW").endOf('isoWeek');
            },
            DAILY: function (stringDate) {
                var matchs = stringDate.match(Istac.widget.DateParser.REGEXPS.DAILY)
                var momentDate;
                if (matchs) {
                    momentDate = moment(stringDate, "YYYY");
                    momentDate.dayOfYear(matchs[1]);
                } else {
                    momentDate = moment(stringDate, "YYYY-MM-DD");
                }
                return momentDate.endOf('day');
            },
            HOURLY: function (stringDate) {
                return moment(stringDate).endOf('hour');
            }
        },

        parse: function (stringDate, granularity) {
            var date = this.dateParsers[granularity](stringDate);

            if (date) {
                return date.utc().valueOf();
            }
        }
    };


}(window.moment));
