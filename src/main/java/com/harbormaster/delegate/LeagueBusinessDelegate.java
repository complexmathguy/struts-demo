/*******************************************************************************
  Turnstone Biologics Confidential

  2018 Turnstone Biologics
  All Rights Reserved.

  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.

  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.delegate;

import com.harbormaster.bo.*;

import com.harbormaster.dao.*;

import com.harbormaster.exception.*;

import com.harbormaster.primarykey.*;

import java.io.IOException;

import java.util.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * League business delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of League related services in the case of a League business related service failing.</li>
 * <li>Exposes a simpler, uniform League interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill League business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
public class LeagueBusinessDelegate extends BaseBusinessDelegate {
    // AIB : #getBusinessMethodImplementations( $classObject.getName() $classObject $classObject.getBusinessMethods() $classObject.getInterfaces() )
    // ~AIB

    //************************************************************************
    // Attributes
    //************************************************************************

    /**
     * Singleton instance
     */
    protected static LeagueBusinessDelegate singleton = null;
    private static final Logger LOGGER = Logger.getLogger(League.class.getName());

    //************************************************************************
    // Public Methods
    //************************************************************************
    /**
     * Default Constructor
     */
    public LeagueBusinessDelegate() {
    }

    /**
         * League Business Delegate Factory Method
         *
         * Returns a singleton instance of LeagueBusinessDelegate().
         * All methods are expected to be self-sufficient.
         *
         * @return         LeagueBusinessDelegate
         */
    public static LeagueBusinessDelegate getLeagueInstance() {
        if (singleton == null) {
            singleton = new LeagueBusinessDelegate();
        }

        return (singleton);
    }

    /**
     * Method to retrieve the League via an LeaguePrimaryKey.
     * @param         key
     * @return         League
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException
     */
    public League getLeague(LeaguePrimaryKey key)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "LeagueBusinessDelegate:getLeague - ";

        if (key == null) {
            String errMsg = msgPrefix + "null key provided.";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        League returnBO = null;

        LeagueDAO dao = getLeagueDAO();

        try {
            returnBO = dao.findLeague(key);
        } catch (Exception exc) {
            String errMsg = "LeagueBusinessDelegate:getLeague( LeaguePrimaryKey key ) - unable to locate League with key " +
                key.toString() + " - " + exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releaseLeagueDAO(dao);
        }

        return returnBO;
    }

    /**
     * Method to retrieve a collection of all Leagues
     *
     * @return         ArrayList<League>
     * @exception ProcessingException Thrown if any problems
     */
    public ArrayList<League> getAllLeague() throws ProcessingException {
        String msgPrefix = "LeagueBusinessDelegate:getAllLeague() - ";
        ArrayList<League> array = null;

        LeagueDAO dao = getLeagueDAO();

        try {
            array = dao.findAllLeague();
        } catch (Exception exc) {
            String errMsg = msgPrefix + exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releaseLeagueDAO(dao);
        }

        return array;
    }

    /**
     * Creates the provided BO.
     * @param                businessObject         League
     * @return       League
     * @exception    ProcessingException
     * @exception        IllegalArgumentException
     */
    public League createLeague(League businessObject)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "LeagueBusinessDelegate:createLeague - ";

        if (businessObject == null) {
            String errMsg = msgPrefix + "null businessObject provided";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        // return value once persisted
        LeagueDAO dao = getLeagueDAO();

        try {
            businessObject = dao.createLeague(businessObject);
        } catch (Exception exc) {
            String errMsg = "LeagueBusinessDelegate:createLeague() - Unable to create League" +
                exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releaseLeagueDAO(dao);
        }

        return (businessObject);
    }

    /**
     * Saves the underlying BO.
     * @param                businessObject                League
     * @return       what was just saved
     * @exception    ProcessingException
     * @exception          IllegalArgumentException
     */
    public League saveLeague(League businessObject)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "LeagueBusinessDelegate:saveLeague - ";

        if (businessObject == null) {
            String errMsg = msgPrefix + "null businessObject provided.";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        // --------------------------------
        // If the businessObject has a key, find it and apply the businessObject
        // --------------------------------
        LeaguePrimaryKey key = businessObject.getLeaguePrimaryKey();

        if (key != null) {
            LeagueDAO dao = getLeagueDAO();

            try {
                businessObject = (League) dao.saveLeague(businessObject);
            } catch (Exception exc) {
                String errMsg = "LeagueBusinessDelegate:saveLeague() - Unable to save League" +
                    exc;
                LOGGER.warning(errMsg);
                throw new ProcessingException(errMsg);
            } finally {
                releaseLeagueDAO(dao);
            }
        } else {
            String errMsg = "LeagueBusinessDelegate:saveLeague() - Unable to create League due to it having a null LeaguePrimaryKey.";

            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        }

        return (businessObject);
    }

    /**
     * Deletes the associatied value object using the provided primary key.
     * @param                key         LeaguePrimaryKey
     * @exception         ProcessingException
     */
    public void delete(LeaguePrimaryKey key)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "LeagueBusinessDelegate:saveLeague - ";

        if (key == null) {
            String errMsg = msgPrefix + "null key provided.";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        LeagueDAO dao = getLeagueDAO();

        try {
            dao.deleteLeague(key);
        } catch (Exception exc) {
            String errMsg = msgPrefix + "Unable to delete League using key = " +
                key + ". " + exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releaseLeagueDAO(dao);
        }

        return;
    }

    // business methods
    /**
     * Returns the League specific DAO.
     *
     * @return      League DAO
     */
    public LeagueDAO getLeagueDAO() {
        return (new com.harbormaster.dao.LeagueDAO());
    }

    /**
     * Release the LeagueDAO back to the FrameworkDAOFactory
     */
    public void releaseLeagueDAO(com.harbormaster.dao.LeagueDAO dao) {
        dao = null;
    }
}
