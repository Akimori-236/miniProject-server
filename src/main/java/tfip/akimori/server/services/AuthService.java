package tfip.akimori.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
                                .firstname(request.getString("firstname"))
                                .lastname(request.getString("lastname"))
                                .email(request.getString("email"))
                                .password(pwEncoder.encode(request.getString("password")))
                                .role(Role.USER)
                                .build();
                // System.out.println(newUser);
                userRepo.save(newUser);
                // give new user JWT
                return jwtSvc.generateJWT(newUser);
        }

        public JsonObject login(JsonObject request) {
                authManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getString("email"),
                                                request.getString("password")));
                // authenticated
                User user = userRepo.findByEmail(request.getString("email"))
                                .orElseThrow();
                return jwtSvc.generateJWT(user);
        }
}
