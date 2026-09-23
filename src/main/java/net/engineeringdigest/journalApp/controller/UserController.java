package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.api.response.VoiceResponse;
import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.TextToSpeechEntity;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.ElevenLabsService;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.service.WeatherService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService ues;

    @Autowired
    private UserRepository uer;

    @Autowired
    private WeatherService ws;

    @Autowired
    private ElevenLabsService elevenLabsService;



    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User userInDb = ues.findByUserName(username);

        userInDb.setUsername(user.getUsername());
        userInDb.setPassword(user.getPassword());
        ues.saveNewUser(userInDb);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uer.deleteByUsername(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> greetings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        uer.deleteByUsername(authentication.getName());
        WeatherResponse response = ws.getWeather("Mumbai");
        String greeting = "";
        if(response != null) {
            greeting = ", Welcome. Today Weather feels like " + response.getCurrent().getFeelslike();
        }
        return new ResponseEntity<>("HI " + authentication.getName() + greeting, HttpStatus.OK);
    }


    @GetMapping("/voices")
    public ResponseEntity<VoiceResponse> fetchVoices() {
        VoiceResponse voices = elevenLabsService.getVoices();
        return new ResponseEntity<>(voices, HttpStatus.OK);
    }

    @PostMapping("/tts/{voiceId}")
    public ResponseEntity<byte[]> textToSpeech(@PathVariable String voiceId, @RequestBody TextToSpeechEntity request) {
        byte[] audioData = elevenLabsService.generateSpeech(voiceId, request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("audio/mpeg"));

        return new ResponseEntity<>(audioData, headers, HttpStatus.OK);
    }
}
