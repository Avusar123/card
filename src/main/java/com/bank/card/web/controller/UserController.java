package com.bank.card.web.controller;

import com.bank.card.user.infrastructure.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    @Autowired
    private UserRepo userRepo;
}
