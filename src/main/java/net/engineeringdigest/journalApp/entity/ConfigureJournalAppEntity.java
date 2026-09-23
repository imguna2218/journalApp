package net.engineeringdigest.journalApp.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "configure_journal")
@Data
public class ConfigureJournalAppEntity {
    private String key;
    private String value;
}
