(function () {
    "use strict";

    App.views.TabView = Backbone.View.extend({

        initialize : function (options) {
            if (options) {
                this.views = options.views || {};
                this.initializeModel(options);
                this.initializeView();
                this.model.on('change:' + this.modelProperty, this.render, this);
            }
        },

        initializeModel : function (options) {
            if (options.model) {
                this.model = options.model;
                this.modelProperty = options.modelProperty || 'tab';
            } else {
                this.model = new Backbone.Model({tab : this.firstTab()});
                this.modelProperty = 'tab';
            }
        },

        initializeView : function () {
            var $contentContainer = this.$('.tab-content');
            _.each(this.views, function (view) {
                view.render();
                view.$el.hide();
                $contentContainer.append(view.$el);
            }, this);
        },

        events : {
            'click [data-tab]' : 'clickTab'
        },

        render : function () {
            var activeTab = this.model.get(this.modelProperty);
            this.changeTab(activeTab);
        },

        viewFromTab : function (tab) {
            return this.views[tab];
        },

        firstTab : function () {
            var $firstTab = this.$el.find('[data-tab]:first');
            return $firstTab.data('tab');
        },

        _removeActiveClass : function () {
            this.$el.find('>.tab-navigation .active').removeClass('active');
        },

        _addActiveClass : function ($el) {
            $el.parent().addClass('active');
        },

        changeTab : function (newTab) {
            this._removeActiveClass();
            this._addActiveClass(this.$('[data-tab=' + newTab + ']'));

            this.newView = this.viewFromTab(newTab);
            _.each(this.views, function (view) {
                if (view === this.newView) {
                    view.$el.show();
                } else {
                    view.$el.hide();
                }
            }, this);
        },

        clickTab : function (e) {
            var $target = $(e.target);
            var newTab = $target.data('tab');
            this.model.set(this.modelProperty, newTab);
            return false;
        }

    });

}());