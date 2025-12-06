package com.example.demo.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OllamaService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> analyzeMatch(String resume, String jobDesc) {
        String prompt = """
                You are a senior technical recruiter.
                1. Extract the 15 most important keywords/skills from this job description (return as JSON array).
                2. Count how many appear in the resume.
                3. Give a match percentage.
                4. Suggest ONE rewritten bullet from the resume that includes 2 missing keywords.
                5. Give a 3-sentence fit summary.

                Job Description:
                %s

                Resume:
                %s

                Respond in JSON only with keys: keywords, found, missing, percentage, rewritten_bullet, summary
                """.formatted(jobDesc, resume.substring(0, Math.min(resume.length(), 12000)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "model", "gemma3:1b-it-qat",
                "prompt", prompt,
                "stream", false
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "http://localhost:11434/api/generate", request, Map.class);
        String raw = (String) response.getBody().get("response");
        return parseOllamaJson(raw);
    }

    private Map<String, Object> parseOllamaJson(String text) {
        try {
            String cleaned = text
                    .replaceAll("(?i)```json", "")
                    .replaceAll("```", "")
                    .trim();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(cleaned, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("raw", text);
            map.put("error", "Failed to parse LLM output as JSON: " + e.getMessage());
            return map;
        }
    }
}