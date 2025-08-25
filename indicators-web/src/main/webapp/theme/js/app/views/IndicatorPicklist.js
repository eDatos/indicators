(function (_) {
    "use strict";

    App.views.IndicatorPicklist = Backbone.View.extend({

        template: App.loadTemplate('indicator-picklist'),

        events: {
            "click .selected-to-the-first-list-button": "_moveSelectedToTheFirstList",
            "click .all-to-the-first-list-button": "_moveAllToTheFirstList",
            "click .selected-to-the-second-list-button": "_moveSelectedToTheSecondList",
            "click .all-to-the-second-list-button": "_moveAllToTheSecondList",
            'click .selectable-picklist-item:not(.disabled)': "_toggleSelectedItem",
            'click .indicator-picklist-subject-header > span': "_toggleSubject",
            "keyup input.indicator-picklist-filter": "_filterTree"
        },

        initialize: function (options) {
            this.subjects = options.subjects;
            this.subjects.on('reset', this.render, this);
            this.collection.on('reset', this.render, this);

            this._filterTree = _.debounce(this._filterTree, 200);

            this.render();
        },

        render: function () {
            var self = this;

            var context = {
                subjectsAndIndicatorsTree: this._getSubjectsAndIndicatorsTree()
            };
            this.$el.html(this.template(context));

            this.$(".indicator-picklist-second-list" ).sortable({
                update: function () {
                    self.trigger("change", self._getSecondListIndicatorsFromDom());
                }
            });
            return this;
        },

        _getSubjectsAndIndicatorsTree: function () {
            if (!this.collection.length || !this.subjects.length) {
                return [];
            }

            var tree = [];
            var indicatorsGroupedBySubjectCode = _.groupBy(this.collection.toJSON(), "subjectCode");
            var subjects = this._getAllSubjects();

            var self = this;
            _.each(indicatorsGroupedBySubjectCode, function (indicators, subjectCode) {
                var currentTreeBranch = tree;
                var subjectCodeParts = subjectCode.split(".");
                subjectCodeParts.reduce(function (parentCode, currentCode) {
                    var nestedCode = parentCode ? parentCode + "." + currentCode : currentCode;
                    var existingSubjectNode = currentTreeBranch.find(function(treeNode) {
                        return treeNode.subjectCode === nestedCode;
                    });
                    if (!existingSubjectNode) {
                        currentTreeBranch.push(self._getSubjectTreeNode(nestedCode, subjects, indicatorsGroupedBySubjectCode));
                        existingSubjectNode = currentTreeBranch[currentTreeBranch.length - 1];
                    }
                    currentTreeBranch = existingSubjectNode.children;
                    return nestedCode;
                }, null);
            });
            return tree;
        },

        _getAllSubjects: function () {
            return this.subjects.toJSON().reduce((subjects, subject) => {
                subjects[subject.nestedId] = subject;
                return subjects;
            }, {})
        },

        _getSubjectTreeNode: function (nestedCode, subjects, indicatorsByCode) {
            return {
                subjectCode: nestedCode,
                subjectTitle: App.utils.InternationalizationUtils.localizeLabel(subjects[nestedCode].name),
                children: [],
                indicators: indicatorsByCode[nestedCode] || [],
                isSubject: true
            };
        },

        _toggleSubject: function (event) {
            event.stopPropagation();
            this.$(event.currentTarget).parent().toggleClass("collapsed");
        },

        _toggleSelectedItem: function (event) {
            event.stopPropagation();
            this.$(event.currentTarget).toggleClass("selected-picklist-item");
        },

        _getSecondListIndicatorsFromDom: function () {
            var self = this;
            return this.$("ul.indicator-picklist-second-list li").map(function (i, secondListItemEl) {
                return self.collection.toJSON().find(function (indicator) {
                    return indicator.id === secondListItemEl.attributes["indicatorid"].value;
                });
            }).get();
        },

        _moveSelectedToTheSecondList: function () {
            this.$("ul.indicator-picklist-first-list li.selected-picklist-item")
                .removeClass("selected-picklist-item")
                .addClass("disabled")
                .clone()
                .prependTo("ul.indicator-picklist-second-list")
                .removeClass("disabled");
            this.trigger("change", this._getSecondListIndicatorsFromDom());
        },

        _moveAllToTheSecondList: function () {
            this.$("ul.indicator-picklist-first-list li.selectable-picklist-item")
                .removeClass("selected-picklist-item")
                .addClass("disabled")
                .clone()
                .prependTo("ul.indicator-picklist-second-list")
                .removeClass("disabled");
            this.trigger("change", this.collection.toJSON());
        },

        _moveSelectedToTheFirstList: function () {
            this.$("ul.indicator-picklist-second-list li.selected-picklist-item").each(function () {
                $("ul.indicator-picklist-first-list li.selectable-picklist-item[indicatorId=" + $(this).attr("indicatorId") + "]").removeClass("disabled");
                $(this).remove();
            });
            this.trigger("change", this._getSecondListIndicatorsFromDom());
        },

        _moveAllToTheFirstList: function () {
            this.$("ul.indicator-picklist-second-list li").remove();
            this.$("ul.indicator-picklist-first-list li.selectable-picklist-item.disabled").removeClass("disabled")
            this.trigger("change", []);
        },

        _filterTree: function () {
            var filter = this.$(".indicator-picklist-filter")[0] ? this.$(".indicator-picklist-filter")[0].value : "";
            this.$("ul.indicator-picklist-first-list li").addClass("hidden");
            this.$("ul.indicator-picklist-first-list li > span").each(function () {
                if ($(this).text().toLowerCase().includes(filter.toLowerCase())) {
                    $(this).parents("ul.indicator-picklist-first-list li").removeClass("hidden");
                    $(this).parent().find("li").removeClass("hidden");
                }
            });
        }
    });
}(window._));
