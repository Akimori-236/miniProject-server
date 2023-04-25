package tfip.akimori.server.services;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import tfip.akimori.server.models.Instrument;
import tfip.akimori.server.repositories.InstrumentRepository;

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

        // parse instruments into jObjects
        for (Instrument inst : instrumentList) {
            JsonObject jObj = Json.createObjectBuilder()
                    .add("instrument_id", inst.getInstrument_id())
                    .add("brand", inst.getBrand())
                    .add("model", inst.getModel())
                    .add("serial_number", inst.getSerial_number())
                    .add("store_name", inst.getStore_name())
                    .add("firstname", inst.getUser().getFirstname())
                    .add("lastname", inst.getUser().getLastname())
                    .add("email", inst.getUser().getEmail())
                    .build();
            jList.add(jObj);
        }

        return jList;
    }

}
