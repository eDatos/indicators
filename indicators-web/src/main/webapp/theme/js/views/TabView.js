var TabView = Backbone.View.extend({

    initialize : function(options){
        if(options){
            this.views = options.views || {};
        }
    },

    events : {
        'click .nav-tabs li a' : 'changeTab'
    },

    render : function(){
        var $content = $('.tab-content', this.$el);

        var activeTab = this.activeTab();
        var view = this.viewFromTab(activeTab);
        if (view) {
            view.render();
            $content.html(view.el);
        } else {
            //tab not found, show error message?
            $content.empty();
        }
    },

    viewFromTab : function(tab){
        return this.views[tab];
    },

    activeTab : function(){
        if (this._activeTab) {
            return this._activeTab;
        } else {
            var $firstTab = $('.nav-tabs li a:first', this.$el);
            return this._extractTabFromA($firstTab);
        }
    },

    _extractTabFromA : function($a){
        var href = $a.attr('href');
        if (href && href.length > 0 && href[0] === '#') {
             return href.substring(1, href.length);
        }
    },

    changeTab : function(e){
        var $target = $(e.target);

        // Change tab active style
        $('.nav-tabs .active').removeClass('active');
        $target.parent('li').addClass('active');


        //Change the content View
        var tab = this._extractTabFromA($target);
        if (tab) {
            this._activeTab = tab;
            e.preventDefault();
            this.render();
        }
    }
});
