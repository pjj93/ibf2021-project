package pjj.ibf2021project.server.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pjj.ibf2021project.server.services.TwitterService;

@RestController
@RequestMapping(path="/api/twitter")
public class TwitterRestController {
    
    @Autowired
    private TwitterService twitterSvc;

    private Logger logger = Logger.getLogger(TwitterRestController.class.getName());

    @GetMapping(path="/user/{username}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUser(@PathVariable String username) {
        
        twitterSvc.getUser(username);
        // logger.log(Level.INFO, "BEARER_TOKEN >>> " + bearer_token);
        // logger.log(Level.INFO, "URL >>> " + url);
    
        return null;
    }

    @GetMapping(path="/stream/rules")
    public ResponseEntity<String> getStreamRules() {

        twitterSvc.getStreamRules();

        return null;
    }

    @PostMapping(path="/stream/rules")
    public ResponseEntity<String> addRule() {

        twitterSvc.addRule("posttry", "testuser69420", "post from server");
        return null;
    }

    @GetMapping(path="/stream")
    public ResponseEntity<String> getStream() throws IOException, URISyntaxException {

        twitterSvc.getStream();

        return null;
    }
}