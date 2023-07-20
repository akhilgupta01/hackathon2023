package com.hackathon.genbdd.prompts.handler;

import com.google.gson.JsonObject;
import com.hackathon.genbdd.aiclient.AIClient;
import com.hackathon.genbdd.prompts.PromptRepository;
import com.hackathon.genbdd.prompts.model.Prompt;
import com.hackathon.genbdd.utils.ClassGenerator;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

@Data
public class WriterHandler implements PromptResponseHandler{
    private PromptRepository promptRepository;

    public WriterHandler(PromptRepository promptRepository){
        this.promptRepository = promptRepository;
    }


    public void handle(Properties context){
        JsonObject scenarioAnalysis = (JsonObject) context.get("scenario-analysis");
        if(StringUtils.isNotBlank(scenarioAnalysis.get("output-data-sink-type").getAsString())){
            Prompt prompt = promptRepository.getByType("WriterClassGeneration").get();

            Properties properties = new Properties();
            properties.put("output-data-entity", scenarioAnalysis.get("output-data-entity").toString());
            properties.put("output-data-sink-type", scenarioAnalysis.get("output-data-sink-type").toString());

            AIClient client = new AIClient();
            String resp = client.query(prompt, properties);
            System.out.println(resp);
            ClassGenerator classGenerator = new ClassGenerator();
            classGenerator.generateJavaClass("C:\\Users\\Akhil\\IdeaProjects\\hackathon2023\\generative-bdd\\target\\", resp);
        }
    }


}
