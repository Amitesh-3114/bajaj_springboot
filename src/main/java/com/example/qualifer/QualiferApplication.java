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
		requestBody.put("email", "rvit22bcs065.rvitm@rvei.edu.in");

		// Headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

		try {
			ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

			if (response.getBody() != null) {
				// Extract from Step 1 response
				String webhook = (String) response.getBody().get("webhook"); // ✅ dynamic URL
				String accessToken = (String) response.getBody().get("accessToken");

// SQL query
				String finalQuery =
						"SELECT p.AMOUNT AS SALARY, " +
								"CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, " +
								"TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, " +
								"d.DEPARTMENT_NAME " +
								"FROM PAYMENTS p " +
								"JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
								"JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
								"WHERE DAY(p.PAYMENT_TIME) <> 1 " +
								"ORDER BY p.AMOUNT DESC " +
								"LIMIT 1;";

// Headers (⚠️ no "Bearer ", just raw token as assignment shows)
				HttpHeaders submitHeaders = new HttpHeaders();
				submitHeaders.set("Authorization", accessToken);
				submitHeaders.setContentType(MediaType.APPLICATION_JSON);

// Body
				Map<String, String> submitBody = new HashMap<>();
				submitBody.put("finalQuery", finalQuery);

				HttpEntity<Map<String, String>> submitEntity = new HttpEntity<>(submitBody, submitHeaders);

// ✅ Post to the dynamic webhook
				ResponseEntity<String> submitResponse =
						restTemplate.postForEntity(webhook, submitEntity, String.class);

				System.out.println("✅ Submission Response: " + submitResponse.getBody());

			} else {
				System.out.println("❌ Failed to get accessToken.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
