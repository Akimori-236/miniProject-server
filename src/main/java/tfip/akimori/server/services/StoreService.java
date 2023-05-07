package tfip.akimori.server.services;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import tfip.akimori.server.models.Store;
import tfip.akimori.server.repositories.StoreRepository;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepo;
    @Autowired
    private JwtService jwtSvc;

    @Transactional(rollbackFor = SQLException.class)
    public boolean createStore(String jwt, String store_name) throws SQLException {
        // get email from JWT
        String email = jwtSvc.extractUsername(jwt);
        // Generate storeID
        String storeID = UUID.randomUUID().toString().substring(8);
        // rollback these >
        storeRepo.createStore(storeID, store_name, email);
        boolean isInserted = storeRepo.insertStoreManager(email, storeID);
        return isInserted;
    }

    public List<JsonObject> getManagedStores(String jwt) {
        // get email from JWT
        String email = jwtSvc.extractUsername(jwt);
        List<Store> storeList = storeRepo.getManagedStores(email);
        List<JsonObject> outputList = new LinkedList<>();
        for (Store s : storeList) {
            outputList.add(storeToJson(s));
        }
        return outputList;
    }

    private static JsonObject storeToJson(Store s) {
        return Json.createObjectBuilder()
                .add("store_id", s.getStore_id())
                .add("store_name", s.getStore_name())
                .build();
    }

    // private static List<JsonObject> sortManager(List<Store> storeList) {
    // // sorting
    // Map<Integer, List<User>> storeMap = new HashMap<>();
    // for (Store s : storeList) {
    // if (!storeMap.containsKey(s.getStore_id())) {
    // storeMap.put(s.getStore_id(), new LinkedList<>());
    // storeMap.get(s.getStore_id()).add(s.getUser());
    // } else {
    // storeMap.get(s.getStore_id()).add(s.getUser());
    // }
    // }
    // List<JsonObject> jList = new LinkedList<>();
    // for (Integer storeid : storeMap.keySet()) {
    // JsonObjectBuilder job = Json.createObjectBuilder();
    // JsonArrayBuilder jab = Json.createArrayBuilder();
    // for (User u : storeMap.get(storeid)) {
    // jab.add(Json.createObjectBuilder()
    // .add("email", u.getEmail())
    // .add("givenname", u.getGivenname())
    // .add("familyname", u.getFamilyname()));
    // }
    // job.add("store_name", storeid);
    // job.add("managers", jab);
    // jList.add(job.build());
    // }
    // System.out.println(storeMap);
    // return jList;
    // }
}
