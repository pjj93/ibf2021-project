package pjj.ibf2021project.server.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pjj.ibf2021project.server.services.FtxService;

@RestController
@RequestMapping(path="/api/ftx")
public class FtxRestController {
    
    @Autowired
    private FtxService ftxSvc;

    @GetMapping(path="/balance", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUser() {
        
        ftxSvc.getBalance();
        return null;
    }

    @GetMapping(path="/placeorder")
    public void placeorder() throws IOException {
        // ftxSvc.NewOrder();
        ftxSvc.placeOrderHttpClient();
    }
    
}
