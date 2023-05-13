package tfip.akimori.server.services;

import java.time.Instant;
import java.util.Date;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tfip.akimori.server.repositories.MongoLoggingRepository;

@Service
public class MongoLoggingService {
    @Autowired
    private MongoLoggingRepository mongoRepo;

    public void logUserActivity(String email, String activity) {
        Document doc = new Document();
        doc.put("email", email);
        doc.put("activity", activity);
        mongoRepo.insertUserActivity(doc);
    }

    public void logInstrumentInsertion(
            String email,
            String instrument_id,
            String instrument_type,
            String serial_number) {
        Document doc = new Document();
        doc.put("email", email);
        doc.put("activity", "insert");
        doc.put("instrument_id", instrument_id);
        doc.put("instrument_type", instrument_type);
        doc.put("serial_number", serial_number);
        mongoRepo.insertUserActivity(doc);
    }

}
