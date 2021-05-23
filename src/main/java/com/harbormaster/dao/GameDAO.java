/*******************************************************************************
  Turnstone Biologics Confidential

  2018 Turnstone Biologics
  All Rights Reserved.

  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.

  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.dao;

import com.harbormaster.bo.*;

import com.harbormaster.dao.*;

import com.harbormaster.exception.ProcessingException;

import com.harbormaster.primarykey.*;

import org.hibernate.*;

import org.hibernate.cfg.*;

import java.sql.*;

import java.util.*;
import java.util.logging.Logger;


/**
 * Implements the Hibernate persistence processing for business entity Game.
 *
 * @author Dev Team
 */

// AIB : #getDAOClassDecl()
public class GameDAO extends BaseDAO// ~AIB
 {
    // AIB : #outputDAOFindAllImplementations()
    // ~AIB

    //*****************************************************
    // Attributes
    //*****************************************************
    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    /**
     * default constructor
     */
    public GameDAO() {
    }

    /**
     * Retrieves a Game from the persistent store, using the provided primary key.
     * If no match is found, a null Game is returned.
     * <p>
     * @param       pk
     * @return      Game
     * @exception   ProcessingException
     */
    public Game findGame(GamePrimaryKey pk) throws ProcessingException {
        if (pk == null) {
            throw new ProcessingException(
                "GameDAO.findGame(...) cannot have a null primary key argument");
        }

        Query query = null;
        Game businessObject = null;

        StringBuilder fromClause = new StringBuilder(
                "from com.harbormaster.bo.Game as game where ");

        Session session = null;
        Transaction tx = null;

        try {
            session = currentSession();
            tx = currentTransaction(session);

            // AIB : #getHibernateFindFromClause()
            fromClause.append("game.gameId = " + pk.getGameId().toString());
            // ~AIB
            query = session.createQuery(fromClause.toString());

            if (query != null) {
                businessObject = new Game();
                businessObject.copy((Game) query.list().iterator().next());
            }

            commitTransaction(tx);
        } catch (Throwable exc) {
            businessObject = null;
            exc.printStackTrace();
            throw new ProcessingException(
                "GameDAO.findGame failed for primary key " + pk + " - " + exc);
        } finally {
            try {
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "GameDAO.findGame - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        return (businessObject);
    }

    /**
     * returns a Collection of all Games
     * @return                ArrayList<Game>
     * @exception   ProcessingException
     */
    public ArrayList<Game> findAllGame() throws ProcessingException {
        ArrayList<Game> list = new ArrayList<Game>();
        ArrayList<Game> refList = new ArrayList<Game>();
        Query query = null;
        StringBuilder buf = new StringBuilder("from com.harbormaster.bo.Game");

        try {
            Session session = currentSession();

            query = session.createQuery(buf.toString());

            if (query != null) {
                list = (ArrayList<Game>) query.list();

                Game tmp = null;

                for (Game listEntry : list) {
                    tmp = new Game();
                    tmp.copyShallow(listEntry);
                    refList.add(tmp);
                }
            }
        } catch (Throwable exc) {
            exc.printStackTrace();
            throw new ProcessingException("GameDAO.findAllGame failed - " +
                exc);
        } finally {
            try {
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "GameDAO.findAllGame - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        if (list.size() <= 0) {
            LOGGER.info("GameDAO:findAllGames() - List is empty.");
        }

        return (refList);
    }

    /**
     * Inserts a new Game into the persistent store.
     * @param       businessObject
     * @return      newly persisted Game
     * @exception   ProcessingException
     */
    public Game createGame(Game businessObject) throws ProcessingException {
        Transaction tx = null;
        Session session = null;

        try {
            session = currentSession();
            tx = currentTransaction(session);

            session.save(businessObject);
            commitTransaction(tx);
        } catch (Throwable exc) {
            try {
                if (tx != null) {
                    rollbackTransaction(tx);
                }
            } catch (Throwable exc1) {
                LOGGER.info(
                    "GameDAO.createGame - Hibernate failed to rollback - " +
                    exc1);
            }

            exc.printStackTrace();
            throw new ProcessingException("GameDAO.createGame failed - " + exc);
        } finally {
            try {
                session.flush();
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "GameDAO.createGame - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        // return the businessObject
        return (businessObject);
    }

    /**
     * Stores the provided Game to the persistent store.
     *
     * @param       businessObject
     * @return      Game        stored entity
     * @exception   ProcessingException
     */
    public Game saveGame(Game businessObject) throws ProcessingException {
        Transaction tx = null;
        Session session = null;

        try {
            session = currentSession();
            tx = currentTransaction(session);

            session.update(businessObject);
            commitTransaction(tx);
        } catch (Throwable exc) {
            try {
                if (tx != null) {
                    rollbackTransaction(tx);
                }
            } catch (Throwable exc1) {
                LOGGER.info(
                    "GameDAO.saveGame - Hibernate failed to rollback - " +
                    exc1);
            }

            exc.printStackTrace();
            throw new ProcessingException("GameDAO.saveGame failed - " + exc);
        } finally {
            try {
                session.flush();
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "GameDAO.saveGame - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        return (businessObject);
    }

    /**
    * Removes a Game from the persistent store.
    *
    * @param        pk                identity of object to remove
    * @exception    ProcessingException
    */
    public void deleteGame(GamePrimaryKey pk) throws ProcessingException {
        Transaction tx = null;
        Session session = null;

        try {
            Game bo = findGame(pk);

            session = currentSession();
            tx = currentTransaction(session);
            session.delete(bo);
            commitTransaction(tx);
        } catch (Throwable exc) {
            try {
                if (tx != null) {
                    rollbackTransaction(tx);
                }
            } catch (Throwable exc1) {
                LOGGER.info(
                    "GameDAO.deleteGame - Hibernate failed to rollback - " +
                    exc1);
            }

            exc.printStackTrace();
            throw new ProcessingException("GameDAO.deleteGame failed - " + exc);
        } finally {
            try {
                session.flush();
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "GameDAO.deleteGame - Hibernate failed to close the Session - " +
                    exc);
            }
        }
    }
}
