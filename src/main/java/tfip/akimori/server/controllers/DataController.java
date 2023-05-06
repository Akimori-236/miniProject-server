package tfip.akimori.server.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import tfip.akimori.server.services.InstrumentService;
import tfip.akimori.server.services.StoreService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/data")
public class DataController {

        @Autowired
        private InstrumentService instruSvc;
        @Autowired
        private StoreService storeSvc;

        @GetMapping(path = "/borrowed")
        public ResponseEntity<String> getBorrowedByJWT(
                        @RequestHeader(name = "Authorization") String token) {

                String jwt = token.substring(7, token.length());

                List<JsonObject> jList = instruSvc.getBorrowedByJWT(jwt);
                return ResponseEntity
                                .status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(jList.toString());
        }

        @GetMapping(path = "/store")
        public ResponseEntity<String> getStoresByJWT(
                        @RequestHeader(name = "Authorization") String token) {
                String jwt = token.substring(7, token.length());

                List<String> storeList = storeSvc.getManagedStores(jwt);
                return ResponseEntity
                                .status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(storeList.toString());
        }

        @PostMapping(path = "/store/create")
        public ResponseEntity<String> createStore(@RequestHeader(name = "Authorization") String token,
                        @RequestParam String storename) {
                System.out.println("TOKEN: " + token);
                System.out.println("CREATING STORE: " + storename);
                if (storename == null) {
                        return ResponseEntity
                                        .status(HttpStatus.BAD_REQUEST)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body("\"message\": \"Please include 'storeName' query parameter\"");
                }
                String jwt = token.substring(7, token.length());

                Boolean isCreated;
                try {
                        isCreated = storeSvc.createStore(jwt, storename);
                } catch (SQLException e) {
                        String response = Json.createObjectBuilder().add("message", "Error creating new store").build()
                                        .toString();
                        return ResponseEntity
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(response);
                }
                // JsonObject response = Json.createObjectBuilder()
                // .add("store_created", isCreated)
                // .build();
                return ResponseEntity
                                .status(HttpStatus.OK)
                                // .contentType(MediaType.APPLICATION_JSON)
                                .body(isCreated.toString());
        }

        @GetMapping(path = "/store/managers")
        public ResponseEntity<String> getStoresManagersByJWT(
                        @RequestHeader(name = "Authorization") String token) {
                String jwt = token.substring(7, token.length());

                List<JsonObject> storeList = storeSvc.getStoreManagers(jwt);
                return ResponseEntity
                                .status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(storeList.toString());
        }
}
