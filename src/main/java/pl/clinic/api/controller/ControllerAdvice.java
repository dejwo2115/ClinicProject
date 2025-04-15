package pl.clinic.api.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @SuppressWarnings("unused")
    @ModelAttribute
    public void addAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
        String name = isLoggedIn ? authentication.getName() : null;

        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("name", name);
    }

}