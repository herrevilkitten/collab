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
        KEY_ESCAPE = 27,
        KEY_CONTROL = 17;

    angular.module('gitboard.whiteboard')
        .controller('WhiteBoardController', function ($scope,
                                                     $http,
                                                     $interval,
                                                     $location,
                                                     $log,
                                                     $timeout,
                                                     CurrentBoard,
                                                     MessageFactory,
                                                     ShapeFactory,
                                                     SocketFactory,
                                                     Whiteboard) {
            function addShape(shape) {
                var segments;
                window.console.log('Adding', shape, 'from welcome');

                switch (shape.type) {
                case '.PathShape':
                    segments = shape.segments.map(function(segment) {
                        return segment.type + segment.position.x + ' ' + segment.position.y
                    }).join('');

                    window.console.log('Segments', segments);

                    $scope.canvas.children()[$scope.currentLayer].path(segments).attr({
                        fill: 'none',
                        stroke: shape.stroke,
                        'stroke-width': 1,
                        '__id': shape.id
                    });
                    break;

                case '.LineShape':
                    $scope.canvas.children()[$scope.currentLayer].line(shape.start.x, shape.start.y, shape.end.x, shape.end.y)
                        .attr({
                            stroke: shape.stroke,
                            'stroke-width': 1,
                            '__id': shape.id
                        });
                    break;

                case '.RectangleShape':
                    $scope.canvas.children()[$scope.currentLayer].rect(shape.dimensions.width, shape.dimensions.height)
                        .move(shape.position.x, shape.position.y)
                        .attr({
                            fill: shape.fill,
                            stroke: shape.stroke,
                            '__id': shape.id,
							'class': 'layer' + $scope.currentLayer
                        });
                    break;

                case '.EllipseShape':
                    $scope.canvas.children()[$scope.currentLayer].ellipse(shape.dimensions.width, shape.dimensions.height)
                        .move(shape.position.x, shape.position.y)
                        .attr({
                            fill: shape.fill,
                            stroke: shape.stroke,
                            '__id': shape.id
                        });
                    break;
                }
            }

            function onWelcomeMessage(socket, response, message) {
                $scope.uuid = message.uuid;
                $scope.user.id = message.user.id;
                $scope.board = message.whiteboard;
                angular.forEach(message.whiteboard.shapes, addShape);
            }

            function onAddShapeMessage(socket, response, message) {
                if (message.actor.id == $scope.user.id) {
                    // Since the database assigns shape ids, update the id of the last shape
                    // and return so it doesn't draw twice
                    return;
                }

                // Add the shape to the board
                angular.forEach([message.shape], addShape);

            }

            var messageHandlers = {
                '.WelcomeMessage': onWelcomeMessage,
                '.AddShapeMessage': onAddShapeMessage
            };


            function onMessage(socket, response, message) {
                var handler,
                    type;

                console.log('onMessage', message);
                handler = messageHandlers[message.type];
                if (handler) {
                    handler(socket, response, message);
                }
            }

            try {
                $scope.color = {
                    foreground: 'rgba(0, 0, 0, 1)',
                    fill: 'rgba(255, 255, 255, 1)',
                    complementary: function (color) {
                        return new SVG.Color(color).complementary();
                    }
                };
				$scope.currentLayer = 0;
                $scope.layerIsVisible = "public/images/eyeIcon.png";
                $scope.layerIsInvisible = "public/images/closedEyeIcon.png";
                $scope.layers = [{ index: 0, name: "default layer", visibile: true, src: $scope.layerIsVisible }];
                $scope.showLayer = true;/*function(index) {
                    return false;
                };*/
				$scope.altLayerName = "";
				$scope.editLayerNameMode = -1;
                $scope.boardId = CurrentBoard;
                $scope.board = null;
                $scope.user = {id: 0};
                $scope.selected = null;
                $scope.selectMode = "select";
                $scope.manipulationReady = false;
                $scope.uuid = null;
                $scope.mode = MODE_DRAWING;
                $scope.drawingMode = MODE_DRAWING_PENCIL;
                $scope.strokes = [];
                $scope.defaultCanvasSize = {
                    height: 1080,
                    width: 1920
                };
                $scope.canvas = SVG('canvas').size($scope.defaultCanvasSize.width, $scope.defaultCanvasSize.height);
					$scope.canvas.group();
                $scope.toolboxSide = 'left';
                console.log('scope', $scope);
                $scope.command = {
                    home: function() {
                        $scope.mode = MODE_DRAWING;
                        $location.path('home');
                    },
                    clear: function () {
                        if (window.confirm('Clear the drawing?')) {
                            $scope.canvas.children()[$scope.currentLayer].clear();
                        }
                        $scope.mode = MODE_DRAWING;
                    },
                    switchSides: function () {
                        $scope.toolboxSide = ($scope.toolboxSide === 'left') ? 'right' : 'left';
                        $scope.mode = MODE_DRAWING;
                    },
                    closeLayersWindow: function(){
                        $event.target.css({
                            visibility: 'hidden'
                        });
                    },
                    addNewLayer: function () {
                        $scope.layers.push({ index: $scope.layers.length, name: "new Layer " + $scope.layers.length, visible: true, src: $scope.layerIsVisible });
						$scope.canvas.group();
					},
                    changeVisibility: function (index) {
                        if ($scope.layers[index].src == $scope.layerIsVisible) {
                            $scope.layers[index].src = $scope.layerIsInvisible;
                            $scope.layers[index].visibile = false;
							
							$scope.canvas.children()[index].attr({
								visibility: 'hidden'
							});
                        }
                        else {
                            $scope.layers[index].src = $scope.layerIsVisible;
                            $scope.layers[index].visibile = true;
							
							$scope.canvas.children()[index].attr({
								visibility: 'visible'
							});
                        }
                    },
					switchActiveLayer : function(index){
						$scope.currentLayer = index;
					},
					changeLayerName : function(index) {
						$scope.editLayerNameMode = index;
					},
                    copyBoard: function() {
                        Whiteboard
                            .copy($scope.boardId)
                            .then(function(response) {
                                $location.path('board/' + response.data.id);
                            })
                            .catch(function(error) {
                                $log.error('Unable to copy board:', error);
                            });
                        $scope.mode = MODE_DRAWING;
                    },
                    forkBoard: function() {
                        Whiteboard
                            .fork($scope.boardId)
                            .then(function(response) {
                                $location.path('board/' + response.data.id);
                            })
                            .catch(function(error) {
                                $log.error('Unable to fork board:', error);
                            });
                        $scope.mode = MODE_DRAWING;
                    },
                    shareBoard: function() {
                        $scope.mode = MODE_DRAWING;
                    },
                    differences: function() {
                        Whiteboard
                            .differences($scope.board.originalId, $scope.board.id)
                            .then(function(response) {
                            })
                            .catch(function(error) {
                                $log.error('Unable to fork board:', error);
                            });
                        $scope.mode = MODE_DRAWING;
                    }
                };

                var recognizer = new window.PDollarRecognizer(),
                    svgElement = $('#canvas').find('svg'),

                    position = svgElement.offset(),

                    shape = null,
                    path = null,
                    isDown = false,
                    points = [],
                    strokes = $scope.strokes,
                    submode = 0;

                $(window).keyup(function (e) {
                    $scope.$apply(function () {
                        $log.info('keyUp: ', e);
                        if (e.which === KEY_ESCAPE) {
                            if (shape) {
                                shape.remove();
                                shape = null;
                            }
                            if (path) {
                                path.remove();
                                path = null;
                            }
                            isDown = false;
                        }
                    });
                });
				
				$(window).keydown(function (e) {
                    $scope.$apply(function () {
                        if((e.keyCode == 187 || e.keyCode == 189) && e.ctrlKey){
							var zoomFactor = 0;
							if(e.keyCode == 187)
								zoomFactor=0.2;
							else 
								zoomFactor=-0.2;
							
							var drawingLayers = $scope.canvas.children();
							for (var i = 0; i < drawingLayers.length; i+=1) {
								var prevVal = drawingLayers[i].attr('transform');
								
								var newVal = 0;
								if(typeof prevVal == "undefined"){
									newVal = 1;
								}
								else{
									newVal = parseFloat(prevVal.substring(6, prevVal.length-1));		
									$log.info('retrievedVal : ', newVal);									
								}
								newVal += zoomFactor;
								$log.info('The value from the substring is : ', newVal);
								
								drawingLayers[i].attr({'transform' : "scale(" + newVal + ")"});
							}
							
							var origHeight = $scope.canvas.attr("height");
							//var origHeightInt = parseInt(origHeight.substring(0, origHeight.length));
							var origWidth = $scope.canvas.attr("width");
							//var origWidthInt = parseInt(origWidth.substring(0, origWidth.length));
							
							var newHeight = origHeight + ($scope.defaultCanvasSize.height * zoomFactor);
							var newWidth = origWidth + ($scope.defaultCanvasSize.width * zoomFactor);
							$scope.canvas.attr({
								width: newWidth,
								height: newHeight
							})
							event.preventDefault();
						}
                    });
                });

				svgElement.on('mousemove touchmove', function (e) {
                    $scope.$apply(function () {
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
                            x = e.pageX + 5;
                            y = e.pageY - 45;
                        }
                        if (isDown) {
                            if ($scope.mode === MODE_COMMAND) {
                                path.attr({
                                    fill: 'none',
                                    stroke: '#0000ff',
                                    'stroke-width': 2
                                }).L(x, y);
                                points[points.length] = new window.Point(x, y, strokes.length);
                            } else if ($scope.mode === MODE_DRAWING && (shape || path)) {
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
                                    width = Math.abs(originalX - x );
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
                                        stroke: $scope.color.foreground,
                                        class: 'layer' + ($scope.layers.length - 1)
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
                            } else if ($scope.mode === MODE_SELECT && $scope.selectMode === "move" && $scope.selected != null) {
                                //do something for move
                                //$scope.selected.move(x, y);
                            }
                        }
                    });
                });

                svgElement.on('mousedown touchstart', function (e) {
                    $scope.$apply(function () {
                        var x, y;
                        $log.info('mouseDown:', e);
                        isDown = true;
                        if (window.TouchEvent !== undefined && e.originalEvent instanceof window.TouchEvent) {
                            x = e.originalEvent.changedTouches[0].pageX - position.left;
                            y = e.originalEvent.changedTouches[0].pageY - position.top;
                        } else {
                            x = e.pageX + 5;
                            y = e.pageY - 45;

                            $log.info("The x loc: " + x + " the y loc: " + y);
                        }

                        if ($scope.mode === MODE_SELECT) {
                            if ($scope.selectMode === "select") {
                                $log.info('Select');
                                var children = $scope.canvas.children()[$scope.currentLayer].children();
                                var topChild = null;
                                var foundSelected = false;
                                var firstChild = null;
                                for (var i = 0; i < children.length; ++i) {
                                    if (!children[i].attr('not-selectable') && children[i].inside(x, y)) {
                                        if (!firstChild) {
                                            firstChild = children[i];
                                        }
                                        window.console.log('Inside:', children[i]);
                                        if ($scope.selected) {
                                            window.console.log('Parent:', $scope.selected.cloneParent)
                                        }
                                        if (!$scope.selected || ($scope.selected && foundSelected)) {
                                            topChild = children[i];
                                            break;
                                        } else if (children[i] === $scope.selected.cloneParent) {
                                            window.console.log('Found selected');
                                            foundSelected = true;
                                        }
                                    }
                                }
                                if ($scope.selected) {
                                    $scope.selected.remove();
                                    $scope.selected = null;
                                    if (topChild === null) {
                                        window.console.log('No topChild, using firstChild');
                                        topChild = firstChild;
                                    }
                                }
                                if (topChild !== null) {
                                    window.console.log('Top child is', topChild);
                                    $scope.selected = topChild.clone();
                                    $scope.selected.attr({
                                        stroke: '#00F',
                                        'stroke-width': 2,
                                        'fill-opacity': 0,
                                        'not-selectable': true
                                    });
                                    $scope.selected.cloneParent = topChild; 
                                }
                            } else if ($scope.selectMode === "move") {
                                //$scope.selected.cloneParent.attr({ "visibility": "hidden" });
                                //shape = $scope.selected;
                                //do something for move here
                            }

                        } else if ($scope.mode === MODE_COMMAND) {
                            path = $scope.canvas.children()[$scope.currentLayer].path().attr({
                                fill: 'none',
                                stroke: '#00F',
                                'stroke-width': 2
                            }).M(x, y);
                            points[points.length] = new window.Point(x, y, 1);
                        } else if ($scope.mode === MODE_DRAWING) {
                            if ($scope.drawingMode === MODE_DRAWING_PENCIL) {
                                path = $scope.canvas.children()[$scope.currentLayer].path().attr({
                                    fill: 'none',
                                    stroke: $scope.color.foreground,
                                    'stroke-width': 1
                                }).M(x, y);
                            } else if ($scope.drawingMode === MODE_DRAWING_LINE) {
                                shape = $scope.canvas.children()[$scope.currentLayer].line(x, y, x, y).attr({
                                    fill: $scope.color.fill,
                                    stroke: $scope.color.foreground,
                                    'stroke-width': 1,
                                    originalX: x,
                                    originalY: y
                                });
                            } else if ($scope.drawingMode === MODE_DRAWING_RECT) {
                                shape = $scope.canvas.children()[$scope.currentLayer].rect(1, 1).attr({
                                    fill: $scope.color.fill,
                                    stroke: $scope.color.foreground,
                                    'stroke-width': 1,
                                    originalX: x,
                                    originalY: y
                                }).move(x, y);
                            } else if ($scope.drawingMode === MODE_DRAWING_OVAL) {
                                shape = $scope.canvas.children()[$scope.currentLayer].ellipse(1, 1).attr({
                                    fill: $scope.color.fill,
                                    stroke: $scope.color.foreground,
                                    'stroke-width': 1,
                                    originalX: x,
                                    originalY: y
                                }).move(x, y);
                            }
                        }
                    });
                });

                svgElement.on('mouseup touchend', function (e) {
                    $scope.$apply(function () {
                        isDown = false;
                        $log.info('mouseUp:', e, $scope.mode);
                        if (shape === null && path === null) {
                            return;
                        }
                        if ($scope.mode === MODE_COMMAND) {
                            strokes[strokes.length] = path;
                        }
                        if ($scope.mode === MODE_SELECT) {
                           
                        }

                        $scope.addAction(shape || path);
                        path = null;
                        shape = null;
                       
                        
                    });

                });

                $scope.addAction = function (action) {
                    var shape,
                        message;

                    window.console.log('Adding action:', action);
                    switch (action.type) {
                        case 'path':
                            shape = ShapeFactory.createPath(action);
                            break;
                        case 'line':
                            shape = ShapeFactory.createLine(action);
                            break;
                        case 'rect':
                            shape = ShapeFactory.createRectangle(action);
                            break;
                        case 'ellipse':
                            shape = ShapeFactory.createEllipse(action);
                            break;
                    }
                    message = MessageFactory.createAddShape(shape);
                    $scope.socket.publish(message);
                };

                $interval(function () {
                    $scope.socket.publish(MessageFactory.createHeartbeat());
                }, 90000);

                $scope.socket = SocketFactory
                    .createSocket()
                    .onMessage(onMessage)
                    .onError(SocketFactory.reconnectOnErrorHandler)
                    .open();
            } catch (e) {
                window.console.error(e);
            }
        })
        .directive("draggableWindow", ['$document', function ($document) {
            return {
                link: function (scope, element, attr) {
                    var startX = 0, startY = 0, x = 300, y = 500;

                    element.on('mousedown', function (event) {
                        event.preventDefault();
                        startX = event.screenX - x;
                        startY = event.screenY - y;
                        $document.on('mousemove', mousemove);
                        $document.on('mouseup', mouseup);
                    });

                    function mousemove(event) {
                        y = event.screenY - startY;
                        x = event.screenX - startX;
                        element.css({
                            top: y + 'px',
                            left: x + 'px'
                        });
                    }

                    function mouseup() {
                        $document.off('mousemove', mousemove);
                        $document.off('mouseup', mouseup);
                    }
                }
            }
        }])
		.directive('ngEnter', function () {
			return function (scope, element, attrs) {
				element.bind("keydown keypress", function (event) {
					if(event.which === 13) {
						scope.layers[scope.editLayerNameMode].name = scope.altLayerName;
						scope.editLayerNameMode = -1;

						event.preventDefault();
					}
				});
			};
		});
}(window.angular, window.SVG, window.gapi, window.jQuery));
