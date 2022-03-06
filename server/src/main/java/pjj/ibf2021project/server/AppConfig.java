package pjj.ibf2021project.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import pjj.ibf2021project.server.services.TwitterService;

@Configuration
public class AppConfig implements WebMvcConfigurer {


	@Autowired
	private TwitterService twitterSvc;

	@EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
        twitterSvc.getStream();
    }
	
}