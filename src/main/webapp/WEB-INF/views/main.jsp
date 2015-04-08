<!DOCTYPE html>
<html ng-app="gitboard">
<head>
    <base href="${it.baseUrl}"/>
    <title>GitBoard</title>

    <meta name="google-signin-clientid" content="114100310474.apps.googleusercontent.com"/>
    <meta name="google-signin-scope" content="https://www.googleapis.com/auth/plus.login email"/>
    <meta name="google-signin-requestvisibleactions" content="http://schemas.google.com/AddActivity"/>
    <meta name="google-signin-cookiepolicy" content="single_host_origin"/>

    <script src="//hangoutsapi.talkgadget.google.com/talkgadget/apps/gadgets/js/rpc.js" type="text/javascript"></script>
    <script src="//hangoutsapi.talkgadget.google.com/hangouts/api/hangout.js?v=1.4" type="text/javascript"></script>

    <link href="public/lib/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">
    <link href="public/lib/angular-ui/colorpicker/css/colorpicker.css" rel="stylesheet">

    <script src="//code.jquery.com/jquery-1.11.0.min.js" type="text/javascript"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.12/angular.min.js" type="text/javascript"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.12/angular-route.min.js"
            type="text/javascript"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.12/angular-sanitize.min.js"
            type="text/javascript"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.12/angular-resource.min.js"
            type="text/javascript"></script>

    <script src="public/lib/angular-ui/ui-bootstrap-tpls-0.10.0.min.js"></script>

    <link href="public/lib/angular-xeditable-0.1.8/css/xeditable.css" rel="stylesheet"/>
    <script src="public/lib/angular-xeditable-0.1.8/js/xeditable.min.js"></script>

    <script src="public/lib/angular-ui/colorpicker/js/bootstrap-colorpicker-module.js"></script>

    <script src="public/lib/svg/svg.js" type="text/javascript"></script>
    <script src="public/lib/svg/svg.path.js" type="text/javascript"></script>
    <script src="public/lib/pdollar/pdollar.js" type="text/javascript"></script>

    <script src="https://rawgit.com/Atmosphere/atmosphere-javascript/javascript-project-2.2.8/modules/jquery/src/main/webapp/jquery/jquery.atmosphere.js"
            type="text/javascript"></script>

    <jsp:include page="styles.jsp"/>
</head>
<body>
<div ng-controller="UserController">
    <input type="button" id="signoutButton" ng-click="signOut()" value="Sign out"/>
</div>

<div ng-view></div>
<script src="application/modules/lib/SVG.js"></script>

<script src="application/app.js"></script>
<script src="application/modules/user/UserModule.js"></script>
<!-- Front page -->
<script src="application/modules/frontpage/frontpage.controller.js"></script>

<!-- Whiteboard -->
<script src="application/modules/whiteboard/whiteboard.module.js"></script>
<script src="application/modules/whiteboard/whiteboard.controller.js"></script>
<script src="application/modules/whiteboard/whiteboard.resource.js"></script>
</body>
</html>
