<!DOCTYPE html>
<html ng-app="svb">
<head>
    <base href="${it.baseUrl}"/>
    <title>ColLab</title>

    <meta name="google-signin-clientid" content="114100310474.apps.googleusercontent.com"/>
    <meta name="google-signin-scope" content="https://www.googleapis.com/auth/plus.login email"/>
    <meta name="google-signin-requestvisibleactions" content="http://schemas.google.com/AddActivity"/>
    <meta name="google-signin-cookiepolicy" content="single_host_origin"/>

    <script src="//hangoutsapi.talkgadget.google.com/talkgadget/apps/gadgets/js/rpc.js" type="text/javascript"></script>
    <script src="//hangoutsapi.talkgadget.google.com/hangouts/api/hangout.js?v=1.4" type="text/javascript"></script>

    <script src="//code.jquery.com/jquery-1.11.0.min.js" type="text/javascript"></script>

    <link href="public/lib/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">
    <link href="public/style.less" rel="stylesheet/less" type="text/css">

    <jsp:include page="styles.jsp"/>
</head>
<body>
<div>
    <button id="signinButton">Sign in with Google</button>
    <button id="signoutButton" style="display: none;">Sign out</button>
</div>

<img src="public/images/collab.png"/>

<script type="text/javascript">
    var state = '${it.state}';

    function render() {
        var signinButton = document.getElementById('signinButton');
        signinButton.addEventListener('click', function(e) {
            window.console.log('click', e);
            gapi.auth.signIn({
                callback: signinCallback
            });
            e.stopPropagation();
            e.preventDefault();
        });

        var signoutButton = document.getElementById('signoutButton');
        signoutButton.addEventListener('click', function() {
            gapi.auth.signOut();
        });
    }

    (function() {
        var po = document.createElement('script');
        po.type = 'text/javascript';
        po.async = true;
        po.src = 'https://apis.google.com/js/client:plusone.js?onload=render';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(po, s);
    })();

    function signinCallback(authResult) {
        'use strict';

        window.console.log('This is the callback: ', authResult);
        if (authResult.status.signed_in) {
            window.gapi.client.load(
                    'plus',
                    'v1',
                    function() {
                        window.console.log('load');
                        // Update the app to reflect a signed in user
                        // Hide the sign-in button now that the user is authorized, for example:

                        if (authResult.status.method !== 'AUTO') {
                            var request = window.gapi.client.plus.people.get({
                                'userId': 'me'
                            });
                            /*
                             request.execute(function(resp) {
                             window.console.log('execute', resp);
                             window.console.log('ID: ' + resp.id);
                             window.console.log('Display Name: ' + resp.displayName);
                             window.console.log('Image URL: ' + resp.image.url);
                             window.console.log('Profile URL: ' + resp.url);
                             window.console.log('Before sign in');
                             var data = {
                             token: authResult.code,
                             state: window.state
                             };
                             window.console.log('data', data, JSON.stringify(data));*/
                            window.$.ajax(
                                    {
                                        url: 'api/user/signin',
                                        type: 'POST',
                                        contentType: 'application/json',
                                        data: JSON.stringify({
                                            token: authResult.code,
                                            state: window.state
                                        })
                                    }
                            ).done(function(response) {
                                        window.console.log('response', response);
                                        window.location = 'home';
                                    });
                            /*
                             window.console.log('After sign in');
                             });*/
                        }
                    }
            );
        } else {
            // Update the app to reflect a signed out user
            // Possible error values:
            // "user_signed_out" - User is signed-out
            // "access_denied" - User denied access to your app
            // "immediate_failed" - Could not automatically log in the user
            window.console.log('Sign-in state: ' + authResult.error);
            window.$.ajax(
                    {
                        url: 'api/user/signout',
                        type: 'GET',
                        contentType: 'application/json'
                    }
            ).done(
                    function(r) {
                        window.console.log('done', r);
                    }
            );
            window.location = 'signin';
        }
    }
</script>
</body>
</html>
