package com.hackathon.genbdd;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hackathon.genbdd.aiclient.AIClient;
import com.hackathon.genbdd.prompts.PromptRepository;
import com.hackathon.genbdd.prompts.handler.ModelHandler;
import com.hackathon.genbdd.prompts.handler.TransformationHandler;
import com.hackathon.genbdd.prompts.model.Prompt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;

public class GenerativeBddApplication {
	public static void main(String[] args) {
		AIClient client = new AIClient();
		PromptRepository promptRepository = PromptRepository.getInstance();

		//Start the context with scenario text
		Properties context = new Properties();
		context.put("scenario-text", readScenario("transaction-aggregation.feature"));

		//Analyze the scenario
		Prompt scenarioAnalysisPrompt = promptRepository.getByType("ScenarioAnalysis").get();
		String response = client.query(scenarioAnalysisPrompt, context);

		//Parse the response
		JsonElement jsonelement = JsonParser.parseString(response);
		JsonObject jsonObject = jsonelement.getAsJsonObject();
		context.put("scenario-analysis", jsonObject);

		//Generate Model classes
		ModelHandler modelHandler = new ModelHandler(promptRepository);
		modelHandler.handle(context);

		//Generate Transformation
		TransformationHandler transformationHandler = new TransformationHandler(promptRepository);
		transformationHandler.handle(context);



		//Prompt prompt = promptRepository.getByType("ReaderMethodGeneration").get();
			//String resp = client.query(prompt, context);
			//System.out.println(resp);

			//TODO: generateModelClasses(context);
			//TODO: generateReader(context);
			//TODO: generateWriter(context);
			//TODO: generateTransformationClass(context);
		//}
	}

	private static void updateContext(Properties context, String response) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode responseJson = objectMapper.readTree(response);
			context.putAll(transformJsonToMap(responseJson, ""));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String readScenario(String fileName) {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("classpath:" + fileName);
		try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
			return FileCopyUtils.copyToString(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static Map<String, String> transformJsonToMap(JsonNode node, String prefix){

		Map<String,String> jsonMap = new HashMap<>();

		if(node.isArray()) {
			jsonMap.put(prefix,node.toPrettyString());
		}else if(node.isObject()){
			Iterator<String> fieldNames = node.fieldNames();
			String curPrefixWithDot = (prefix==null || prefix.trim().length()==0) ? "" : prefix+".";
			//list all keys and values
			while(fieldNames.hasNext()){
				String fieldName = fieldNames.next();
				JsonNode fieldValue = node.get(fieldName);
				jsonMap.putAll(transformJsonToMap(fieldValue, curPrefixWithDot+fieldName));
			}
		}else {
			//basic type
			jsonMap.put(prefix,node.asText());
		}

		return jsonMap;
	}
}
