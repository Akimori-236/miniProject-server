package tfip.akimori.server.services;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.JsonObject;
import tfip.akimori.server.models.Instrument;
import tfip.akimori.server.repositories.InstrumentRepository;
import tfip.akimori.server.utils.MyUtils;

@Service
public class InstrumentService {

    @Autowired
    private InstrumentRepository instruRepo;
    @Autowired
    private JwtService jwtSvc;

    public List<JsonObject> getBorrowedByJWT(String jwt) {
        // get email from JWT
        String email = jwtSvc.extractUsername(jwt);
        // System.out.println(email);

        List<Instrument> instrumentList = instruRepo.getBorrowedByEmail(email);
        List<JsonObject> jList = new LinkedList<>();
        for (Instrument i : instrumentList) {
            jList.add(MyUtils.instrumentToJOB(i).build());
        }
        return jList;
    }

    // how to loan out to myself?
}
