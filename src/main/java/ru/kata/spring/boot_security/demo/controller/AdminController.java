package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        return "admin";
    }

    // New User
    @PostMapping("/add")
    public String addUser(@ModelAttribute("newUser") User user,
                          @RequestParam(value = "roleNames", required = false) String[] roleNames) {

        if (roleNames != null && roleNames.length > 0) {
            Set<Role> roles = userService.mapRoleNames(roleNames);
            userService.saveUserWithRoles(user, roles);
        } else {
            // если ролей нет в запросе – ставим дефолтную ROLE_USER
            userService.saveUser(user);
        }
        return "redirect:/admin";
    }

    // Edit user
    @PostMapping("/edit")
    public String editUser(@RequestParam("id") Long id,
                           @RequestParam("username") String username,
                           @RequestParam("name") String name,
                           @RequestParam("age") int age,
                           @RequestParam("email") String email,
                           @RequestParam(value = "password", required = false) String password,
                           @RequestParam(value = "roleNames", required = false) String[] roleNames) {

        User user = userService.getUser(id);
        user.setUsername(username);
        user.setName(name);
        user.setAge(age);
        user.setEmail(email);

        if (password != null && !password.isBlank()) {
            user.setPassword(password);
        }

        if (roleNames != null && roleNames.length > 0) {
            Set<Role> roles = userService.mapRoleNames(roleNames);
            user.setRoles(roles);
        }

        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
