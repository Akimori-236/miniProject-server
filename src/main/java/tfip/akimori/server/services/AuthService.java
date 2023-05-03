package tfip.akimori.server.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import tfip.akimori.server.exceptions.DuplicateEmailException;
import tfip.akimori.server.models.Role;
import tfip.akimori.server.models.User;
import tfip.akimori.server.repositories.UserRepository;

@Service
public class AuthService {

        @Autowired
        private UserRepository userRepo;
        @Autowired
        private JwtService jwtSvc;
        @Autowired
        private PasswordEncoder pwEncoder;
        @Autowired
        private AuthenticationManager authManager;

        public JsonObject register(JsonObject request) throws DuplicateEmailException {
                User newUser = User.builder()
                                .givenname(request.getString("givenname"))
                                .familyname(request.getString("familyname"))
                                .email(request.getString("email"))
                                .password(pwEncoder.encode(request.getString("password")))
                                .role(Role.USER)
                                .isGoogleLogin(request.getBoolean("isGoogleLogin"))
                                .build();
                // System.out.println(newUser);
                userRepo.insertUser(newUser);
                // give new user JWT
                return jwtSvc.generateJWT(newUser);
        }

        public JsonObject login(JsonObject request) {
                authManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getString("email"),
                                                request.getString("password")));
                // authenticated
                User user = userRepo.getUserByEmail(request.getString("email"))
                                .orElseThrow();
                return jwtSvc.generateJWT(user);
        }

        public JsonObject registerGoogleUser(Payload payload) throws DuplicateEmailException {
                System.out.println("REGISTERING GOOGLE USER");
                JsonObject googleUser = Json.createObjectBuilder()
                                .add("googleUserId", payload.getSubject())
                                .add("email", payload.getEmail())
                                .add("emailVerified", Boolean.valueOf(payload.getEmailVerified()))
                                .add("name", (String) payload.get("name"))
                                .add("picture", (String) payload.get("picture"))
                                .add("familyname", (String) payload.get("family_name"))
                                .add("givenname", (String) payload.get("given_name"))
                                .add("password", this.generateRandomPassword())
                                .add("isGoogleLogin", true)
                                .build();
                System.out.println(googleUser);
                return this.register(googleUser);
        }

        public JsonObject loginGoogleUser(Payload payload) {
                System.out.println("LOGGING IN GOOGLE USER");
                JsonObject googleUser = Json.createObjectBuilder()
                                .add("googleUserId", payload.getSubject())
                                .add("email", payload.getEmail())
                                .add("emailVerified", Boolean.valueOf(payload.getEmailVerified()))
                                .add("name", (String) payload.get("name"))
                                .add("picture", (String) payload.get("picture"))
                                .add("familyname", (String) payload.get("family_name"))
                                .add("givenname", (String) payload.get("given_name"))
                                .build();
                System.out.println(googleUser);
                // authenticated by google already
                User user = userRepo.getUserByEmail(googleUser.getString("email"))
                                .orElseThrow();
                return jwtSvc.generateJWT(user);
        }

        private static String generateRandomPassword() {
                String randPw = UUID.randomUUID()
                                .toString()
                                .replace("-", "")
                                .replace("_", "")
                                .substring(0, 15);
                System.out.println("Random password generated: " + randPw);
                return randPw;
        }
}
