/*******************************************************************************
  Turnstone Biologics Confidential

  2018 Turnstone Biologics
  All Rights Reserved.

  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.

  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.converter;

import org.apache.struts2.util.StrutsTypeConverter;

import java.sql.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FrameworkStrutsEnumConverter extends StrutsTypeConverter {
    // attributes
    private static final Logger LOGGER = Logger.getLogger(FrameworkStrutsEnumConverter.class.getName());

    public Object convertFromString(Map context, String[] values, Class toClass) {
        Enum e = null;

        if (values == null) {
            LOGGER.info(
                "***FrameworkStrutsEnumConverter.convertFromString() values[] is null ");

            return e;
        }

        if (values.length == 0) {
            LOGGER.info(
                "***FrameworkStrutsEnumConverter.convertFromString() values[] is empty");

            return e;
        }

        String enumValue = values[0];

        if ((enumValue != null) && (enumValue.length() > 0) &&
                (enumValue.trim().length() > 0)) {
            e = Enum.valueOf(toClass, enumValue);
        }

        LOGGER.info(
            "***FrameworkStrutsEnumConverter.convertFromString() enumClass: " +
            toClass + ", enumValue: " + values[0] + ", returning enum: " +
            e.toString());

        return e;
    }

    public String convertToString(Map context, Object o) {
        if (o == null) {
            LOGGER.info(
                "***FrameworkStrutsEnumConverter.convertToString() object arg is null.");
        }

        LOGGER.info("***FrameworkStrutsEnumConverter.convertToString() with " +
            o.toString());

        return (o.toString());
    }

    protected Object performFallbackConversion(Map context, Object o,
        Class toClass) {
        LOGGER.info(
            "***inside of FrameworkStrutsEnumConverter.performFallbackConversion() with " +
            o.getClass().getName() + " and " + toClass.getName());

        return super.performFallbackConversion(context, o, toClass);
    }
}
