package tfip.akimori.server.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import tfip.akimori.server.exceptions.UnauthorizedException;
import tfip.akimori.server.repositories.StoreRepository;

@Service
public class QrService {

    private static final String ANGULAR_BORROW_URL = "localhost:4200/#/borrow/";
    private static final String GOQR_URL = "https://api.qrserver.com/v1/create-qr-code/";

    @Autowired
    private JwtService jwtSvc;
    @Autowired
    private StoreRepository storeRepo;

    public ResponseEntity<byte[]> getQRResponse(String URLString) {
        RestTemplate template = new RestTemplate();
        // SET Headers
        final HttpHeaders headers = new HttpHeaders();
        // String apiKey = System.getenv("${API_KEY}");
        // headers.set("data", apiKey);
        // headers.set("hosthost", "host-url");

        // GET request creation with headers
        final HttpEntity<String> entity = new HttpEntity<String>(headers);
        // SEND GET REQUEST
        ResponseEntity<byte[]> response = template.exchange(URLString, HttpMethod.GET, entity, byte[].class);
        return response;
    }

    public Optional<Resource> getLoanQR(String instrumentID, String storeID, String jwt) throws UnauthorizedException {
        // get email from JWT
        String email = jwtSvc.extractUsername(jwt);
        // check manager clearance
        // verify store manager
        if (storeRepo.isManagerOfStore(email, storeID)) {
            String loanURL = ANGULAR_BORROW_URL + instrumentID; // TODO: LINK BACK TO ANGULAR ROUTING
            // access external api
            ResponseEntity<byte[]> response = getQRResponse(buildURL(loanURL));
            // get body from GET response
            byte[] imageBytes = response.getBody();
            if (imageBytes != null) {
                Resource resource = new ByteArrayResource(imageBytes);
                return Optional.of(resource);
            } else {
                return Optional.empty();
            }
        } else {
            // not a manager of the store
            throw new UnauthorizedException("Not a manager of store");
        }
    }

    private String buildURL(String data) {
        String url = UriComponentsBuilder.fromUriString(GOQR_URL)
                .queryParam("data", data)
                .toUriString();
        // System.out.println(url);
        return url;
    }

    private String buildURL(String data, Integer pixelSize) throws Exception {
        if (pixelSize <= 1000) {
            return UriComponentsBuilder.fromUriString(GOQR_URL)
                    .queryParam("data", data)
                    .queryParam("size", pixelSize.toString() + "x" + pixelSize.toString())
                    .toUriString();
        } else {
            throw new Exception("Pixel size needs to be 1000 or below");
        }
    }
}
