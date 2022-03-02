package pjj.ibf2021project.server.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import pjj.ibf2021project.server.repositories.AppRepository;

@Service
public class DatabaseService {
    
    @Autowired
    private AppRepository appRepo;

    private Logger logger = Logger.getLogger(DatabaseService.class.getName());
    
    public boolean addNewUser(String username, String password) {
        try {
            return appRepo.insertUser(username, password);
        } catch (DuplicateKeyException e){
            logger.log(Level.INFO, "error - username is registered");
            return false;
        }
    }
}
