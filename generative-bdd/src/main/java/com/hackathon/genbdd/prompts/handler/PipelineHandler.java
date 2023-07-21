package com.hackathon.genbdd.prompts.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hackathon.genbdd.aiclient.AIClient;
import com.hackathon.genbdd.prompts.PromptRepository;
import com.hackathon.genbdd.utils.ClassGenerator;
import lombok.Data;

import java.util.Properties;

@Data
public class PipelineHandler implements PromptResponseHandler {
    private PromptRepository promptRepository;

    public PipelineHandler(PromptRepository promptRepository) {
        this.promptRepository = promptRepository;
    }

    public void handle(Properties context) {
        promptRepository.getByType("PipelineGeneration").ifPresent(prompt -> {
            AIClient client = AIClient.getInstance();
            String response = client.query(prompt, context);

            //Parse the response
            JsonElement jsonelement = JsonParser.parseString(response);
            JsonObject jsonObject = jsonelement.getAsJsonObject();
            context.put("pipeline", jsonObject);

            ClassGenerator classGenerator = new ClassGenerator();
            classGenerator.generateJavaClass("C:\\Users\\Akhil\\IdeaProjects\\hackathon2023\\generative-bdd\\target\\", response);
        });
    }
}
