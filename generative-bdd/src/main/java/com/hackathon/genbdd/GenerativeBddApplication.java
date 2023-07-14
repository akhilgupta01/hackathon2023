package com.hackathon.genbdd;

import com.hackathon.genbdd.aiclient.AIClient;
import com.hackathon.genbdd.prompts.PromptRepository;
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
import java.util.Map;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;

public class GenerativeBddApplication {
	public static void main(String[] args) {
		PromptRepository promptRepository = PromptRepository.getInstance();
		Prompt scenarioAnalysisPrompt = promptRepository.getByType("ScenarioAnalysis").get();

		Properties context = new Properties();
		context.put("scenario_text", readScenario("transaction-aggregation.feature"));

		AIClient client = new AIClient();
		String response = client.query(scenarioAnalysisPrompt, context);
		System.out.println(response);
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

}
