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
 * Matchup business delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of Matchup related services in the case of a Matchup business related service failing.</li>
 * <li>Exposes a simpler, uniform Matchup interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill Matchup business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
public class MatchupBusinessDelegate extends BaseBusinessDelegate {
    // AIB : #getBusinessMethodImplementations( $classObject.getName() $classObject $classObject.getBusinessMethods() $classObject.getInterfaces() )
    // ~AIB

    //************************************************************************
    // Attributes
    //************************************************************************

    /**
     * Singleton instance
     */
    protected static MatchupBusinessDelegate singleton = null;
    private static final Logger LOGGER = Logger.getLogger(Matchup.class.getName());

    //************************************************************************
    // Public Methods
    //************************************************************************
    /**
     * Default Constructor
     */
    public MatchupBusinessDelegate() {
    }

    /**
         * Matchup Business Delegate Factory Method
         *
         * Returns a singleton instance of MatchupBusinessDelegate().
         * All methods are expected to be self-sufficient.
         *
         * @return         MatchupBusinessDelegate
         */
    public static MatchupBusinessDelegate getMatchupInstance() {
        if (singleton == null) {
            singleton = new MatchupBusinessDelegate();
        }

        return (singleton);
    }

    /**
     * Method to retrieve the Matchup via an MatchupPrimaryKey.
     * @param         key
     * @return         Matchup
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException
     */
    public Matchup getMatchup(MatchupPrimaryKey key)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "MatchupBusinessDelegate:getMatchup - ";

        if (key == null) {
            String errMsg = msgPrefix + "null key provided.";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Matchup returnBO = null;

        MatchupDAO dao = getMatchupDAO();

        try {
            returnBO = dao.findMatchup(key);
        } catch (Exception exc) {
            String errMsg = "MatchupBusinessDelegate:getMatchup( MatchupPrimaryKey key ) - unable to locate Matchup with key " +
                key.toString() + " - " + exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releaseMatchupDAO(dao);
        }

        return returnBO;
    }

    /**
     * Method to retrieve a collection of all Matchups
     *
     * @return         ArrayList<Matchup>
     * @exception ProcessingException Thrown if any problems
     */
    public ArrayList<Matchup> getAllMatchup() throws ProcessingException {
        String msgPrefix = "MatchupBusinessDelegate:getAllMatchup() - ";
        ArrayList<Matchup> array = null;

        MatchupDAO dao = getMatchupDAO();

        try {
            array = dao.findAllMatchup();
        } catch (Exception exc) {
            String errMsg = msgPrefix + exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releaseMatchupDAO(dao);
        }

        return array;
    }

    /**
     * Creates the provided BO.
     * @param                businessObject         Matchup
     * @return       Matchup
     * @exception    ProcessingException
     * @exception        IllegalArgumentException
     */
    public Matchup createMatchup(Matchup businessObject)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "MatchupBusinessDelegate:createMatchup - ";

        if (businessObject == null) {
            String errMsg = msgPrefix + "null businessObject provided";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        // return value once persisted
        MatchupDAO dao = getMatchupDAO();

        try {
            businessObject = dao.createMatchup(businessObject);
        } catch (Exception exc) {
            String errMsg = "MatchupBusinessDelegate:createMatchup() - Unable to create Matchup" +
                exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releaseMatchupDAO(dao);
        }

        return (businessObject);
    }

    /**
     * Saves the underlying BO.
     * @param                businessObject                Matchup
     * @return       what was just saved
     * @exception    ProcessingException
     * @exception          IllegalArgumentException
     */
    public Matchup saveMatchup(Matchup businessObject)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "MatchupBusinessDelegate:saveMatchup - ";

        if (businessObject == null) {
            String errMsg = msgPrefix + "null businessObject provided.";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        // --------------------------------
        // If the businessObject has a key, find it and apply the businessObject
        // --------------------------------
        MatchupPrimaryKey key = businessObject.getMatchupPrimaryKey();

        if (key != null) {
            MatchupDAO dao = getMatchupDAO();

            try {
                businessObject = (Matchup) dao.saveMatchup(businessObject);
            } catch (Exception exc) {
                String errMsg = "MatchupBusinessDelegate:saveMatchup() - Unable to save Matchup" +
                    exc;
                LOGGER.warning(errMsg);
                throw new ProcessingException(errMsg);
            } finally {
                releaseMatchupDAO(dao);
            }
        } else {
            String errMsg = "MatchupBusinessDelegate:saveMatchup() - Unable to create Matchup due to it having a null MatchupPrimaryKey.";

            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        }

        return (businessObject);
    }

    /**
     * Deletes the associatied value object using the provided primary key.
     * @param                key         MatchupPrimaryKey
     * @exception         ProcessingException
     */
    public void delete(MatchupPrimaryKey key)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "MatchupBusinessDelegate:saveMatchup - ";

        if (key == null) {
            String errMsg = msgPrefix + "null key provided.";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        MatchupDAO dao = getMatchupDAO();

        try {
            dao.deleteMatchup(key);
        } catch (Exception exc) {
            String errMsg = msgPrefix +
                "Unable to delete Matchup using key = " + key + ". " + exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releaseMatchupDAO(dao);
        }

        return;
    }

    // business methods
    /**
     * Returns the Matchup specific DAO.
     *
     * @return      Matchup DAO
     */
    public MatchupDAO getMatchupDAO() {
        return (new com.harbormaster.dao.MatchupDAO());
    }

    /**
     * Release the MatchupDAO back to the FrameworkDAOFactory
     */
    public void releaseMatchupDAO(com.harbormaster.dao.MatchupDAO dao) {
        dao = null;
    }
}
