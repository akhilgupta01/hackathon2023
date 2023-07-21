package com.hackathon.genbdd;

import com.hackathon.genbdd.prompts.PromptRepository;
import com.hackathon.genbdd.prompts.handler.*;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;

public class GenerativeBddApplication {
	public static void main(String[] args) {
		PromptRepository promptRepository = PromptRepository.getInstance();

		//Start the context with scenario text
		Properties context = new Properties();
		context.put("scenario-text", readScenario("transaction-aggregation.feature"));

		//Analyze the scenario
		ScenarioAnalysisHandler scenarioAnalysisHandler = new ScenarioAnalysisHandler(promptRepository);
		scenarioAnalysisHandler.handle(context);

		//Generate Model classes
		ModelHandler modelHandler = new ModelHandler(promptRepository);
		modelHandler.handle(context);

		//Reader Class Generation
		ReaderHandler readerHandler = new ReaderHandler(promptRepository);
		readerHandler.handle(context);

		//Generate Transformation
		TransformationHandler transformationHandler = new TransformationHandler(promptRepository);
		transformationHandler.handle(context);

		//Writer Class Generation
		WriterHandler writerHandler = new WriterHandler(promptRepository);
		writerHandler.handle(context);

		PipelineHandler pipelineHandler = new PipelineHandler(promptRepository);
		pipelineHandler.handle(context);

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
