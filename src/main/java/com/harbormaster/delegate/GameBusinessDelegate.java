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
 * Game business delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of Game related services in the case of a Game business related service failing.</li>
 * <li>Exposes a simpler, uniform Game interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill Game business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
public class GameBusinessDelegate extends BaseBusinessDelegate {
    // AIB : #getBusinessMethodImplementations( $classObject.getName() $classObject $classObject.getBusinessMethods() $classObject.getInterfaces() )
    // ~AIB

    //************************************************************************
    // Attributes
    //************************************************************************

    /**
     * Singleton instance
     */
    protected static GameBusinessDelegate singleton = null;
    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    //************************************************************************
    // Public Methods
    //************************************************************************
    /**
     * Default Constructor
     */
    public GameBusinessDelegate() {
    }

    /**
         * Game Business Delegate Factory Method
         *
         * Returns a singleton instance of GameBusinessDelegate().
         * All methods are expected to be self-sufficient.
         *
         * @return         GameBusinessDelegate
         */
    public static GameBusinessDelegate getGameInstance() {
        if (singleton == null) {
            singleton = new GameBusinessDelegate();
        }

        return (singleton);
    }

    /**
     * Method to retrieve the Game via an GamePrimaryKey.
     * @param         key
     * @return         Game
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException
     */
    public Game getGame(GamePrimaryKey key)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "GameBusinessDelegate:getGame - ";

        if (key == null) {
            String errMsg = msgPrefix + "null key provided.";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Game returnBO = null;

        GameDAO dao = getGameDAO();

        try {
            returnBO = dao.findGame(key);
        } catch (Exception exc) {
            String errMsg = "GameBusinessDelegate:getGame( GamePrimaryKey key ) - unable to locate Game with key " +
                key.toString() + " - " + exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releaseGameDAO(dao);
        }

        return returnBO;
    }

    /**
     * Method to retrieve a collection of all Games
     *
     * @return         ArrayList<Game>
     * @exception ProcessingException Thrown if any problems
     */
    public ArrayList<Game> getAllGame() throws ProcessingException {
        String msgPrefix = "GameBusinessDelegate:getAllGame() - ";
        ArrayList<Game> array = null;

        GameDAO dao = getGameDAO();

        try {
            array = dao.findAllGame();
        } catch (Exception exc) {
            String errMsg = msgPrefix + exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releaseGameDAO(dao);
        }

        return array;
    }

    /**
     * Creates the provided BO.
     * @param                businessObject         Game
     * @return       Game
     * @exception    ProcessingException
     * @exception        IllegalArgumentException
     */
    public Game createGame(Game businessObject)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "GameBusinessDelegate:createGame - ";

        if (businessObject == null) {
            String errMsg = msgPrefix + "null businessObject provided";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        // return value once persisted
        GameDAO dao = getGameDAO();

        try {
            businessObject = dao.createGame(businessObject);
        } catch (Exception exc) {
            String errMsg = "GameBusinessDelegate:createGame() - Unable to create Game" +
                exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releaseGameDAO(dao);
        }

        return (businessObject);
    }

    /**
     * Saves the underlying BO.
     * @param                businessObject                Game
     * @return       what was just saved
     * @exception    ProcessingException
     * @exception          IllegalArgumentException
     */
    public Game saveGame(Game businessObject)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "GameBusinessDelegate:saveGame - ";

        if (businessObject == null) {
            String errMsg = msgPrefix + "null businessObject provided.";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        // --------------------------------
        // If the businessObject has a key, find it and apply the businessObject
        // --------------------------------
        GamePrimaryKey key = businessObject.getGamePrimaryKey();

        if (key != null) {
            GameDAO dao = getGameDAO();

            try {
                businessObject = (Game) dao.saveGame(businessObject);
            } catch (Exception exc) {
                String errMsg = "GameBusinessDelegate:saveGame() - Unable to save Game" +
                    exc;
                LOGGER.warning(errMsg);
                throw new ProcessingException(errMsg);
            } finally {
                releaseGameDAO(dao);
            }
        } else {
            String errMsg = "GameBusinessDelegate:saveGame() - Unable to create Game due to it having a null GamePrimaryKey.";

            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        }

        return (businessObject);
    }

    /**
     * Deletes the associatied value object using the provided primary key.
     * @param                key         GamePrimaryKey
     * @exception         ProcessingException
     */
    public void delete(GamePrimaryKey key)
        throws ProcessingException, IllegalArgumentException {
        String msgPrefix = "GameBusinessDelegate:saveGame - ";

        if (key == null) {
            String errMsg = msgPrefix + "null key provided.";
            LOGGER.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        GameDAO dao = getGameDAO();

        try {
            dao.deleteGame(key);
        } catch (Exception exc) {
            String errMsg = msgPrefix + "Unable to delete Game using key = " +
                key + ". " + exc;
            LOGGER.warning(errMsg);
            throw new ProcessingException(errMsg);
        } finally {
            releaseGameDAO(dao);
        }

        return;
    }

    // business methods
    /**
     * Returns the Game specific DAO.
     *
     * @return      Game DAO
     */
    public GameDAO getGameDAO() {
        return (new com.harbormaster.dao.GameDAO());
    }

    /**
     * Release the GameDAO back to the FrameworkDAOFactory
     */
    public void releaseGameDAO(com.harbormaster.dao.GameDAO dao) {
        dao = null;
    }
}
