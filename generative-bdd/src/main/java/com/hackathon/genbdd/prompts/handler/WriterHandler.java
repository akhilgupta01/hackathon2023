package com.hackathon.genbdd.prompts.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hackathon.genbdd.aiclient.AIClient;
import com.hackathon.genbdd.prompts.PromptRepository;
import lombok.Data;

import java.util.Objects;
import java.util.Properties;

@Data
public class WriterHandler implements PromptResponseHandler{
    private PromptRepository promptRepository;

    public WriterHandler(PromptRepository promptRepository){
        this.promptRepository = promptRepository;
    }

    public void handle(Properties context) {
        if (Objects.nonNull(context.get("output-data-sink-type"))) {
            promptRepository.getByType("WriterClassGeneration").ifPresent(prompt -> {
                AIClient client = AIClient.getInstance();
                String response = client.query(prompt, context);

                //Parse the response
                JsonElement jsonelement = JsonParser.parseString(response);
                JsonObject jsonObject = jsonelement.getAsJsonObject();
                context.put("writer", jsonObject.get("classBody").toString());
            });
        }
    }


}
