/**
 *
 */

var PaginatedView = Backbone.View.extend({
    events : {
        'click a.pagination-next' : 'nextResultPage',
        'click a.pagination-previous' : 'previousResultPage',
        'click a.pagination-page' : 'preventDefault'
    },

    paginationTemplate : templateManager.get("pagination"),

    initialize : function(){
        this.$elPagination = $('<div></div>');
        this.$elContent = $('<div></div>');

        this.$el.append(this.$elContent);
        this.$el.append(this.$elPagination);

        this.collection.on('reset', this.render, this);
        this.collection.on('change', this.render, this);

        this.collection.fetchCurrentPage();
    },

    render : function(){
        this.renderContent();
        this.renderPagination();
    },

    renderPagination : function(){
        var info = this.collection.getPaginationInfo();
        this.$elPagination.html(this.paginationTemplate(info));
    },

    renderContent : function(){
        var self = this;
        this.$elContent.empty();
        this.collection.each(function(item){
            self.$elContent.append(self.renderContentItem(item));
        });
    },

    renderContentItem : function(item){
        if (this.contentItemTemplate === undefined) {
            console.error("Content item template not defined");
            return;
        }

        return this.contentItemTemplate(item.toJSON());
    },

    _pageIsActive : function(page){
        return !$(page).parent('li').hasClass('disabled');
    },

    nextResultPage : function(e){
        e.preventDefault();
        if (this._pageIsActive(e.target)) {
            this.collection.fetchNextPage();
        }
    },

    previousResultPage : function(e){
        e.preventDefault();
        if (this._pageIsActive(e.target)) {
            this.collection.fetchPreviousPage();
        }
    },

    gotoPage : function(e){
        e.preventDefault();
        if (this._pageIsActive(e.target)) {
            var page = $(e.target).text();
            this.collection.fetchPage(page);
        }
    },

    preventDefault : function(e){
        e.preventDefault();
    }

});
