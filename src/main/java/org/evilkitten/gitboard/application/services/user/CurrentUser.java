package org.evilkitten.gitboard.application.services.user;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.inject.servlet.SessionScoped;
import org.evilkitten.gitboard.application.entity.User;

@SessionScoped
public class CurrentUser {
    private HttpSession httpSession;

    @Inject
    public CurrentUser(HttpServletRequest request) {
        this.httpSession = request.getSession();
    }

    public User get() {
        return (User) httpSession.getAttribute("session.user");
    }
}
