/**
 * Collection and view paginated
 */
var SystemsCollection = PaginatedCollection.extend({
    url : 'http://localhost:8080/indicators-web/api/systems'
});

var SystemsView = PaginatedViewWithSelection.extend({
    initialize : function(){
        console.log('waat');
    },


    templateContentItem : _.template($('#systemsItem').html())
});