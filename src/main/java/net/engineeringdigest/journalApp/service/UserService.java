package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {
    @Autowired
    private UserRepository uer;

    private final static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public void saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        uer.save(user);
    }

    public void saveAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER", "ADMIN"));
        uer.save(user);
    }

    public void saveEntry(User user) {
        uer.save(user);
    }

    public List<User> getAll() {
        return uer.findAll();
    }

    public Optional<User> findJournalById(ObjectId id) {
        return uer.findById(id);
    }

    public void deleteById(ObjectId id) {
        uer.deleteById(id);
    }

    public User findByUserName(String username) {
        return uer.findByUsername(username);
    }
}

//
//the logic is like
//        controller calls service , and service calls repository