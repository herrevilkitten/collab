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
                messages = {
                    createAddShape: function(shape) {
                        return angular.extend({}, BASE_MESSAGE, {
                            type: MESSAGE_PACKAGE + '.AddShapeMessage',
                            boardId: $routeParams.boardId,
                            shape: shape
                        });
                    },

                    createRemoveShape: function(shapeId) {
                        return angular.extend({}, BASE_MESSAGE, {
                            type: MESSAGE_PACKAGE + '.RemoveShapeMessage',
                            boardId: $routeParams.boardId,
                            shapeId: shapeId
                        });
                    },

                    createHeartbeat: function() {
                        return angular.extend({}, BASE_MESSAGE, {
                            type: MESSAGE_PACKAGE + '.HeartbeatMessage',
                            boardId: $routeParams.boardId
                        });
                    },

                    createQuery: function() {
                        return angular.extend({}, BASE_MESSAGE, {
                            type: MESSAGE_PACKAGE + '.QueryMessage',
                            boardId: $routeParams.boardId
                        });
                    },

                    createWelcome: function() {
                        return angular.extend({}, BASE_MESSAGE, {
                            type: MESSAGE_PACKAGE + '.WelcomeMessage',
                            boardId: $routeParams.boardId
                        });
                    },

                    createChatMessage: function (text) {
                        return angular.extend({}, BASE_MESSAGE, {
                            type: MESSAGE_PACKAGE + ".ChatMessage",
                            boardId: $routeParams.boardId,
                            chat: text
                        });
                    }

                };

            return messages;
        });
}(window.angular));
