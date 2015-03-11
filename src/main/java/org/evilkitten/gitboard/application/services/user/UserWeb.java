package org.evilkitten.gitboard.application.services.user;

import java.io.IOException;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.typesafe.config.Config;
import org.evilkitten.gitboard.application.database.query.RecordNotFoundException;
import org.evilkitten.gitboard.application.entity.ProviderType;
import org.evilkitten.gitboard.application.entity.User;
import org.evilkitten.gitboard.application.response.GitboardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@Path("/user")
public class UserWeb {
    private static final Logger LOG = LoggerFactory.getLogger(UserWeb.class);

    private static final String OAUTH_CLIENTID_KEY = "gitboard.private.oauth.google.clientId";
    private static final String OAUTH_CLIENTSECRET_KEY = "gitboard.private.oauth.google.clientSecret";
    private static final String OAUTH_REDIRECTURL_KEY = "gitboard.private.oauth.google.redirectUrl";

    /*
     * Default HTTP transport to use to make HTTP requests.
     */
    private static final HttpTransport TRANSPORT = new NetHttpTransport();

    /*
     * Default JSON factory to use to deserialize JSON.
     */
    private static final JacksonFactory JSON_FACTORY = new JacksonFactory();

    private final Config config;
    private final UserDao userDao;

    @Inject
    public UserWeb(Config config, UserDao userDao) {
        this.config = config;
        this.userDao = userDao;
    }

    @POST
    @Path("/signin")
    @Produces(MediaType.APPLICATION_JSON)
    public GitboardResponse signIn(OAuthSignin signin, @Context HttpServletRequest request) throws IOException, SQLException {
        HttpSession session = request.getSession();
        session.removeAttribute("session.user");

        LOG.info(getClass().getName());
        LOG.info("URL: " + request.getRequestURL());
        LOG.info("session: " + session.getId());
        LOG.info("token: " + signin);

        String sessionState = (String) session.getAttribute("state");

        if (signin == null || sessionState == null) {
            LOG.info("sessionState is null");
            return new GitboardResponse(Response.Status.FORBIDDEN);
        }
        String clientState = signin.getState();
        if (clientState == null || !sessionState.equals(clientState)) {
            LOG.info("clientState is null");
            return new GitboardResponse(Response.Status.FORBIDDEN);
        }
        session.removeAttribute("state");

        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
            TRANSPORT,
            JSON_FACTORY,
            config.getString(OAUTH_CLIENTID_KEY),
            config.getString(OAUTH_CLIENTSECRET_KEY),
            signin.getToken(),
            config.getString(OAUTH_REDIRECTURL_KEY)
        ).execute();

        GoogleIdToken idToken = tokenResponse.parseIdToken();
        String gplusId = idToken.getPayload().getSubject();

        LOG.info("gplusId: " + gplusId);
        LOG.info("email: " + idToken.getPayload().getEmail());

        User user = null;
        try {
            user = userDao.getByProvider(ProviderType.GOOGLE, gplusId);
        } catch (RecordNotFoundException e) {
            LOG.info("No GOOGLE user for {}", gplusId);
            user = new User();

            user.setEmail(idToken.getPayload().getEmail());
            user.setDisplayName(idToken.getPayload().getEmail());
            user.setProviderType(ProviderType.GOOGLE);
            user.setProviderId(gplusId);

            user = userDao.add(user);
            LOG.info("User {} added to system with ID {}", user.getEmail(), user.getId());
        }
        LOG.info("User {} ({}/{}): sign in", user.getId(), user.getProviderType(), user.getProviderId());
        session.setAttribute("session.user", user);
        return new UserStatusResponse(user);
    }

    @GET
    @Path("/signout")
    @Produces(MediaType.APPLICATION_JSON)
    public GitboardResponse signOut(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();

        Object sessionUser = session.getAttribute("session.user");
        if (sessionUser != null && sessionUser instanceof User) {
            User user = (User) sessionUser;
            LOG.info("User {} ({}/{}): sign out", user.getId(), user.getProviderType(), user.getProviderId());
        }
        session.removeAttribute("session.user");

        return new UserStatusResponse();
    }

    public GitboardResponse status(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = null;

        Object sessionUser = session.getAttribute("session.user");
        if (sessionUser != null && sessionUser instanceof User) {
            user = (User) sessionUser;
        }

        return new UserStatusResponse(user);
    }
}
