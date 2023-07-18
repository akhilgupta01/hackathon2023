package com.hackathon.genbdd.prompts.model.entity;

import com.hackathon.genbdd.prompts.model.Generatable;
import lombok.Data;

@Data
public class Entity extends Generatable {
    private String name;
    private String jsonModel;
}
