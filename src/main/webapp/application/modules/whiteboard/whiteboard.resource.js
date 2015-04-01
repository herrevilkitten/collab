(function(angular) {
    angular
        .module('gitboard.whiteboard')
        .service('Whiteboard', function($http) {
            var baseUrl = 'api/board';

            return {
                get: function(id) {
                    return $http.get(baseUrl + '/' + id);
                },
                list: function() {
                    return $http.get(baseUrl + '/byUser');
                },
                create: function() {
                    return $http.post(baseUrl + '/create');
                }
            };
        });

}(window.angular));