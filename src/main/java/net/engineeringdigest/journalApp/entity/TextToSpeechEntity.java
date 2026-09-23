package net.engineeringdigest.journalApp.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextToSpeechEntity {
    private String text;
    private String model_id = "eleven_multilingual_v2"; // Hardcode a default model or it will fail
}
