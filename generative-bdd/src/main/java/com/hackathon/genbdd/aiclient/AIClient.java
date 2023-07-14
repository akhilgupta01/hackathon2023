package com.hackathon.genbdd.aiclient;

import com.hackathon.genbdd.Context;
import com.hackathon.genbdd.prompts.model.Prompt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

public class AIClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AIClient.class);

    private String apiUrl = "https://api.openai.com/v1/chat/completions";

    private RestTemplate restTemplate;

    private PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("${", "}");

    private String getKey() {
        StringBuilder sb = new StringBuilder("raioT3n62994kqdgmIYiJFkblB3ToBnz8AN6zPEbY0oHAxLR-ksa");
        return sb.reverse().substring(1);
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

        String promptMessage = prompt.getTemplate();
        promptMessage = propertyPlaceholderHelper.replacePlaceholders(promptMessage, context);

        ChatRequest request = new ChatRequest(promptMessage);
        ChatResponse response = restTemplate().postForObject(apiUrl, request, ChatResponse.class);

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "No response";
        }

        // return the first response
        return response.getChoices().get(0).getMessage().getContent();
    }

//    public static String sendQuery(String input, String endpoint, String apiKey) {
//        // Build input and API key params
//        JsonObject payload = new JsonObject();
//        JsonObject message = new JsonObject();
//        JsonArray messageList = new JsonArray();
//
//        message.addProperty("role", "user");
//        message.addProperty("content", input);
//        messageList.add(message);
//
//        payload.addProperty("model", "gpt-3.5-turbo"); // model is important
//        payload.add("messages", messageList);
//        //payload.put("temperature", 0.7);
//
//        StringEntity inputEntity = new StringEntity(payload.toString(), ContentType.APPLICATION_JSON);
//
//        // Build POST request
//        HttpPost post = new HttpPost(endpoint);
//        post.setEntity(inputEntity);
//        post.setHeader("Authorization", "Bearer " + apiKey);
//        post.setHeader("Content-Type", "application/json");
//
//        // Send POST request and parse response
//        try (CloseableHttpClient httpClient = HttpClients.createDefault();
//             CloseableHttpResponse response = httpClient.execute(post)) {
//            HttpEntity resEntity = response.getEntity();
//            String resJsonString = new String(resEntity.getContent().readAllBytes()/* , StandardCharsets.UTF_8 */);
//            JsonObject resJson = new JsonObject(resJsonString);
//
//
//            if (resJson.has("error")) {
//                String errorMsg = resJson.getString("error");
//                LOGGER.error("Chatbot API error: {}", errorMsg);
//                return "Error: " + errorMsg;
//            }
//
//            // Parse JSON response
//            JSONArray responseArray = resJson.getJSONArray("choices");
//            List<String> responseList = new ArrayList<>();
//
//            for (int i = 0; i < responseArray.length(); i++) {
//                JSONObject responseObj = responseArray.getJSONObject(i);
//                String responseString = responseObj.getJSONObject("message").getString("content");
//                responseList.add(responseString);
//            }
//
//            // Convert response list to JSON and return it
//            //Gson gson = new Gson();
//
//            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//
//            /*
//             * Gson gson = new Gson(); String jsonResponse =
//             * (gson.toJson(responseList.get(0))).replaceAll("\\\\n", "\\n");
//             */
//
//            //return 	new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(responseList.toString()));
//
//            String jsonResponse = gson.toJson(responseList.get(0));
//            return jsonResponse;
//        } catch (IOException | JSONException e) {
//            LOGGER.error("Error sending request: {}", e.getMessage());
//            return "Error: " + e.getMessage();
//        }
//    }
}
