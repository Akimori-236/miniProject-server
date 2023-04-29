package tfip.akimori.server.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import tfip.akimori.server.services.InstrumentService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/data")
public class DataController {

    @Autowired
    private InstrumentService instruSvc;

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

    @PostMapping(path = "/store/create")
    public ResponseEntity<String> createStore(
            @RequestHeader(name = "Authorization") String token, String storeName) {

        String jwt = token.substring(7, token.length());

        Boolean isCreated = instruSvc.createStore(jwt, storeName);
        JsonObject response = Json.createObjectBuilder()
                .add("store_created", isCreated)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toString());
    }

    @GetMapping(path = "/store")
    public ResponseEntity<String> getStoresByJWT(
            @RequestHeader(name = "Authorization") String token) {

        String jwt = token.substring(7, token.length());

        List<JsonObject> jList = instruSvc.getStoresByJWT(jwt);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jList.toString());
    }
}
