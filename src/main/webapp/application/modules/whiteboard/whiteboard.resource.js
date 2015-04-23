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
                },
                copy: function(id) {
                    return $http.post(baseUrl + '/copy', {id: id});
                },
                fork: function(id) {
                    return $http.post(baseUrl + '/branch', {id: id});
                },
                merge: function(id) {
                    return $http.post(baseUrl + '/merge', {id: id});
                },
                share: function(email) {

                },
                differences: function(source, destination) {
                    return $http.post(baseUrl + '/diff', {source: source, destination: destination});
                }
            };
        })
        .factory('CurrentBoard', function($routeParams) {
            console.log('$routeParams', $routeParams);
            return $routeParams.boardId;
        });

}(window.angular));