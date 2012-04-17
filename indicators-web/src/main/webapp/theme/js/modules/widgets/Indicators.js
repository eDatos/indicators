/**
 *
 */

//TODO change url
var IndicatorsCollection = PaginatedCollection.extend({
    url : 'http://localhost:8080/indicators-web/api/systems'
});

var IndicatorsView = PaginatedView.extend({
    contentItemTemplate : _.template($('#indicatorsItem').html())
});