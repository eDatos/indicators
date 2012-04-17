/**
 *
 */
var PaginatedCollection = Backbone.Collection.extend({
    initialize : function(models, options){
        options = options || {};

        this.firstPage = 1;
        this.perPage  = 10;

        this.page = options.page || this.firstPage;
    },

    fetchPage : function(page){
        //TODO pass the options to fetch
        var self = this;
        this.page = page;
        var fetchData =  {
            start : (page - self.firstPage) * self.perPage,
            size  : self.perPage
        };
        this.fetch({data : fetchData});
    },

    fetchCurrentPage : function(){
        this.fetchPage(this.page);
    },

    fetchNextPage : function(options){
        if(this.page !== undefined){
            this.page = ++this.page;
            this.fetchCurrentPage();
        }
    },

    fetchPreviousPage : function(options){
        console.log('fetch');
        if(this.page !== undefined && this.totalPages !== undefined){
            console.log('fetch!');
            this.page = --this.page;
            this.fetchCurrentPage();
        }
    },

    hasNextPage : function(){
        if(this.page !== undefined && this.totalPages !== undefined){
            return this.page < this.totalPages;
        }
        return false;
    },

    hasPreviousPage : function(){
        if(this.page !== undefined && this.totalPages !== undefined){
            return this.page > this.firstPage;
        }
    },

    parse : function(response){
        this.totalPages = Math.floor(response.total / this.perPage);
        return response.results;
    },

    getPaginationInfo : function(){
        var self = this;
        var info = {
            page: self.page,
            firstPage: self.firstPage,
            totalPages: self.totalPages,
            lastPage: self.totalPages,
            perPage: self.perPage
        };
        return info;
    }

});