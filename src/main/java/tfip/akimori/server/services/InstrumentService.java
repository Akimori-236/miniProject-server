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
        return instrumentListToJsonObjectList(instrumentList);
    }

    public Boolean createStore(String jwt, String storeName) {
        // what problems if stores have same name?

        return null;
    }

    private JsonObject instrumentToJsonObject(Instrument i) {
        JsonObject jObj = Json.createObjectBuilder()
                .add("instrument_id", i.getInstrument_id())
                .add("type", i.getType())
                .add("brand", i.getBrand())
                .add("model", i.getModel())
                .add("serial_number", i.getSerial_number())
                .add("store_name", i.getStore_name())
                .add("givenname", i.getUser().getGivenname())
                .add("familyname", i.getUser().getFamilyname())
                .add("email", i.getUser().getEmail())
                .build();
        return jObj;
    }

    private List<JsonObject> instrumentListToJsonObjectList(List<Instrument> instrumentList) {
        List<JsonObject> jList = new LinkedList<>();
        for (Instrument ins : instrumentList) {
            jList.add(instrumentToJsonObject(ins));
        }
        return jList;
    }


    // how to loan out to myself
}
