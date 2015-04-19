(function(angular, SVG, gapi, $) {
    'use strict';

    angular
        .module('gitboard.frontpage', [])
        .controller('FrontPageController', function($scope, $log, $location, Whiteboard) {
            $scope.whiteboards = [];
            $scope.sharedWhiteboards = [];
            $scope.isDialogOpen = false;
            $scope.newBoardName = '';

            function createWhiteboard(name) {
                Whiteboard
                    .create(name)
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

                // Get the shared whiteboards
                Whiteboard
                    .listShared()
                    .success(function(data) {
                        $log.info('Retrieved boards:', data);
                        $scope.sharedWhiteboards.length = 0;
                        angular.forEach(data, function(item) {
                            $scope.sharedWhiteboards.push(item);
                        });
                    })
                    .error(function(data) {
                        $log.error('Unable to retrieve boards for user:', data);
                    });
            }

            function openBoardDialog() {
                $scope.isDialogOpen = true;
            }

            function cancelNewBoard() {
                $scope.newBoardName = '';
                $scope.isDialogOpen = false;
            }

            function createNewBoard() {
                createWhiteboard($scope.newBoardName);
                $scope.newBoardName = '';
                $scope.isDialogOpen = false;
            }

            $scope.createWhiteboard = createWhiteboard;
            $scope.openBoardDialog = openBoardDialog;
            $scope.cancelNewBoard = cancelNewBoard;
            $scope.createNewBoard = createNewBoard;

            retrieveWhiteboards();
        }
    );
}(window.angular, window.SVG, window.gapi, window.jQuery));
