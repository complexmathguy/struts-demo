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
 * Implements the Hibernate persistence processing for business entity Player.
 *
 * @author Dev Team
 */

// AIB : #getDAOClassDecl()
public class PlayerDAO extends BaseDAO// ~AIB
 {
    // AIB : #outputDAOFindAllImplementations()
    // ~AIB

    //*****************************************************
    // Attributes
    //*****************************************************
    private static final Logger LOGGER = Logger.getLogger(Player.class.getName());

    /**
     * default constructor
     */
    public PlayerDAO() {
    }

    /**
     * Retrieves a Player from the persistent store, using the provided primary key.
     * If no match is found, a null Player is returned.
     * <p>
     * @param       pk
     * @return      Player
     * @exception   ProcessingException
     */
    public Player findPlayer(PlayerPrimaryKey pk) throws ProcessingException {
        if (pk == null) {
            throw new ProcessingException(
                "PlayerDAO.findPlayer(...) cannot have a null primary key argument");
        }

        Query query = null;
        Player businessObject = null;

        StringBuilder fromClause = new StringBuilder(
                "from com.harbormaster.bo.Player as player where ");

        Session session = null;
        Transaction tx = null;

        try {
            session = currentSession();
            tx = currentTransaction(session);

            // AIB : #getHibernateFindFromClause()
            fromClause.append("player.playerId = " +
                pk.getPlayerId().toString());
            // ~AIB
            query = session.createQuery(fromClause.toString());

            if (query != null) {
                businessObject = new Player();
                businessObject.copy((Player) query.list().iterator().next());
            }

            commitTransaction(tx);
        } catch (Throwable exc) {
            businessObject = null;
            exc.printStackTrace();
            throw new ProcessingException(
                "PlayerDAO.findPlayer failed for primary key " + pk + " - " +
                exc);
        } finally {
            try {
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "PlayerDAO.findPlayer - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        return (businessObject);
    }

    /**
     * returns a Collection of all Players
     * @return                ArrayList<Player>
     * @exception   ProcessingException
     */
    public ArrayList<Player> findAllPlayer() throws ProcessingException {
        ArrayList<Player> list = new ArrayList<Player>();
        ArrayList<Player> refList = new ArrayList<Player>();
        Query query = null;
        StringBuilder buf = new StringBuilder("from com.harbormaster.bo.Player");

        try {
            Session session = currentSession();

            query = session.createQuery(buf.toString());

            if (query != null) {
                list = (ArrayList<Player>) query.list();

                Player tmp = null;

                for (Player listEntry : list) {
                    tmp = new Player();
                    tmp.copyShallow(listEntry);
                    refList.add(tmp);
                }
            }
        } catch (Throwable exc) {
            exc.printStackTrace();
            throw new ProcessingException("PlayerDAO.findAllPlayer failed - " +
                exc);
        } finally {
            try {
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "PlayerDAO.findAllPlayer - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        if (list.size() <= 0) {
            LOGGER.info("PlayerDAO:findAllPlayers() - List is empty.");
        }

        return (refList);
    }

    /**
     * Inserts a new Player into the persistent store.
     * @param       businessObject
     * @return      newly persisted Player
     * @exception   ProcessingException
     */
    public Player createPlayer(Player businessObject)
        throws ProcessingException {
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
                    "PlayerDAO.createPlayer - Hibernate failed to rollback - " +
                    exc1);
            }

            exc.printStackTrace();
            throw new ProcessingException("PlayerDAO.createPlayer failed - " +
                exc);
        } finally {
            try {
                session.flush();
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "PlayerDAO.createPlayer - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        // return the businessObject
        return (businessObject);
    }

    /**
     * Stores the provided Player to the persistent store.
     *
     * @param       businessObject
     * @return      Player        stored entity
     * @exception   ProcessingException
     */
    public Player savePlayer(Player businessObject) throws ProcessingException {
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
                    "PlayerDAO.savePlayer - Hibernate failed to rollback - " +
                    exc1);
            }

            exc.printStackTrace();
            throw new ProcessingException("PlayerDAO.savePlayer failed - " +
                exc);
        } finally {
            try {
                session.flush();
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "PlayerDAO.savePlayer - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        return (businessObject);
    }

    /**
    * Removes a Player from the persistent store.
    *
    * @param        pk                identity of object to remove
    * @exception    ProcessingException
    */
    public void deletePlayer(PlayerPrimaryKey pk) throws ProcessingException {
        Transaction tx = null;
        Session session = null;

        try {
            Player bo = findPlayer(pk);

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
                    "PlayerDAO.deletePlayer - Hibernate failed to rollback - " +
                    exc1);
            }

            exc.printStackTrace();
            throw new ProcessingException("PlayerDAO.deletePlayer failed - " +
                exc);
        } finally {
            try {
                session.flush();
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "PlayerDAO.deletePlayer - Hibernate failed to close the Session - " +
                    exc);
            }
        }
    }
}
