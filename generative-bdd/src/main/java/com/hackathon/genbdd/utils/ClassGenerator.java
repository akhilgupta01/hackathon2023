package com.hackathon.genbdd.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ClassGenerator {

    public void generateJavaClass(String filePath, String jsonSpec) {
        ClassSpecification spec = ClassSpecification.fromJson(jsonSpec);
        StringBuilder sb = new StringBuilder();

        // Add class imports
        for (String classImport : spec.getClassImports()) {
            sb.append(classImport).append("\n");
        }
        sb.append("\n");

        // Add class body
        sb.append(spec.getClassBody()).append("\n");

        saveToFile(filePath + "//" + spec.getClassName() + ".java", sb.toString());
    }

    private void saveToFile(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
            System.out.println("Generated Java class saved to: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Data
    static class ClassSpecification {
        private String className;
        private List<String> classImports;
        private String classBody;

        @SneakyThrows
        public static ClassSpecification fromJson(String json) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ClassSpecification classSpecification = objectMapper.readValue(json, ClassSpecification.class);
            return classSpecification;
        }
    }
}

