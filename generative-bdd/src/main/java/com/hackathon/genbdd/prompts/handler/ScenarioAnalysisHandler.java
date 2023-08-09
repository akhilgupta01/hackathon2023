package com.hackathon.genbdd.prompts.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hackathon.genbdd.aiclient.AIClient;
import com.hackathon.genbdd.prompts.PromptRepository;
import lombok.Data;

import java.util.Properties;

@Data
public class ScenarioAnalysisHandler implements PromptResponseHandler {
    private PromptRepository promptRepository;

    public ScenarioAnalysisHandler(PromptRepository promptRepository) {
        this.promptRepository = promptRepository;
    }


    public void handle(Properties context) {
        promptRepository.getByType("ScenarioAnalysis").ifPresent(prompt -> {
            AIClient client = AIClient.getInstance();
            String response = client.query(prompt, context);

            //Parse the response
            JsonElement jsonelement = JsonParser.parseString(response);
            JsonObject jsonObject = jsonelement.getAsJsonObject();
            for(String key: jsonObject.keySet()){
                context.put(key, jsonObject.get(key).toString());
            }
        });
    }
}
