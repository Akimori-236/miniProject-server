package tfip.akimori.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tfip.akimori.server.repositories.StoreRepository;

@Service
public class QrService {

    private static final String GOQR_URL = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=Example";

    @Autowired
    private JwtService jwtSvc;
    @Autowired
    private StoreRepository storeRepo;

    public String getLoanQR(String instrumentID, String storeID, String jwt) {
        // get email from JWT
        String email = jwtSvc.extractUsername(jwt);
        // check manager clearance
        // verify store manager
        if (storeRepo.isManagerOfStore(email, storeID)) {
            //access external api
        } else {
            // not a manager of the store
            return null;
        }
    }
}
