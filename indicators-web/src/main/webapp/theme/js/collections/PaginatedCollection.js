/**
 * PaginatedCollection
 */

var PaginatedCollection = Backbone.Collection.extend({

    initialize : function(models, options){
        options = options || {};

        this.firstPage = 1;
        this.perPage = 1;

        this.page = options.page || this.firstPage;

        this.fetchParamsNames = {
            offset : 'offset',
            limit : 'limit',
            total : 'total',
            items : 'items'
        };
    },

    _getValue : function(object, prop){
        if (!(object && object[prop])) return null;
        return _.isFunction(object[prop]) ? object[prop]() : object[prop];
    },

    fetchPage : function(page){
        if (this._getValue(this, 'url') != null) {
            var self = this;
            this.page = page;

            var fetchData = {};
            fetchData[this.fetchParamsNames.offset] = (page - self.firstPage) * self.perPage;
            fetchData[this.fetchParamsNames.limit] = self.perPage;

            return this.fetch({data : fetchData});
        } else {
            //No url? empty the collection
            this.reset([]);
        }
    },

    fetchCurrentPage : function(){
        return this.fetchPage(this.page);
    },

    fetchNextPage : function(options){
        if (this.page !== undefined) {
            this.page = ++this.page;
            return this.fetchCurrentPage();
        }
    },

    fetchPreviousPage : function(options){
        if (this.page !== undefined && this.totalPages !== undefined) {
            this.page = --this.page;
            return this.fetchCurrentPage();
        }
    },

    hasNextPage : function(){
        if (this.page !== undefined && this.totalPages !== undefined) {
            return this.page < this.totalPages;
        }
        return false;
    },

    hasPreviousPage : function(){
        if (this.page !== undefined && this.totalPages !== undefined) {
            return this.page > this.firstPage;
        }
    },

    parse : function(response){
        var total = response[this.fetchParamsNames.total];
        this.totalPages = Math.ceil(total / this.perPage);

        return response[this.fetchParamsNames.items];
    },

    getPaginationInfo : function(){
        var self = this;
        var info = {
            page : self.page,
            firstPage : self.firstPage,
            totalPages : self.totalPages,
            lastPage : self.totalPages,
            perPage : self.perPage
        };
        return info;
    }

});
