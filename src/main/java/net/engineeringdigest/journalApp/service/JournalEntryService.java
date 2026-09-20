package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository jer;

    private static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class);

    @Autowired
    private UserService ues;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String username) {
        try {
            User user = ues.findByUserName(username);

            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = jer.save(journalEntry);

            user.getJournalEntries().add(saved);
            ues.saveEntry(user);
        } catch (Exception e) {
            logger.info("Error occured ; {}", e);
            throw new RuntimeException("An error occured while saving the entry. ", e);
        }
    }

    public void saveEntry(JournalEntry journalEntry) {
        jer.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return jer.findAll();
    }

    public Optional<JournalEntry> findJournalById(ObjectId id) {
        return jer.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String username) {
        boolean removed = false;
        try {
            User user = ues.findByUserName(username);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if(removed) {
                jer.deleteById(id);
                ues.saveEntry(user);
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("An Error occured while delting the entry : ", e);
        }
        return removed;
    }

}

//
//the logic is like
//        controller calls service , and service calls repository