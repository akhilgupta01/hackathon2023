package com.hackathon.genbdd.prompts.handler;

import com.google.gson.JsonObject;
import com.hackathon.genbdd.aiclient.AIClient;
import com.hackathon.genbdd.prompts.PromptRepository;
import com.hackathon.genbdd.prompts.model.Prompt;
import com.hackathon.genbdd.utils.ClassGenerator;
import lombok.Data;

import java.util.Properties;

@Data
public class TransformationHandler implements PromptResponseHandler{
    private PromptRepository promptRepository;

    public TransformationHandler(PromptRepository promptRepository){
        this.promptRepository = promptRepository;
    }

    public void handle(Properties context){
        JsonObject scenarioAnalysis = (JsonObject) context.get("scenario-analysis");
        if("data-transformation".equalsIgnoreCase(scenarioAnalysis.get("scenario-type").getAsString())){
            Prompt prompt = promptRepository.getByType("TransformationMethodGeneration").get();

            Properties properties = new Properties();
            properties.put("scenario-type", scenarioAnalysis.get("scenario-type").toString());
            properties.put("input-data-entity", scenarioAnalysis.get("input-data-entity").toString());
            properties.put("output-data-entity", scenarioAnalysis.get("output-data-entity").toString());
            properties.put("scenario-text", context.get("scenario-text"));

            AIClient client = new AIClient();
            String resp = client.query(prompt, properties);
            System.out.println(resp);
            ClassGenerator classGenerator = new ClassGenerator();
            classGenerator.generateJavaClass("C:\\Users\\Akhil\\IdeaProjects\\hackathon2023\\generative-bdd\\target\\", resp);
        }
    }


}
