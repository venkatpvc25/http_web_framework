package com.example.app.controller;

import java.io.IOException;

import com.example.app.model.User;

import com.pvc.backend.annotations.Controller;
import com.pvc.backend.annotations.GetMapping;
import com.pvc.backend.annotations.PathVariable;
import com.pvc.backend.annotations.RequestParam;
import com.pvc.backend.model.Response;

@Controller(path = "/user")
public class UserController {

    @GetMapping("/users/{number}")
    public Response<User> getUsers(@PathVariable(value = "number") Long number) throws IOException {
        User user = new User("venkat");
        return Response.<User>builder().data(user).status(200).build();
    }

    @GetMapping("/emp")
    public Response<User> getEmployee(@RequestParam("id") String id) {
        User user = new User("venkat-employee");
        return Response.<User>builder().data(user).status(200).build();
    }
}
