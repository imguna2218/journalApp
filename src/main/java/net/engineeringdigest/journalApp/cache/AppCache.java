package net.engineeringdigest.journalApp.cache;

import net.engineeringdigest.journalApp.entity.ConfigureJournalAppEntity;
import net.engineeringdigest.journalApp.repository.ConfigureJournalAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public Map<String, String> APP_CACHE = new HashMap<>();


    @Autowired
    private ConfigureJournalAppRepository cjr;

    @PostConstruct
    public void init() {
        List<ConfigureJournalAppEntity> all = cjr.findAll();

        for(ConfigureJournalAppEntity ce : all) {
            APP_CACHE.put(ce.getKey(), ce.getValue());
        }
    }
}
