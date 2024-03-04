package pl.clinic.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@AllArgsConstructor
@SuppressWarnings("unused")
public class HomeController {
    public static final String HOME = "/";
    @SuppressWarnings("unused")
    @RequestMapping(value = HOME, method = RequestMethod.GET)
    public String homePage() {
        return "home";
    }
    @SuppressWarnings("unused")
    @GetMapping("successMessage")
    public String getSuccessMessage() {
        return "successMessage";
    }
}