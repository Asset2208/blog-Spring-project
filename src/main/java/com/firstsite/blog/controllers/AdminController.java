package com.firstsite.blog.controllers;

import com.firstsite.blog.models.Post;
import com.firstsite.blog.models.Role;
import com.firstsite.blog.models.User;
import com.firstsite.blog.repo.RoleRepository;
import com.firstsite.blog.repo.UserRepository;
import com.firstsite.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.String;
import java.lang.Long;
import java.util.Collections;
import java.util.ArrayList;


@Controller
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/admin")
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        return "adminSide/admin";
    }

    @PostMapping("/admin")
    public String  deleteUser(@RequestParam String userId,
                              @RequestParam String action,
                              Model model) {
        if (action.equals("delete")){
            System.out.println("This is:" + userId);
            Long id = Long.parseLong(userId);
            userService.deleteUser(id);
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/gt/{userId}")
    public String  gtUser(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("allUsers", userService.usergtList(userId));
        return "adminSide/admin";
    }

    @GetMapping("/admin/{userId}/edit")
    public String editUser(@PathVariable("userId") Long userId, Model model) {
        Iterable<Role> roles = roleRepository.findAll();
        model.addAttribute("title", "Редактирование пользователя");
        model.addAttribute("user", userService.findUserById(userId));
        model.addAttribute("roles", roles);
        return "adminSide/admin-user-edit";
    }

    @PostMapping("/admin/{userId}/edit")
    public String editUserUpdate(@PathVariable(value = "userId") Long userId, @RequestParam String password, @RequestParam ArrayList<String> myroles, Model model) {
        User user = userService.findUserById(userId);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setPasswordConfirm(bCryptPasswordEncoder.encode(password));

        user.getRoles().clear();

        System.out.println(user);

        for (String i : myroles){
            if (i.equals("ROLE_USER")){
                user.getRoles().add(new Role(1L, "ROLE_USER"));
            }
            if (i.equals("ROLE_ADMIN")){
                user.getRoles().add(new Role(2L, "ROLE_ADMIN"));
            }
        }

        userRepository.save(user);

        return "redirect:/admin";
    }
}
