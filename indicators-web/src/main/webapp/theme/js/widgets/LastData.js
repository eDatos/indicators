/**
 *
 */
Istac.Widgets.LastData = Istac.Widgets.Base.extend({
    containerClass : "istac-widget-lastData",

    init : function(options){
        this._super(options);
    },

    _renderTableColumn : function (content) {
        return '<td>' + content + '</td>'
    },

    render : function () {
        if (this.indicators) {
            var head = '<thead>';
            head += '<tr><th></th>';

            if (this.visibleData.absolute) {
                head += '<th>absoluto</th>';
            }

            if (this.visibleData.interanual) {
                head += '<th>variación interanual</th>';
            }

            if (this.visibleData.interperiodic) {
                head += '<th>variación interperiódica</th>';
            }

            head += '</thead>';

            var rows = '';
            for (var i = 0; i < this.indicators.length; i++) {
                var indicator = this.indicators[i];
                var row = '<tr><th>' + indicator.name + '</th>';

                if (this.visibleData.absolute) {
                    row += this._renderTableColumn(indicator.data.absolute);
                }
                if (this.visibleData.interanual) {
                    row += this._renderTableColumn(indicator.data.interanual);
                }
                if (this.visibleData.interperiodic) {
                    row += this._renderTableColumn(indicator.data.interperiodic);
                }

                row += '</tr>';
                rows += (row);
            }
            this.contentContainer.html('<table>' + head + '<tbody>' + rows + '</tbody></table>');
        }
    }
});


