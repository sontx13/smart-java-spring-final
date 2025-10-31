package vn.project.smart.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.*;

@RestController
@RequestMapping("/api/v1/proxy")
public class ProxyController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/fetch")
    public ResponseEntity<?> proxyGet(@RequestParam("url") String url) {
        try {
            String encodedUrl = encodeQueryUrl(url);

            HttpHeaders headers = new HttpHeaders();
            headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            headers.add("Accept", "application/json, text/javascript, */*; q=0.01");
            headers.add("X-Requested-With", "XMLHttpRequest");
            headers.add("Referer", "https://bacninh.gov.vn/chinh-tri");
            headers.add("Cookie", "COOKIE_SUPPORT=true; GUEST_LANGUAGE_ID=vi_VN");

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    encodedUrl,
                    HttpMethod.GET,
                    requestEntity,
                    String.class);

            String body = response.getBody();
            if (body == null || body.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("error", "Empty body"));
            }

            // 🔍 Tìm JSON trong đoạn script
            Pattern pattern = Pattern.compile("data\\s*=\\s*'(\\{.*?\\})';", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(body);

            if (matcher.find()) {
                String json = matcher.group(1)
                        .replace("\\u003d", "=")
                        .replace("\\u0026", "&")
                        .replace("\\u003c", "<")
                        .replace("\\u003e", ">");
                return ResponseEntity.ok(json);
            }

            return ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of("error", "Không tìm thấy JSON trong response"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    private String encodeQueryUrl(String url) {
        int idx = url.indexOf('?');
        if (idx == -1)
            return url;
        String base = url.substring(0, idx);
        String query = url.substring(idx + 1);
        return base + "?" + UriUtils.encodeQuery(query, StandardCharsets.UTF_8);
    }
}
