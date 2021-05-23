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
 * Implements the Hibernate persistence processing for business entity League.
 *
 * @author Dev Team
 */

// AIB : #getDAOClassDecl()
public class LeagueDAO extends BaseDAO// ~AIB
 {
    // AIB : #outputDAOFindAllImplementations()
    // ~AIB

    //*****************************************************
    // Attributes
    //*****************************************************
    private static final Logger LOGGER = Logger.getLogger(League.class.getName());

    /**
     * default constructor
     */
    public LeagueDAO() {
    }

    /**
     * Retrieves a League from the persistent store, using the provided primary key.
     * If no match is found, a null League is returned.
     * <p>
     * @param       pk
     * @return      League
     * @exception   ProcessingException
     */
    public League findLeague(LeaguePrimaryKey pk) throws ProcessingException {
        if (pk == null) {
            throw new ProcessingException(
                "LeagueDAO.findLeague(...) cannot have a null primary key argument");
        }

        Query query = null;
        League businessObject = null;

        StringBuilder fromClause = new StringBuilder(
                "from com.harbormaster.bo.League as league where ");

        Session session = null;
        Transaction tx = null;

        try {
            session = currentSession();
            tx = currentTransaction(session);

            // AIB : #getHibernateFindFromClause()
            fromClause.append("league.leagueId = " +
                pk.getLeagueId().toString());
            // ~AIB
            query = session.createQuery(fromClause.toString());

            if (query != null) {
                businessObject = new League();
                businessObject.copy((League) query.list().iterator().next());
            }

            commitTransaction(tx);
        } catch (Throwable exc) {
            businessObject = null;
            exc.printStackTrace();
            throw new ProcessingException(
                "LeagueDAO.findLeague failed for primary key " + pk + " - " +
                exc);
        } finally {
            try {
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "LeagueDAO.findLeague - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        return (businessObject);
    }

    /**
     * returns a Collection of all Leagues
     * @return                ArrayList<League>
     * @exception   ProcessingException
     */
    public ArrayList<League> findAllLeague() throws ProcessingException {
        ArrayList<League> list = new ArrayList<League>();
        ArrayList<League> refList = new ArrayList<League>();
        Query query = null;
        StringBuilder buf = new StringBuilder("from com.harbormaster.bo.League");

        try {
            Session session = currentSession();

            query = session.createQuery(buf.toString());

            if (query != null) {
                list = (ArrayList<League>) query.list();

                League tmp = null;

                for (League listEntry : list) {
                    tmp = new League();
                    tmp.copyShallow(listEntry);
                    refList.add(tmp);
                }
            }
        } catch (Throwable exc) {
            exc.printStackTrace();
            throw new ProcessingException("LeagueDAO.findAllLeague failed - " +
                exc);
        } finally {
            try {
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "LeagueDAO.findAllLeague - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        if (list.size() <= 0) {
            LOGGER.info("LeagueDAO:findAllLeagues() - List is empty.");
        }

        return (refList);
    }

    /**
     * Inserts a new League into the persistent store.
     * @param       businessObject
     * @return      newly persisted League
     * @exception   ProcessingException
     */
    public League createLeague(League businessObject)
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
                    "LeagueDAO.createLeague - Hibernate failed to rollback - " +
                    exc1);
            }

            exc.printStackTrace();
            throw new ProcessingException("LeagueDAO.createLeague failed - " +
                exc);
        } finally {
            try {
                session.flush();
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "LeagueDAO.createLeague - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        // return the businessObject
        return (businessObject);
    }

    /**
     * Stores the provided League to the persistent store.
     *
     * @param       businessObject
     * @return      League        stored entity
     * @exception   ProcessingException
     */
    public League saveLeague(League businessObject) throws ProcessingException {
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
                    "LeagueDAO.saveLeague - Hibernate failed to rollback - " +
                    exc1);
            }

            exc.printStackTrace();
            throw new ProcessingException("LeagueDAO.saveLeague failed - " +
                exc);
        } finally {
            try {
                session.flush();
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "LeagueDAO.saveLeague - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        return (businessObject);
    }

    /**
    * Removes a League from the persistent store.
    *
    * @param        pk                identity of object to remove
    * @exception    ProcessingException
    */
    public void deleteLeague(LeaguePrimaryKey pk) throws ProcessingException {
        Transaction tx = null;
        Session session = null;

        try {
            League bo = findLeague(pk);

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
                    "LeagueDAO.deleteLeague - Hibernate failed to rollback - " +
                    exc1);
            }

            exc.printStackTrace();
            throw new ProcessingException("LeagueDAO.deleteLeague failed - " +
                exc);
        } finally {
            try {
                session.flush();
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "LeagueDAO.deleteLeague - Hibernate failed to close the Session - " +
                    exc);
            }
        }
    }
}
