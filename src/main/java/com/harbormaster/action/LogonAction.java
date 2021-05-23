/*******************************************************************************
  Turnstone Biologics Confidential

  2018 Turnstone Biologics
  All Rights Reserved.

  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.

  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.action;

import java.util.logging.Logger;


/**
 * General Logon Action.

 * @author Dev Team
 */
public class LogonAction extends BaseStrutsAction {
    private static final Logger LOGGER = Logger.getLogger(LogonAction.class.getName());

    // attributes
    protected String userID = null;
    protected String password = null;

    //************************************************************************    
    // Public Methods
    //************************************************************************
    public String getUserID() {
        return (userID);
    }

    public void setUserID(String id) {
        userID = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pw) {
        password = pw;
    }

    /**
     * Struts Action processing method
     */
    public String execute() throws Exception {
        try {
            LOGGER.info("logging in starting...");

            boolean authenticated = false;

            if (!authenticated) {
                authenticated = authenticateUser(userID, password);
            }

            if (authenticated == true) {
                LOGGER.info("logging in success");

                return "success";
            } else {
                LOGGER.info("logging in failed");

                return "LOGIN_FAILURE";
            }
        } catch (Throwable generalExc) {
            LOGGER.severe("Authentication failed: userid-" + userID + ", " +
                "password-" + password);

            return "LOGIN_FAILURE";
        }
    }
}
