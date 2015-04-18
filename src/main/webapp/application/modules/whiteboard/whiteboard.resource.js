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
                listShared: function() {
                    return $http.get(baseUrl + '/byAccess');
                },
                create: function(name) {
                    return $http.post(baseUrl + '/create', {name: name});
                }
            };
        })
        .factory('CurrentBoard', function($routeParams) {
            return $routeParams.boardId;
        });

}(window.angular));