package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test1")
public class InfoServiceTest1 implements InfoService {

    Logger logger = LoggerFactory.getLogger(InfoServiceTest1.class);

    public String getPort() {
        logger.info("Was invoked method for get port");
        String port = "1111";
        return port;
    }
}
