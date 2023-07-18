package com.hackathon.genbdd.prompts.model;

import lombok.Data;

@Data
public abstract class Generatable {
    private boolean generated;

    private String fileName;
}
