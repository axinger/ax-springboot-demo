package com.github.axinger.controller;

import com.github.axinger.entity.Department;
import com.github.axinger.entity.User;
import com.github.axinger.service.OrganizationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/organization")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping("/users")
    public String listUsers(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        List<User> users = organizationService.getAllUsers();
        model.addAttribute("users", users);
        return "users/list";
    }

    @GetMapping("/departments")
    public String listDepartments(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        List<Department> departments = organizationService.getAllDepartments();
        model.addAttribute("departments", departments);
        return "departments/list";
    }
}
