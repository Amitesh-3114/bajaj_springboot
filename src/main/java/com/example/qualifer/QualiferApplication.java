package com.example.qualifer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class QualiferApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(QualiferApplication.class, args);
	}

	@Override
	public void run(String... args) {
		RestTemplate restTemplate = new RestTemplate();

		// API URL
		String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

		// Request body
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("name", "Amitesh Bhaskar");
		requestBody.put("regNo", "1RF22CS013");
		requestBody.put("email", "amiteshbhaskar1@gmail.com");

		// Headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Combine body + headers
		HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

		try {
			// Send POST request and get response as Map
			ResponseEntity<Map> response =
					restTemplate.postForEntity(url, entity, Map.class);

			if (response.getBody() != null) {
				String webhook = (String) response.getBody().get("webhook");
				String accessToken = (String) response.getBody().get("accessToken");

				System.out.println("✅ Webhook: " + webhook);
				System.out.println("✅ Access Token: " + accessToken);
			} else {
				System.out.println("❌ No response body received!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
