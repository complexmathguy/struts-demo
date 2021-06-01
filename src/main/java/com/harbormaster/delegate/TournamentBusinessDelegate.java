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
 * Tournament business delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of Tournament related services in the case of a Tournament business related service failing.</li>
 * <li>Exposes a simpler, uniform Tournament interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill Tournament business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
public class TournamentBusinessDelegate extends BaseBusinessDelegate {
    // AIB : #getBusinessMethodImplementations( $classObject.getName() $classObject $classObject.getBusinessMethods() $classObject.getInterfaces() )
    // ~AIB

    //************************************************************************
    // Attributes
    //************************************************************************

    /**
     * Singleton instance
     */
    protected static TournamentBusinessDelegate singleton = null;
    private static final Logger LOGGER = Logger.getLogger(Tournament.class.getName());

    //************************************************************************
    // Public Methods
    //************************************************************************
    /**
     * Default Constructor
     */
    public TournamentBusinessDelegate() {
    }

    /**
         * Tournament Business Delegate Factory Method
         *
         * Returns a singleton instance of TournamentBusinessDelegate().
         * All methods are expected to be self-sufficient.
         *
         * @return         TournamentBusinessDelegate
         */
    public static TournamentBusinessDelegate getTournamentInstance() {
        if (singleton == null) {
            singleton = new TournamentBusinessDelegate();
        }

        return (singleton);
    }

    /**
     * Method to retrieve the Tournament via an TournamentPrimaryKey.
     * @param         key
     * @return         Tournament
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException
     */
    public Tournament getTournament(TournamentPrimaryKey key)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "TournamentBusinessDelegate:getTournament - ";

        if (key == null) {
            String errMsg = msgPrefix + "null key provided.";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Tournament returnBO = null;

        TournamentDAO dao = getTournamentDAO();

        try {
            returnBO = dao.findTournament(key);
        } catch (Exception exc) {
            String errMsg = "TournamentBusinessDelegate:getTournament( TournamentPrimaryKey key ) - unable to locate Tournament with key " +
                key.toString() + " - " + exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releaseTournamentDAO(dao);
        }

        return returnBO;
    }

    /**
     * Method to retrieve a collection of all Tournaments
     *
     * @return         ArrayList<Tournament>
     * @exception ProcessingException Thrown if any problems
     */
    public ArrayList<Tournament> getAllTournament() throws ProcessingException {
        String msgPrefix = "TournamentBusinessDelegate:getAllTournament() - ";
        ArrayList<Tournament> array = null;

        TournamentDAO dao = getTournamentDAO();

        try {
            array = dao.findAllTournament();
        } catch (Exception exc) {
            String errMsg = msgPrefix + exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releaseTournamentDAO(dao);
        }

        return array;
    }

    /**
     * Creates the provided BO.
     * @param                businessObject         Tournament
     * @return       Tournament
     * @exception    ProcessingException
     * @exception        IllegalArgumentException
     */
    public Tournament createTournament(Tournament businessObject)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "TournamentBusinessDelegate:createTournament - ";

        if (businessObject == null) {
            String errMsg = msgPrefix + "null businessObject provided";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        // return value once persisted
        TournamentDAO dao = getTournamentDAO();

        try {
            businessObject = dao.createTournament(businessObject);
        } catch (Exception exc) {
            String errMsg = "TournamentBusinessDelegate:createTournament() - Unable to create Tournament" +
                exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releaseTournamentDAO(dao);
        }

        return (businessObject);
    }

    /**
     * Saves the underlying BO.
     * @param                businessObject                Tournament
     * @return       what was just saved
     * @exception    ProcessingException
     * @exception          IllegalArgumentException
     */
    public Tournament saveTournament(Tournament businessObject)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "TournamentBusinessDelegate:saveTournament - ";

        if (businessObject == null) {
            String errMsg = msgPrefix + "null businessObject provided.";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        // --------------------------------
        // If the businessObject has a key, find it and apply the businessObject
        // --------------------------------
        TournamentPrimaryKey key = businessObject.getTournamentPrimaryKey();

        if (key != null) {
            TournamentDAO dao = getTournamentDAO();

            try {
                businessObject = (Tournament) dao.saveTournament(businessObject);
            } catch (Exception exc) {
                String errMsg = "TournamentBusinessDelegate:saveTournament() - Unable to save Tournament" +
                    exc;
                LOGGER.warning(errMsg);
                throw new ProcessingException(errMsg);
            } finally {
                releaseTournamentDAO(dao);
            }
        } else {
            String errMsg = "TournamentBusinessDelegate:saveTournament() - Unable to create Tournament due to it having a null TournamentPrimaryKey.";

            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        }

        return (businessObject);
    }

    /**
     * Deletes the associatied value object using the provided primary key.
     * @param                key         TournamentPrimaryKey
     * @exception         ProcessingException
     */
    public void delete(TournamentPrimaryKey key)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "TournamentBusinessDelegate:saveTournament - ";

        if (key == null) {
            String errMsg = msgPrefix + "null key provided.";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        TournamentDAO dao = getTournamentDAO();

        try {
            dao.deleteTournament(key);
        } catch (Exception exc) {
            String errMsg = msgPrefix +
                "Unable to delete Tournament using key = " + key + ". " + exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releaseTournamentDAO(dao);
        }

        return;
    }

    // business methods
    /**
     * Returns the Tournament specific DAO.
     *
     * @return      Tournament DAO
     */
    public TournamentDAO getTournamentDAO() {
        return (new com.harbormaster.dao.TournamentDAO());
    }

    /**
     * Release the TournamentDAO back to the FrameworkDAOFactory
     */
    public void releaseTournamentDAO(com.harbormaster.dao.TournamentDAO dao) {
        dao = null;
    }
}
