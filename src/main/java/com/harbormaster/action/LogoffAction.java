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

import com.harbormaster.bo.*;

import com.harbormaster.delegate.*;

import java.util.logging.Logger;

import javax.servlet.http.*;


/**
 * General Logoff Action.

 * @author Dev Team
 */
public class LogoffAction extends BaseStrutsAction {
    // attributes
    private static final Logger LOGGER = Logger.getLogger(LogoffAction.class.getName());

    public String execute() throws Exception {
        try {
            // terminate the session
            HttpSession currentHttpSession = getServletRequest()
                                                 .getSession(false);

            if (currentHttpSession != null) {
                try {
                    currentHttpSession.invalidate();
                } catch (IllegalStateException exc) {
                    LOGGER.severe(
                        "LogoffAction.execute() - failed to invalidate session - " +
                        exc.getMessage());

                    // do nothing because it is thrown if the session is 
                    // already invalidated
                }
            }
        } catch (Throwable generalExc) {
            LOGGER.severe("LogoffAction.execute() - general exception- " +
                generalExc.getMessage());

            return "LOGIN_FAILURE";
        }

        return SUCCESS;
    }
}
