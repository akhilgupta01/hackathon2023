package com.hackathon.genbdd.prompts.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hackathon.genbdd.aiclient.AIClient;
import com.hackathon.genbdd.prompts.PromptRepository;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;


@Data
public class ReaderHandler implements PromptResponseHandler {
    private PromptRepository promptRepository;

    public ReaderHandler(PromptRepository promptRepository) {
        this.promptRepository = promptRepository;
    }


    public void handle(Properties context) {
        if (StringUtils.isNotBlank(String.valueOf(context.get("input-data-source-type").toString()))) {
            promptRepository.getByType("ReaderClassGeneration").ifPresent(prompt -> {
                AIClient client = AIClient.getInstance();
                String response = client.query(prompt, context);

                //Parse the response
                JsonElement jsonelement = JsonParser.parseString(response);
                JsonObject jsonObject = jsonelement.getAsJsonObject();
                context.put("reader", jsonObject.get("classBody").toString());
            });
        }
    }
}
