package tfip.akimori.server.services;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.JsonObject;
import tfip.akimori.server.models.Instrument;
import tfip.akimori.server.repositories.InstrumentRepository;
import tfip.akimori.server.utils.MyUtils;

@Service
public class InstrumentService {
    private static final int ID_LENGTH = 8;

    @Autowired
    private InstrumentRepository instruRepo;
    @Autowired
    private JwtService jwtSvc;
    @Autowired
    private MongoLoggingService logSvc;

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

    public boolean addInstrument(String jwt, String storeID, JsonObject jObj) {
        // get email from JWT
        String email = jwtSvc.extractUsername(jwt);

        Instrument i = Instrument.builder()
                .instrument_id(generateID(ID_LENGTH))
                .instrument_type(jObj.getString("instrument_type"))
                .brand(jObj.getString("brand"))
                .model(jObj.getString("model"))
                .serial_number(jObj.getString("serial_number"))
                .store_id(storeID)
                .isRepairing(false)
                .email(email)
                .build();
        // System.out.println(i);
        logSvc.logInstrumentActivity("insert", i.getStore_id(), email, i.getInstrument_id(), i.getInstrument_type(),
                i.getSerial_number());
        return instruRepo.addInstrument(i);
    }

    // TODO: how to loan out to myself?

    private static String generateID(int length) {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .replace("_", "")
                .substring(0, length);
    }
}
