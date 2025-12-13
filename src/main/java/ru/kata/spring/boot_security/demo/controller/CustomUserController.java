package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class CustomUserController {

    private final UserService userService;

    public CustomUserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user")
    public String showUserInfo(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("user", currentUser);
        return "user";
    }


    @PostMapping("/user")
    public String updateUserProfile(@AuthenticationPrincipal User currentUser,
                                    @ModelAttribute("user") User userData) {
        User user = userService.getUser(currentUser.getId());
        user.setName(userData.getName());
        user.setEmail(userData.getEmail());
        user.setAge(userData.getAge());
        userService.updateUser(user);
        return "redirect:/user";
    }
}
