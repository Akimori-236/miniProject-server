package tfip.akimori.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tfip.akimori.server.services.ProfileService;

@RestController
@RequestMapping(path = "/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileSvc;

    @GetMapping
    public ResponseEntity<String> getProfile(@RequestHeader(name = "Authorization") String token) {
        String jwt = token.substring(7, token.length());
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(profileSvc.getProfile(jwt).toString());
    }
}
