package net.engineeringdigest.journalApp.api.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VoiceResponse {
    private List<Voice> voices;

    @Getter
    @Setter
    public static class Voice {
        private String voice_id;
        private String name;
    }
}
