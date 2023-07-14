package com.hackathon.genbdd;

import com.hackathon.genbdd.prompts.PromptRepository;
import com.hackathon.genbdd.prompts.model.Prompt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class GenerativeBddApplication {
	public static void main(String[] args) {
		PromptRepository promptRepository = PromptRepository.getInstance();
		for (Prompt prompt : promptRepository.getPrompts()) {
			System.out.println(prompt.getType());
			System.out.println(prompt.getTemplate());
		}
	}

}
