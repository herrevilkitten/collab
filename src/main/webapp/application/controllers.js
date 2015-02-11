(function(angular, SVG, gapi, $) {
    'use strict';

    var MODE_DRAWING = 'd',
        MODE_COMMAND = 'c',
        MODE_SELECT = 's',
        MODE_SELECT_LASSO = 0,
        MODE_SELECT_RECT = 1,

        MODE_DRAWING_PENCIL = 'p',
        MODE_DRAWING_LINE = 'l',
        MODE_DRAWING_RECT = 'r',
        MODE_DRAWING_OVAL = 'e',

        KEY_SHIFT = 16,
        KEY_CONTROL = 17;

    angular.module('svb.controllers', [ 'ui.bootstrap', 'colorpicker.module' ])
        .controller('FrontPageController', ['$scope', '$log', function($scope, $log) {
        }])
        .controller('WhiteboardController', [ '$scope', '$log', function($scope, $log) {
            try {
                if (gapi.auth) {
                    $log.info('auth.token', gapi.auth.getToken());
                }

                $scope.color = {
                    foreground: '#000000',
                    fill: '#ffffff',
                    complementary: function(color) {
                        return new SVG.Color(color).complementary();
                    }
                };
                $scope.mode = MODE_DRAWING;
                $scope.drawingMode = MODE_DRAWING_PENCIL;
                $scope.strokes = [];
                $scope.canvas = SVG('canvas').size(1920, 1080);
                $scope.toolboxSide = 'left';
                $scope.command = {
                    clear: function() {
                        if (window.confirm('Clear the drawing?')) {
                            $scope.canvas.clear();
                        }
                        $scope.mode = MODE_DRAWING;
                    },
                    switchSides: function() {
                        $scope.toolboxSide = ($scope.toolboxSide === 'left') ? 'right' : 'left';
                    }

                };

                var recognizer = new window.PDollarRecognizer(),
                    svgElement = $('#canvas svg'),

                    position = svgElement.offset(),

                    shape = null,
                    path = null,
                    isDown = false,
                    points = [],
                    strokes = $scope.strokes,
                    submode = 0;

                gapi.hangout.onApiReady.add(function(eventObj) {
                    var participants = gapi.hangout.getParticipants(),
                        participant = gapi.hangout.getParticipantById(gapi.hangout.getLocalParticipantId());
                    $scope.canvas.text('Hangout id: ' + gapi.hangout.getHangoutId() + '\n' + participant.person.displayName).move(10, 100).font({
                        size: 36,
                        anchor: 'right'
                    });

                    gapi.hangout.data.onStateChanged.add(function(e) {
                        window.$log.info('State Changed:', e);
                    });

                    window.$log.info(participants);
                    gapi.hangout.data.setValue('participantCount', participants.length.toString());
                });

                $(window).keydown(
                    function(e) {
                        $scope.$apply(
                            function() {
                                $log.info('keyDown: ', e);
                                if (e.which >= 49) {
                                    submode = e.which - 49;
                                    if ($scope.mode === MODE_DRAWING) {
                                        switch (submode) {
                                        case 0:
                                            $scope.drawingMode = MODE_DRAWING_PENCIL;
                                            break;
                                        case 1:
                                            $scope.drawingMode = MODE_DRAWING_LINE;
                                            break;
                                        case 2:
                                            $scope.drawingMode = MODE_DRAWING_RECT;
                                            break;
                                        case 3:
                                            $scope.drawingMode = MODE_DRAWING_OVAL;
                                            break;
                                        }
                                    }
                                } else if (e.which === KEY_SHIFT) {
                                    $scope.mode = MODE_SELECT;
                                } else if (e.which === KEY_CONTROL) {
                                    $scope.mode = MODE_COMMAND;
                                }

                                window.$log.info('mode: ', $scope.mode, submode);
                            }
                        );
                    }
                );

                $(window).keyup(function(e) {
                    var i;
                    $scope.$apply(function() {
                        if (e.which !== KEY_SHIFT && e.which !== KEY_CONTROL) {
                            return;
                        }
                        e.stopPropagation();
                        window.$log.info('keyUp: ', e);
                        if ($scope.mode === MODE_COMMAND && strokes.length > 0) {
                            $log.info(points);
                            var result = recognizer.Recognize(points);
                            $log.info(result);
                            if (result !== null && result.Name !== null && result.Score > 0) {
                                if (result.Score < 0.15) {
                                    window.alert('Could not identify gesture with confidence');
                                } else {
                                    if (result.Name === 'null') {
                                        $scope.command.clear();
                                    }
                                }
                            }
                            for (i = 0; i < strokes.length; ++i) {
                                if (strokes[i] !== null) {
                                    strokes[i].remove();
                                }
                            }
                        }
                        $scope.mode = MODE_DRAWING;
                        path = null;
                        points = [];
                        strokes.length = 0;
                    });
                });

                svgElement.on('mousemove touchmove', function(e) {
                    $scope.$apply(function() {
                        var x,
                            y,
                            attr,
                            originalX,
                            originalY,
                            width,
                            height,
                            newX,
                            newY;

                        if (window.TouchEvent !== undefined && e.originalEvent instanceof window.TouchEvent) {
                            x = e.originalEvent.changedTouches[0].pageX - position.left;
                            y = e.originalEvent.changedTouches[0].pageY - position.top;
                        } else {
                            x = e.pageX;
                            y = e.pageY;
                        }
                        if (path !== null || shape !== null) {
                            if ($scope.mode === MODE_COMMAND) {
                                path.attr({
                                    fill: 'none',
                                    stroke: '#0000ff',
                                    'stroke-width': 2
                                }).L(x, y);
                                points[points.length] = new window.Point(x, y, strokes.length);
                            } else if ($scope.mode === MODE_DRAWING) {
                                if ($scope.drawingMode === MODE_DRAWING_PENCIL) {
                                    path.attr({
                                        fill: 'none',
                                        stroke: $scope.color.foreground,
                                        'stroke-width': 1
                                    }).L(x, y);
                                } else if ($scope.drawingMode === MODE_DRAWING_LINE) {
                                    shape.attr({
                                        fill: $scope.color.fill,
                                        stroke: $scope.color.foreground,
                                        x2: x,
                                        y2: y
                                    });
                                } else if ($scope.drawingMode === MODE_DRAWING_RECT) {
                                    attr = shape.attr();
                                    originalX = attr.originalX;
                                    originalY = attr.originalY;
                                    width = Math.abs(originalX - x);
                                    height = Math.abs(originalY - y);
                                    newX = originalX;
                                    newY = originalY;
                                    if (originalX > x) {
                                        newX = x;
                                    }
                                    if (originalY > y) {
                                        newY = y;
                                    }
                                    if (newX !== originalX || newY !== originalY) {
                                        shape.move(newX, newY);
                                    }
                                    shape.size(width, height);
                                    shape.attr({
                                        fill: $scope.color.fill,
                                        stroke: $scope.color.foreground
                                    });
                                } else if ($scope.drawingMode === MODE_DRAWING_OVAL) {
                                    attr = shape.attr();
                                    $log.info(attr);
                                    originalX = attr.originalX;
                                    originalY = attr.originalY;
                                    width = Math.abs(originalX - x) * 2;
                                    height = Math.abs(originalY - y) * 2;
                                    shape.size(width, height);
                                    shape.attr({
                                        fill: $scope.color.fill,
                                        stroke: $scope.color.foreground
                                    });
                                }
                            }
                        }
                    });
                });

                svgElement.on('mousedown touchstart', function(e) {
                    $scope.$apply(function() {
                        var x, y;
                        $log.info('mouseDown:', e);
                        isDown = true;
                        if (window.TouchEvent !== undefined && e.originalEvent instanceof window.TouchEvent) {
                            x = e.originalEvent.changedTouches[0].pageX - position.left;
                            y = e.originalEvent.changedTouches[0].pageY - position.top;
                        } else {
                            x = e.pageX;
                            y = e.pageY;
                        }

                        if ($scope.mode === MODE_SELECT) {
                            $log.info('Select');
                        } else if ($scope.mode === MODE_COMMAND) {
                            path = $scope.canvas.path().attr({
                                fill: 'none',
                                stroke: '#00F',
                                'stroke-width': 2
                            }).M(x, y);
                            points[points.length] = new window.Point(x, y, 1);
                        } else if ($scope.mode === MODE_DRAWING) {
                            $log.info($scope.drawingMode);
                            if ($scope.drawingMode === MODE_DRAWING_PENCIL) {
                                path = $scope.canvas.path().attr({
                                    fill: 'none',
                                    stroke: '#000',
                                    'stroke-width': 1
                                }).M(x, y);
                            } else if ($scope.drawingMode === MODE_DRAWING_LINE) {
                                shape = $scope.canvas.line(x, y, x, y).attr({
                                    fill: 'none',
                                    stroke: '#000',
                                    'stroke-width': 1,
                                    originalX: x,
                                    originalY: y
                                });
                            } else if ($scope.drawingMode === MODE_DRAWING_RECT) {
                                shape = $scope.canvas.rect(1, 1).attr({
                                    fill: 'none',
                                    stroke: '#000',
                                    'stroke-width': 1,
                                    originalX: x,
                                    originalY: y
                                }).move(x, y);
                            } else if ($scope.drawingMode === MODE_DRAWING_OVAL) {
                                shape = $scope.canvas.ellipse(1, 1).attr({
                                    fill: 'none',
                                    stroke: '#000',
                                    'stroke-width': 1,
                                    originalX: x,
                                    originalY: y
                                }).move(x, y);
                            }
                        }
                    });
                });

                svgElement.on('mouseup touchend', function(e) {
                    $scope.$apply(function() {
                        $log.info('mouseUp:', e, $scope.mode);
                        if ($scope.mode === MODE_COMMAND) {
                            strokes[strokes.length] = path;
                        }
                        path = null;
                        shape = null;
                        isDown = false;
                    });

                });
            } catch (e) {
                window.console.info(e);
            }
        } ]);
}(window.angular, window.SVG, window.gapi, window.jQuery));
