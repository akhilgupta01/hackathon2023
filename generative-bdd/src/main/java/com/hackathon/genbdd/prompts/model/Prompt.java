package com.hackathon.genbdd.prompts.model;

import lombok.Data;

@Data
public class Prompt {
    private String type;
    private String template;
    private String templateRef;

}
