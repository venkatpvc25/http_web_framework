package com.example.app;

import java.io.IOException;

import com.pvc.backend.WebServer;

public class MyApp {
    public static void main(String[] args) throws IOException {
        new WebServer("com.example.app.controller");
    }
}