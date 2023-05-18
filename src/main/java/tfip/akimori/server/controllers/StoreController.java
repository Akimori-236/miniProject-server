package tfip.akimori.server.controllers;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.mail.MessagingException;
import tfip.akimori.server.services.EmailSenderService;
import tfip.akimori.server.services.InstrumentService;
import tfip.akimori.server.services.StoreService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/data", produces = MediaType.APPLICATION_JSON_VALUE)
public class StoreController {

    @Autowired
    private InstrumentService instruSvc;
    @Autowired
    private StoreService storeSvc;
    @Autowired
    private EmailSenderService emailSvc;

    @GetMapping(path = "/borrowed")
    public ResponseEntity<String> getBorrowedByJWT(@RequestHeader(name = "Authorization") String token) {
        System.out.println("GETTING BORROWED");
        String jwt = token.substring(7, token.length());

        List<JsonObject> jList = instruSvc.getBorrowedByJWT(jwt);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jList.toString());
    }

    @PostMapping(path = "/store/create")
    public ResponseEntity<String> createStore(@RequestHeader(name = "Authorization") String token,
            @RequestParam String storename) {
        System.out.println("CREATING STORE: " + storename);
        if (storename == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("\"error\": \"Please include 'storeName' query parameter\"");
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

    @GetMapping(path = "/store")
    public ResponseEntity<String> getStoresByJWT(@RequestHeader(name = "Authorization") String token) {
        System.out.println("GETTING STORES");
        String jwt = token.substring(7, token.length());

        List<JsonObject> storeList = storeSvc.getManagedStores(jwt);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(storeList.toString());
    }

    @GetMapping(path = "/store/{storeID}")
    public ResponseEntity<String> getStoreDetails(@RequestHeader(name = "Authorization") String token,
            @PathVariable String storeID) {
        System.out.println("GETTING DETAILS OF STORE: " + storeID);
        String jwt = token.substring(7, token.length());
        // GET DATA
        JsonObject data = storeSvc.getStoreDetails(jwt, storeID);
        if (data == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("\"error\":\"Not a manager of the store\"");
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(data.toString());
        }
    }

    @PostMapping(path = "/store/{storeID}/addinstrument")
    public ResponseEntity<String> addInstrument(@RequestHeader(name = "Authorization") String token,
            @PathVariable String storeID,
            @RequestBody String dataString) {
        System.out.println(dataString);
        String jwt = token.substring(7, token.length());
        System.out.println("ADDING INSTRUMENT TO STORE: " + storeID);
        JsonReader jr = Json.createReader(new StringReader(dataString));
        JsonObject body = jr.readObject().get("body").asJsonObject();

        boolean isInserted = instruSvc.addInstrument(jwt, storeID, body);
        if (isInserted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(path = "/store/{storeID}/invitemanager")
    public ResponseEntity<String> sendEmailInviteManager(@RequestHeader(name = "Authorization") String token,
            @PathVariable String storeID,
            @RequestParam String managerEmail) {
        String jwt = token.substring(7, token.length());
        System.out.println("SENDING EMAIL TO: " + managerEmail);
        try {
            emailSvc.sendManagerInvite(managerEmail, jwt, storeID);
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body("Email invite sent!");
    }

    @GetMapping(path = "/instrument/{id}")
    public ResponseEntity<String> getInstrument(@PathVariable String id) {
        JsonObject jObj = instruSvc.getInstrumentByID(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jObj.toString());
    }

}