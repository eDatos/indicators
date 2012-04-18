/**
 *
 */
var PaginatedView = Backbone.View.extend({

    events: {
        'click a.servernext': 'nextResultPage',
        'click a.serverprevious': 'previousResultPage',
        'click a.serverlast': 'gotoLast',
        'click a.page': 'gotoPage',
        'click a.serverfirst': 'gotoFirst'
    },

    paginationTemplate : _.template($('#paginationTmpl').html()),

    initialize: function () {
        this.$elPagination = $('<div></div>');
        this.$elContent = $('<div></div>');

        this.$el.append(this.$elContent);
        this.$el.append(this.$elPagination);

        this.collection.on('reset', this.render, this);
        this.collection.on('change', this.render, this);

        this.collection.fetchCurrentPage();
    },

    render: function () {
        this.renderContent();
        this.renderPagination();
    },

    renderPagination : function(){
        this.$elPagination.html(this.paginationTemplate(this.collection.getPaginationInfo()));
    },

    renderContent : function(){
        var self = this;
        this.$elContent.empty();
        this.collection.each(function(item){
            self.$elContent.append(self.renderContentItem(item));
        });
    },

    renderContentItem : function(item){
        if(this.contentItemTemplate === undefined){
            console.error("Content item template not defined");
            return;
        };

        return this.contentItemTemplate(item.toJSON());
    },

    nextResultPage: function (e) {
        e.preventDefault();
        this.collection.fetchNextPage();
    },

    previousResultPage: function (e) {
        e.preventDefault();
        this.collection.fetchPreviousPage();
    },

    gotoFirst: function (e) {
        e.preventDefault();
        this.collection.fetchPage(this.collection.getPaginationInfo().firstPage);
    },

    gotoLast: function (e) {
        e.preventDefault();
        this.collection.fetchPage(this.collection.getPaginationInfo().lastPage);
    },

    gotoPage: function (e) {
        e.preventDefault();
        var page = $(e.target).text();
        this.collection.fetchPage(page);
    }

});