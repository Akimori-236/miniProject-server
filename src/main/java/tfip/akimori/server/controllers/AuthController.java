package tfip.akimori.server.controllers;

import org.glassfish.json.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import jakarta.json.JsonObject;
import tfip.akimori.server.exceptions.DuplicateEmailException;
import tfip.akimori.server.services.AuthService;

@RestController
@CrossOrigin(origins = "*") // DANGEROUS
@RequestMapping(path = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Autowired
    private AuthService authSvc;

    // REGISTRATION
    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@RequestBody String request) {
        // read JSON
        JsonObject jsonRequest = JsonUtil.toJson(request).asJsonObject();
        // System.out.println("Register Request >>>>> " + jsonRequest);
        JsonObject jwt;
        try {
            // save new user & get jwt
            jwt = authSvc.register(jsonRequest);
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("\"error\": \"%s\"".formatted(e.getMessage()));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jwt.toString());
    }

    // LOG IN
    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody String request) {
        // read JSON
        JsonObject jsonRequest = JsonUtil.toJson(request).asJsonObject();
        JsonObject jwt;
        jwt = authSvc.login(jsonRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jwt.toString());
    }

    @PostMapping(path = "/google")
    public ResponseEntity<String> getCurrentUser(String oauthToken) {

        System.out.println(">>>>>>>>>>" + oauthToken);
        // oauthToken.isAuthenticated(); // Boolean
        // oauthToken.getPrincipal().getAttributes().get("email"); // Map<string,object>
        // oauthToken.getCredentials();
        // oauthToken.getAuthorizedClientRegistrationId(); // string

        // how to redirect back to angular
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body("");
    }
}
