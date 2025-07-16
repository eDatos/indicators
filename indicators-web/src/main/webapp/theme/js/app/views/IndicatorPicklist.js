(function (_) {
    "use strict";

    App.views.IndicatorPicklist = Backbone.View.extend({

        template: App.loadTemplate('indicator-picklist'),

        events: {
            "click .selected-to-the-left-button": "_moveSelectedToTheLeft",
            "click .all-to-the-left-button": "_moveAllToTheLeft",
            "click .selected-to-the-right-button": "_moveSelectedToTheRight",
            "click .all-to-the-right-button": "__moveAllToTheRight",
            'click .selectable-picklist-item': "_toggleSelectedItem"
        },

        initialize: function () {
            this.collection.on('reset', this.render, this);
            this.render();
        },

        render: function () {
            var self = this;
            var context = {
                indicatorsBySubject: _.map(_.groupBy(self.collection.toJSON(), "subjectCode"), function (value, key) {
                    return {
                        subjectCode: key,
                        subjectTitle: self._getLabel(value[0].subjectTitle),
                        indicators: value
                    };
                })
            };
            this.$el.html(this.template(context));

            this.$(".indicator-picklist-right-list" ).sortable({
                update: function () {
                    self.trigger("change", self._getRightListIndicatorsFromDom());
                }
            });
            return this;
        },

        _getLabel: function (internationalString) {
            if (internationalString) {
                return internationalString[currentLocale] || internationalString["__default__"];
            }
        },

        _toggleSelectedItem: function (event) {
            this.$(event.currentTarget).toggleClass("selected-picklist-item");
        },

        _getRightListIndicatorsFromDom: function () {
            var self = this;
            return this.$("ul.indicator-picklist-right-list li").map(function (i, rightListItemEl) {
                return self.collection.toJSON().find(function (indicator) {
                    return indicator.id === rightListItemEl.id;
                });
            }).get();
        },

        _moveSelectedToTheRight: function () {
            this.$("ul.indicator-picklist-left-list li.selected-picklist-item").removeClass("selected-picklist-item").prependTo("ul.indicator-picklist-right-list");
            this.trigger("change", this._getRightListIndicatorsFromDom());
        },

        __moveAllToTheRight: function () {
            this.$("ul.indicator-picklist-left-list li.selectable-picklist-item").removeClass("selected-picklist-item").prependTo("ul.indicator-picklist-right-list");
            this.trigger("change", this.collection.toJSON());
        },

        _moveSelectedToTheLeft: function () {
            this.$("ul.indicator-picklist-right-list li.selected-picklist-item").removeClass("selected-picklist-item").each(function (_, rightListItemEl) {
                $(rightListItemEl).prependTo("ul.indicator-picklist-left-list ul#" + $(rightListItemEl).attr("subjectId").replaceAll(".", "\\."));
            });
            this.trigger("change", this._getRightListIndicatorsFromDom());
        },

        _moveAllToTheLeft: function () {
            this.$("ul.indicator-picklist-right-list li").removeClass("selected-picklist-item").each(function (_, rightListItemEl) {
                $(rightListItemEl).prependTo("ul.indicator-picklist-left-list ul#" + $(rightListItemEl).attr("subjectId").replaceAll(".", "\\."));
            });
            this.trigger("change", []);
        }
    });
}(window._));
