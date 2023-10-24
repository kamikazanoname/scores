package ru.pflb.scores.http;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pflb.scores.service.LoginService;

@RestController
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping(path = "/{userId}/login")
    public String login(@PathVariable int userId, @RequestParam String password, HttpServletResponse servletResponse) {
        Cookie authCookie = new Cookie("SessionKey", loginService.login(userId, password));
        authCookie.setPath("/");
        authCookie.setMaxAge(86400);
        servletResponse.addCookie(authCookie);
        return "Authorized!";
    }
}