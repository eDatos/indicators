(function () {
    "use strict";

    var YEARLY_PATTERN = /^\d{4}$/;
    var BIYEARLY_PATTERN = /^\d{4}H\d$/;
    var QUARTERLY_PATTERN = /^\d{4}Q\d$/;
    var FOUR_MONTHLY_PATTERN = /^\d{4}T\d$/;
    var MONTHLY_PATTERN = /^\d{4}M\d\d$/;
    var WEEKLY_PATTERN = /^\d{4}W\d\d$/;
    var DAILY_PATTERN = /^\d{8}$/;
    var HOURLY_PATTERN = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}$/;

    Istac.widget.DateParser = {

        /**
         * @param {String} stringDate format:
         *
         *      - Yearly: yyyy (Example: 1999)
         *      - Biyearly: yyyyHs (Example: 1999H1)
         *      - Quarterly: yyyyQt (Example: 1999Q1)
         *      - Four monthly: yyyyTt (Example: 1999T1)
         *      - Monthly: yyyyMmm (Example: 1999M01)
         *      - Weekly: yyyyWss (Example: 1999W51).
         *      - Daily: yyyymmdd (Example: 19990101)
         *      - Hourly: yyyymmddThh:mm:ss (Example: 1999-01-01T01:01:01)
         *
         * @return {Number} milliseconds
         */
        parse : function (stringDate) {
            var date;

            if (stringDate.match(YEARLY_PATTERN)) {
                date = moment(stringDate, "YYYY");
            } else if (stringDate.match(BIYEARLY_PATTERN)) {
                date = moment(stringDate, "YYYY?M");
                date.month(((date.month() + 1) * 6) - 1);
            } else if (stringDate.match(QUARTERLY_PATTERN)) {
                date = moment(stringDate, "YYYY?M");
                date.month(((date.month() + 1) * 3) - 1);
            } else if (stringDate.match(FOUR_MONTHLY_PATTERN)) {
            	date = moment(stringDate, "YYYY?M");
                date.month(((date.month() + 1) * 4) - 1);
            } else if (stringDate.match(MONTHLY_PATTERN)) {
                date = moment(stringDate, "YYYY'M'MM");
            } else if (stringDate.match(WEEKLY_PATTERN)) {
                date = moment(stringDate, "YYYY'W'WW").weekday(0);
            } else if (stringDate.match(DAILY_PATTERN)) {
                date = moment(stringDate, "YYYYMMDD");
            } else if (stringDate.match(HOURLY_PATTERN)) {
            	date = moment(stringDate, "YYYY-MM-DD'T'HH:mm:ss");
            }

            if (date) {
                return date.utc().valueOf();
            }
        }
    };


}());