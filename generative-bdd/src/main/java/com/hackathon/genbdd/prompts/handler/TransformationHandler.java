package com.hackathon.genbdd.prompts.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hackathon.genbdd.aiclient.AIClient;
import com.hackathon.genbdd.prompts.PromptRepository;
import lombok.Data;

import java.util.Properties;

@Data
public class TransformationHandler implements PromptResponseHandler {
    private PromptRepository promptRepository;

    public TransformationHandler(PromptRepository promptRepository) {
        this.promptRepository = promptRepository;
    }

    public void handle(Properties context) {
        promptRepository.getByType("TransformationMethodGeneration").ifPresent(prompt -> {
            AIClient client = AIClient.getInstance();
            String response = client.query(prompt, context);

            //Parse the response
            JsonElement jsonelement = JsonParser.parseString(response);
            JsonObject jsonObject = jsonelement.getAsJsonObject();
            context.put("transformation", jsonObject.get("classBody").toString());
        });
    }
}
