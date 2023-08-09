package com.hackathon.genbdd.aiclient;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatRequest {
    private String model;
    private List<Message> messages;
    private double temperature = 0;

    public ChatRequest(String prompt) {
        this("gpt-3.5-turbo", prompt);
    }

    public ChatRequest(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }
}
