package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.ConfigureJournalAppEntity;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface ConfigureJournalAppRepository extends MongoRepository<ConfigureJournalAppEntity, ObjectId> {


}
