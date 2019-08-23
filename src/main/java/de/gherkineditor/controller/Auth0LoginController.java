package de.gherkineditor.controller;

import com.auth0.AuthenticationController;
import de.gherkineditor.configuration.Auth0WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("unused")
@Controller
@Profile("auth0")
public class Auth0LoginController {

    @Autowired
    private AuthenticationController controller;
    @Autowired
    private Auth0WebSecurityConfig auth0WebSecurityConfig;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    protected String login(final HttpServletRequest req) {
        String redirectUri = req.getScheme() + "://" + req.getServerName();
        if (req.getServerPort() != 80 && req.getServerPort() != 443) {
            redirectUri += ":" + req.getServerPort();
        }
        redirectUri += "/callback";

        String authorizeUrl = this.controller.buildAuthorizeUrl(req, redirectUri)
                .withAudience(String.format("https://%s/userinfo", this.auth0WebSecurityConfig.getDomain()))
                .withScope("openid email profile")
                .build();
        return "redirect:" + authorizeUrl;
    }

}