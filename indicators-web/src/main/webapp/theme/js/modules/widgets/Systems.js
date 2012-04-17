/**
 * Collection and view paginated
 */
var SystemsCollection = PaginatedCollection.extend({
    url : 'http://localhost:8080/indicators-web/api/systems'
});

var SystemsView = PaginatedView.extend({
    contentItemTemplate : _.template($('#systemsItem').html())
});