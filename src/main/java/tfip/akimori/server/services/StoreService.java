package tfip.akimori.server.services;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import tfip.akimori.server.models.Store;
import tfip.akimori.server.models.User;
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

        int store_id = storeRepo.createStore(store_name);
        boolean isInserted = storeRepo.insertStoreManager(email, store_id);
        return isInserted;
    }

    public List<String> getManagedStores(String jwt) {
        // get email from JWT
        String email = jwtSvc.extractUsername(jwt);
        return storeRepo.getManagedStores(email);
    }

    public List<JsonObject> getStoreManagers(String jwt) {
        // get email from JWT
        String email = jwtSvc.extractUsername(jwt);
        List<Store> sList = storeRepo.getStoreManagers(email);

        return sortManager(sList);
    }

    private static JsonObject storeToJson(Store s) {
        return Json.createObjectBuilder()
                .add("store_name", s.getStore_name())
                .add("email", s.getUser().getEmail())
                .add("givenname", s.getUser().getGivenname())
                .add("familyname", s.getUser().getFamilyname())
                .build();
    }

    private static List<JsonObject> sortManager(List<Store> storeList) {
        List<JsonObject> jList = new LinkedList<>();
        Map<String, List<User>> storeMap = new HashMap<>();
        for (Store s : storeList) {
            if (!storeMap.containsKey(s.getStore_name())) {
                storeMap.put(s.getStore_name(), new LinkedList<>());
                storeMap.get(s.getStore_name()).add(s.getUser());
            } else {
                storeMap.get(s.getStore_name()).add(s.getUser());
            }
        }
        for (String s : storeMap.keySet()) {
            JsonObjectBuilder job = Json.createObjectBuilder();
            JsonArrayBuilder jab = Json.createArrayBuilder();
            for (User u : storeMap.get(s)) {
                jab.add(Json.createObjectBuilder()
                        .add("email", u.getEmail())
                        .add("givenname", u.getGivenname())
                        .add("familyname", u.getFamilyname()));
            }
            job.add("storeName", s);
            job.add("managers", jab);
            jList.add(job.build());
        }
        System.out.println(storeMap);
        return jList;
    }
}
