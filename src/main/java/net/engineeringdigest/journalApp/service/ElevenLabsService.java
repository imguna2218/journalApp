package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.api.response.VoiceResponse;
import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.entity.TextToSpeechEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Response;
import java.util.Collections;

@Component
public class ElevenLabsService {

    @Value("${elevenlabs.api}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;


    public VoiceResponse getVoices() {
        VoiceResponse cachedVoices = redisService.get("voices", VoiceResponse.class);

        if (cachedVoices != null) {
            // Cache hit. Return the object directly.
            return cachedVoices;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("xi-api-key", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<VoiceResponse> response = restTemplate.exchange(
                appCache.APP_CACHE.get("elevenlabs_api")+"/voices",
                HttpMethod.GET,
                entity,
                VoiceResponse.class
        );

        VoiceResponse apiVoices = response.getBody();

        if(apiVoices != null) {
            redisService.set("voices", apiVoices, 300l);
        }

        return apiVoices;
    }

    public byte[] generateSpeech(String voiceId, TextToSpeechEntity ttsRequest) {
        byte[] cachedResponse = redisService.get(ttsRequest.getText(), byte[].class);
        if (cachedResponse != null && cachedResponse.length != 0) {
            return cachedResponse;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("xi-api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.valueOf("audio/mpeg")));

        HttpEntity<TextToSpeechEntity> entity = new HttpEntity<>(ttsRequest, headers);
        String url =  appCache.APP_CACHE.get("elevenlabs_api") + "/text-to-speech/" + voiceId;

        // Notice byte[].class - we are catching binary audio data, not JSON
        ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                byte[].class
        );

        byte[] apiResponse = response.getBody();

        if (apiResponse != null && apiResponse.length != 0) {
            redisService.set(ttsRequest.getText(), apiResponse, 300L);
        }

        return response.getBody();
    }
}
