package com.hackathon.genbdd.prompts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.genbdd.prompts.model.Prompt;
import lombok.Getter;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PromptRepository {
    public static final PromptRepository INSTANCE = new PromptRepository();

    @Getter
    private List<Prompt> prompts;

    private boolean initialized;

    private void initialize() {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:prompts/prompts.json");
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            String promptsJson = FileCopyUtils.copyToString(reader);
            ObjectMapper objectMapper = new ObjectMapper();
            prompts = Arrays.asList(objectMapper.readValue(promptsJson, Prompt[].class));
            for(Prompt prompt: prompts){
                prompt.setTemplate(readTemplate(resourceLoader, prompt.getTemplateRef()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        initialized = true;
    }

    private String readTemplate(ResourceLoader resourceLoader, String templateRef) {
        Resource resource = resourceLoader.getResource("classpath:prompts/" + templateRef);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static PromptRepository getInstance(){
        if(!INSTANCE.initialized){
            INSTANCE.initialize();
        }
        return INSTANCE;
    }

    public Optional<Prompt> getByType(String type){
        return prompts.stream().filter(p -> p.getType().equals(type)).findFirst();
    }

    public static void main(String[] args) {
        Prompt prompt = new Prompt();
        prompt.setType("ScenarioAnalysis");
        prompt.setTemplateRef("scen.txt");

        List<Prompt> prompts =  new ArrayList<>();
        prompts.add(prompt);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(prompts);
            System.out.println(json);

            json = "[\n" +
                    "  {\n" +
                    "    \"type\":\"ScenarioAnalysis\",\n" +
                    "    \"template\":null,\n" +
                    "    \"templateRef\": \"scenario-analysis.txt\",\n" +
                    "    \"response\":null\n" +
                    "  }\n" +
                    "]";

            List<Prompt> prompts2 = Arrays.asList(objectMapper.readValue(json, Prompt[].class));
            System.out.println(prompts2);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
