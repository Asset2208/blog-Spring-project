package com.firstsite.blog.controllers;

import com.firstsite.blog.models.Post;
import com.firstsite.blog.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/")
    public String home(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("title", "Главная страница");
        return "home";
    }



    @GetMapping("/about")
    public String blogMain(Model model) {
        model.addAttribute("title", "Страница про нас");
        return "about";
    }
}
