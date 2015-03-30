(function(angular, SVG, gapi, $) {
    'use strict';

    angular
        .module('gitboard.frontpage', [])
        .controller('FrontPageController', function($scope, $log, $location, Whiteboard) {
            $scope.whiteboards = [];

            function createWhiteboard() {
                Whiteboard
                    .create()
                    .success(function(data) {
                        $log.info('Created new whiteboard #' + data.id);
                        $location.path('/board/' + data.id);
                    })
                    .error(function(data) {
                        $log.error('Unable to create new board:', data);
                    });
            }

            function retrieveWhiteboards() {
                Whiteboard
                    .list()
                    .success(function(data) {
                        $log.info('Retrieved boards:', data);
                        $scope.whiteboards.length = 0;
                        angular.forEach(data, function(item) {
                            $scope.whiteboards.push(item);
                        });
                    })
                    .error(function(data) {
                       $log.error('Unable to retrieve boards for user:', data);
                    });
            }

            $scope.createWhiteboard = createWhiteboard;

            retrieveWhiteboards();
        }
    );
}(window.angular, window.SVG, window.gapi, window.jQuery));
