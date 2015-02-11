package org.pixi.collab.server.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
public class JspWeb {
    private static final Logger LOG = LoggerFactory.getLogger(JspWeb.class);

    @GET
    @Path("/view")
    @Produces(MediaType.TEXT_HTML)
    public Viewable main(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> model = new HashMap<>();

        String state = new BigInteger(130, new SecureRandom()).toString(32);
        session.setAttribute("state", state);

        model.put("state", state);
        model.put("baseUrl", calculateBaseUrl(request));

        return new Viewable("/WEB-INF/views/main", model);
    }

    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    public Viewable login(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> model = new HashMap<>();

        String state = new BigInteger(130, new SecureRandom()).toString(32);
        session.setAttribute("state", state);

        model.put("state", state);
        model.put("baseUrl", calculateBaseUrl(request));

        return new Viewable("/WEB-INF/views/login", model);
    }

    private String calculateBaseUrl(HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        String contextPath = request.getContextPath();
        try {
            URL url = new URL(requestUrl);
            return new URL(url, contextPath + "/").toString();
        } catch (MalformedURLException e) {
            LOG.error("Received a request with a malformed requestURL.  requestURL = {}, contextPath = {}",
                requestUrl,
                contextPath);
            throw new RuntimeException(e);
        }
    }
}
