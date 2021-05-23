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
 * Player business delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of Player related services in the case of a Player business related service failing.</li>
 * <li>Exposes a simpler, uniform Player interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill Player business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
public class PlayerBusinessDelegate extends BaseBusinessDelegate {
    // AIB : #getBusinessMethodImplementations( $classObject.getName() $classObject $classObject.getBusinessMethods() $classObject.getInterfaces() )
    // ~AIB

    //************************************************************************
    // Attributes
    //************************************************************************

    /**
     * Singleton instance
     */
    protected static PlayerBusinessDelegate singleton = null;
    private static final Logger LOGGER = Logger.getLogger(Player.class.getName());

    //************************************************************************
    // Public Methods
    //************************************************************************
    /**
     * Default Constructor
     */
    public PlayerBusinessDelegate() {
    }

    /**
         * Player Business Delegate Factory Method
         *
         * Returns a singleton instance of PlayerBusinessDelegate().
         * All methods are expected to be self-sufficient.
         *
         * @return         PlayerBusinessDelegate
         */
    public static PlayerBusinessDelegate getPlayerInstance() {
        if (singleton == null) {
            singleton = new PlayerBusinessDelegate();
        }

        return (singleton);
    }

    /**
     * Method to retrieve the Player via an PlayerPrimaryKey.
     * @param         key
     * @return         Player
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException
     */
    public Player getPlayer(PlayerPrimaryKey key)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "PlayerBusinessDelegate:getPlayer - ";

        if (key == null) {
            String errMsg = msgPrefix + "null key provided.";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Player returnBO = null;

        PlayerDAO dao = getPlayerDAO();

        try {
            returnBO = dao.findPlayer(key);
        } catch (Exception exc) {
            String errMsg = "PlayerBusinessDelegate:getPlayer( PlayerPrimaryKey key ) - unable to locate Player with key " +
                key.toString() + " - " + exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releasePlayerDAO(dao);
        }

        return returnBO;
    }

    /**
     * Method to retrieve a collection of all Players
     *
     * @return         ArrayList<Player>
     * @exception ProcessingException Thrown if any problems
     */
    public ArrayList<Player> getAllPlayer() throws ProcessingException {
        String msgPrefix = "PlayerBusinessDelegate:getAllPlayer() - ";
        ArrayList<Player> array = null;

        PlayerDAO dao = getPlayerDAO();

        try {
            array = dao.findAllPlayer();
        } catch (Exception exc) {
            String errMsg = msgPrefix + exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releasePlayerDAO(dao);
        }

        return array;
    }

    /**
     * Creates the provided BO.
     * @param                businessObject         Player
     * @return       Player
     * @exception    ProcessingException
     * @exception        IllegalArgumentException
     */
    public Player createPlayer(Player businessObject)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "PlayerBusinessDelegate:createPlayer - ";

        if (businessObject == null) {
            String errMsg = msgPrefix + "null businessObject provided";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        // return value once persisted
        PlayerDAO dao = getPlayerDAO();

        try {
            businessObject = dao.createPlayer(businessObject);
        } catch (Exception exc) {
            String errMsg = "PlayerBusinessDelegate:createPlayer() - Unable to create Player" +
                exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releasePlayerDAO(dao);
        }

        return (businessObject);
    }

    /**
     * Saves the underlying BO.
     * @param                businessObject                Player
     * @return       what was just saved
     * @exception    ProcessingException
     * @exception          IllegalArgumentException
     */
    public Player savePlayer(Player businessObject)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "PlayerBusinessDelegate:savePlayer - ";

        if (businessObject == null) {
            String errMsg = msgPrefix + "null businessObject provided.";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        // --------------------------------
        // If the businessObject has a key, find it and apply the businessObject
        // --------------------------------
        PlayerPrimaryKey key = businessObject.getPlayerPrimaryKey();

        if (key != null) {
            PlayerDAO dao = getPlayerDAO();

            try {
                businessObject = (Player) dao.savePlayer(businessObject);
            } catch (Exception exc) {
                String errMsg = "PlayerBusinessDelegate:savePlayer() - Unable to save Player" +
                    exc;
                LOGGER.warning(errMsg);
                throw new ProcessingException(errMsg);
            } finally {
                releasePlayerDAO(dao);
            }
        } else {
            String errMsg = "PlayerBusinessDelegate:savePlayer() - Unable to create Player due to it having a null PlayerPrimaryKey.";

            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        }

        return (businessObject);
    }

    /**
     * Deletes the associatied value object using the provided primary key.
     * @param                key         PlayerPrimaryKey
     * @exception         ProcessingException
     */
    public void delete(PlayerPrimaryKey key)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "PlayerBusinessDelegate:savePlayer - ";

        if (key == null) {
            String errMsg = msgPrefix + "null key provided.";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        PlayerDAO dao = getPlayerDAO();

        try {
            dao.deletePlayer(key);
        } catch (Exception exc) {
            String errMsg = msgPrefix + "Unable to delete Player using key = " +
                key + ". " + exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releasePlayerDAO(dao);
        }

        return;
    }

    // business methods
    /**
     * Returns the Player specific DAO.
     *
     * @return      Player DAO
     */
    public PlayerDAO getPlayerDAO() {
        return (new com.harbormaster.dao.PlayerDAO());
    }

    /**
     * Release the PlayerDAO back to the FrameworkDAOFactory
     */
    public void releasePlayerDAO(com.harbormaster.dao.PlayerDAO dao) {
        dao = null;
    }
}
