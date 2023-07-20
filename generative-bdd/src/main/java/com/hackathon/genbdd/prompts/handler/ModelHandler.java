package com.hackathon.genbdd.prompts.handler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hackathon.genbdd.aiclient.AIClient;
import com.hackathon.genbdd.prompts.PromptRepository;
import com.hackathon.genbdd.prompts.model.Prompt;
import com.hackathon.genbdd.utils.ClassGenerator;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class ModelHandler implements PromptResponseHandler{

    private PromptRepository promptRepository;

    public ModelHandler(PromptRepository promptRepository){
        this.promptRepository = promptRepository;
    }

    public void handle(Properties context){
        JsonObject scenarioAnalysis = (JsonObject) context.get("scenario-analysis");
        generateDataEntity(scenarioAnalysis.get("input-data-entity").toString());
        generateDataEntity(scenarioAnalysis.get("output-data-entity").toString());
    }

    private void generateDataEntity(String entity) {
        Prompt prompt = promptRepository.getByType("ModelClassGeneration").get();
        Properties properties = new Properties();
        properties.put("data-entity-json", entity);
        AIClient client = new AIClient();
        String resp = client.query(prompt, properties);
        ClassGenerator classGenerator = new ClassGenerator();
        classGenerator.generateJavaClass("C:\\Users\\Akhil\\IdeaProjects\\hackathon2023\\generative-bdd\\target\\", resp);
    }

    private void generateDataEntities(JsonObject context, String entityCollection) {
        JsonArray inputEntities = context.getAsJsonArray(entityCollection);
        inputEntities.iterator().forEachRemaining(entity -> {
            generateDataEntity(entity.toString());
        });
    }
}
