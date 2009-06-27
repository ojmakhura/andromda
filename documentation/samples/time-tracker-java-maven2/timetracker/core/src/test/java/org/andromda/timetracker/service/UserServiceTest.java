package org.andromda.timetracker.service;

import org.andromda.timetracker.ServiceLocator;
import org.andromda.timetracker.vo.UserVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.Configuration;
import org.testng.annotations.Test;

public class UserServiceTest {
    private Log logger = LogFactory.getLog(UserServiceTest.class);

    private UserService userService;

    /**
     * Initialize test suite
     */
    @Configuration(beforeSuite=true)
    public void initializeTestSuite() {

        // Initialize ServiceLocator
        logger.info("Initializing ServiceLocator");
        ServiceLocator locator = ServiceLocator.instance();
        locator.init("testBeanRefFactory.xml", "beanRefFactory");

        // Initialize UserService
        logger.info("Initializing UserService");
        userService = locator.getUserService();
    }

    @Test
    public void testGetAllUsers() {

        logger.info("testGetAllUsers:");
        UserVO[] users = userService.getAllUsers();

        for (int i=0; i<users.length; i++) {
            logger.info(users[i].getUsername());
        }
    }
}