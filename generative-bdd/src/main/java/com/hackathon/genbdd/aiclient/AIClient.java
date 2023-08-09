package com.hackathon.genbdd.aiclient;

import com.hackathon.genbdd.prompts.model.Prompt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

public class AIClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AIClient.class);

    private static final String apiUrl = "https://api.openai.com/v1/chat/completions";

    private RestTemplate restTemplate;

    private final PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("${", "}");

    private long lastRequestMillis = 0L;

    private static final AIClient INSTANCE = new AIClient();

    public static AIClient getInstance(){
        return INSTANCE;
    }

    private AIClient(){}

    private void waitIfNecessary(){
        long currentTimeMillis = System.currentTimeMillis();
        if((currentTimeMillis - lastRequestMillis) < 20_000){
            try{
                LOGGER.debug("Waiting for " + (20_000 - (currentTimeMillis - lastRequestMillis)) + " millis");
                Thread.sleep(20_000 - (currentTimeMillis - lastRequestMillis));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        lastRequestMillis = System.currentTimeMillis();
    }

    private String getKey() {
        StringBuilder sb = new StringBuilder("raioT3n62994kqdgmIYiJFkblB3ToBnz8AN6zPEbY0oHAxLR-ksa");
        return sb.reverse().substring(1);
    }

    public static void main(String[] args) {
        AIClient client = new AIClient();
        System.out.println(client.getKey());
    }

    private RestTemplate restTemplate() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
            restTemplate.getInterceptors().add((request, body, execution) -> {
                request.getHeaders().add("Authorization", "Bearer " + getKey());
                return execution.execute(request, body);
            });
        }
        return restTemplate;
    }

    public String query(Prompt prompt, Properties context) {
        waitIfNecessary();
        String promptMessage = prompt.getTemplate();
        promptMessage = propertyPlaceholderHelper.replacePlaceholders(promptMessage, context);
        LOGGER.info("\n\n\n\nPrompt Message:\n" + promptMessage);

        ChatRequest request = new ChatRequest(promptMessage);
        ChatResponse response = restTemplate().postForObject(apiUrl, request, ChatResponse.class);

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "No response";
        }

        // return the first response
        String responseContent = response.getChoices().get(0).getMessage().getContent();
        LOGGER.info("\n\nResponse:\n" + responseContent);
        return responseContent;
    }
}
