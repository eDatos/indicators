/**
 *
 */


var SystemsCollection = PaginatedCollection.extend({
    //url : 'http://jrodped.arte:8080/indicators-web/api/indicators/v1.0/indicatorsSystems/'
    url : function(){
        return apiContext + '/indicatorsSystems/'
    }
});


