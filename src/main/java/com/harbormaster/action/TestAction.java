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

import java.io.*;

import java.util.logging.*;


/**
 * Test class for all delegation functions
 *
 * @author Dev Team
 */
public class TestAction extends BaseStrutsAction {
    // attributes
    private ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
    private StreamHandler handler = null;

    /**
    * Handles calling the underlying JUnit testing facility
    */
    public String execute() {
        StreamHandler handler = new StreamHandler(os, new LogTestFormatter());

        com.harbormaster.test.BaseTest.runTheTest(handler);

        return (SUCCESS);
    }

    public String getResults() {
        return (os.toString());
    }

    class LogTestFormatter extends Formatter {
        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder(1000);

            if (record.getLevel() == Level.WARNING) {
                builder.append("<span><style='color:red'>");
            } else if (record.getLevel() == Level.SEVERE) {
                builder.append("<span><style='color:red;font-weight:bolder'>");
            } else {
                builder.append("<span><style='color:black'>");
            }

            builder.append(formatMessage(record));

            builder.append("</style></span>");

            builder.append("<br>");

            return builder.toString();
        }
    }
}
