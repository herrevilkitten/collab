(function(angular) {
    'use strict';

    angular
        .module('gitboard.whiteboard')
        .factory('MessageFactory', function($routeParams) {
            var MESSAGE_PACKAGE = 'org.evilkitten.gitboard.application.services.atmosphere.message',
                BASE_MESSAGE = {
                    toJson: function() {
                        return angular.toJson(this);
                    }
                },
                boardId = $routeParams.boardId,
                messages = {
                    createAddShape: function(shape) {
                        return angular.extend({}, BASE_MESSAGE, {
                            type: MESSAGE_PACKAGE + '.AddShapeMessage',
                            boardId: boardId,
                            shape: shape
                        });
                    },

                    createRemoveShape: function(shapeId) {
                        return angular.extend({}, BASE_MESSAGE, {
                            type: MESSAGE_PACKAGE + '.RemoveShapeMessage',
                            boardId: boardId,
                            shapeId: shapeId
                        });
                    },

                    createHeartbeat: function() {
                        return angular.extend({}, BASE_MESSAGE, {
                            type: MESSAGE_PACKAGE + '.HeartbeatMessage',
                            boardId: boardId
                        });
                    },

                    createQuery: function() {
                        return angular.extend({}, BASE_MESSAGE, {
                            type: MESSAGE_PACKAGE + '.QueryMessage',
                            boardId: boardId
                        });
                    },

                    createWelcome: function() {
                        return angular.extend({}, BASE_MESSAGE, {
                            type: MESSAGE_PACKAGE + '.WelcomeMessage',
                            boardId: boardId
                        });
                    }
                };

            return messages;
        });
}(window.angular));
