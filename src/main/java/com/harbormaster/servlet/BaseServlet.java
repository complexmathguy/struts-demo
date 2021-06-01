/*******************************************************************************
  Turnstone Biologics Confidential

  2018 Turnstone Biologics
  All Rights Reserved.

  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.

  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.servlet;

import com.harbormaster.persistent.FrameworkPersistenceHelper;

import javax.servlet.ServletConfig;
import javax.servlet.http.*;


/**
 * Base class for application servlet(s)
 *
 * @author Dev Team
 */
public class BaseServlet extends HttpServlet {
    public void init(ServletConfig config)
        throws javax.servlet.ServletException {
        super.init(config);
        // give a Hibernate a kick-start during servlet initialization instead of
        // hitting it cold
        FrameworkPersistenceHelper.self();
    }
}
