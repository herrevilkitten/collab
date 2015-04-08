(function(angular, SVG, gapi, $) {
    'use strict';

    angular.module('gitboard.user', [])
        .factory('UserService', ['$log', '$resource',
            function($log, $resource) {
                $log.debug('Creating UserService');
                return $resource(

                );
            }])
        .controller('UserController', ['$scope', '$log', '$http', '$location',
            function($scope, $log, $http, $location) {
                $scope.signOut = function() {
                    $log.info('Signing out');
                    $http.get('api/user/signout').then(function(r) {
                        window.console.log('r', r);
                        window.location = 'signin';
//                        $location.path('/signin');
//                        gapi.auth.signOut();
                    });
                };
            }]);
}(window.angular, window.SVG, window.gapi, window.jQuery));
