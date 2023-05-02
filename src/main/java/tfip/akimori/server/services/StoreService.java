package tfip.akimori.server.services;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tfip.akimori.server.repositories.StoreRepository;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepo;

    @Transactional(rollbackFor = SQLException.class)
    public boolean insertStore(String email, String store_name) throws SQLException {
        int store_id = storeRepo.insertStore(store_name);
        boolean isInserted = storeRepo.insertStoreManager(email, store_id);
        return isInserted;
    }

}
