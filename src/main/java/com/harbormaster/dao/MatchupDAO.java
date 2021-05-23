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
 * Implements the Hibernate persistence processing for business entity Matchup.
 *
 * @author Dev Team
 */

// AIB : #getDAOClassDecl()
public class MatchupDAO extends BaseDAO// ~AIB
 {
    // AIB : #outputDAOFindAllImplementations()
    // ~AIB

    //*****************************************************
    // Attributes
    //*****************************************************
    private static final Logger LOGGER = Logger.getLogger(Matchup.class.getName());

    /**
     * default constructor
     */
    public MatchupDAO() {
    }

    /**
     * Retrieves a Matchup from the persistent store, using the provided primary key.
     * If no match is found, a null Matchup is returned.
     * <p>
     * @param       pk
     * @return      Matchup
     * @exception   ProcessingException
     */
    public Matchup findMatchup(MatchupPrimaryKey pk) throws ProcessingException {
        if (pk == null) {
            throw new ProcessingException(
                "MatchupDAO.findMatchup(...) cannot have a null primary key argument");
        }

        Query query = null;
        Matchup businessObject = null;

        StringBuilder fromClause = new StringBuilder(
                "from com.harbormaster.bo.Matchup as matchup where ");

        Session session = null;
        Transaction tx = null;

        try {
            session = currentSession();
            tx = currentTransaction(session);

            // AIB : #getHibernateFindFromClause()
            fromClause.append("matchup.matchupId = " +
                pk.getMatchupId().toString());
            // ~AIB
            query = session.createQuery(fromClause.toString());

            if (query != null) {
                businessObject = new Matchup();
                businessObject.copy((Matchup) query.list().iterator().next());
            }

            commitTransaction(tx);
        } catch (Throwable exc) {
            businessObject = null;
            exc.printStackTrace();
            throw new ProcessingException(
                "MatchupDAO.findMatchup failed for primary key " + pk + " - " +
                exc);
        } finally {
            try {
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "MatchupDAO.findMatchup - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        return (businessObject);
    }

    /**
     * returns a Collection of all Matchups
     * @return                ArrayList<Matchup>
     * @exception   ProcessingException
     */
    public ArrayList<Matchup> findAllMatchup() throws ProcessingException {
        ArrayList<Matchup> list = new ArrayList<Matchup>();
        ArrayList<Matchup> refList = new ArrayList<Matchup>();
        Query query = null;
        StringBuilder buf = new StringBuilder(
                "from com.harbormaster.bo.Matchup");

        try {
            Session session = currentSession();

            query = session.createQuery(buf.toString());

            if (query != null) {
                list = (ArrayList<Matchup>) query.list();

                Matchup tmp = null;

                for (Matchup listEntry : list) {
                    tmp = new Matchup();
                    tmp.copyShallow(listEntry);
                    refList.add(tmp);
                }
            }
        } catch (Throwable exc) {
            exc.printStackTrace();
            throw new ProcessingException("MatchupDAO.findAllMatchup failed - " +
                exc);
        } finally {
            try {
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "MatchupDAO.findAllMatchup - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        if (list.size() <= 0) {
            LOGGER.info("MatchupDAO:findAllMatchups() - List is empty.");
        }

        return (refList);
    }

    /**
     * Inserts a new Matchup into the persistent store.
     * @param       businessObject
     * @return      newly persisted Matchup
     * @exception   ProcessingException
     */
    public Matchup createMatchup(Matchup businessObject)
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
                    "MatchupDAO.createMatchup - Hibernate failed to rollback - " +
                    exc1);
            }

            exc.printStackTrace();
            throw new ProcessingException("MatchupDAO.createMatchup failed - " +
                exc);
        } finally {
            try {
                session.flush();
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "MatchupDAO.createMatchup - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        // return the businessObject
        return (businessObject);
    }

    /**
     * Stores the provided Matchup to the persistent store.
     *
     * @param       businessObject
     * @return      Matchup        stored entity
     * @exception   ProcessingException
     */
    public Matchup saveMatchup(Matchup businessObject)
        throws ProcessingException {
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
                    "MatchupDAO.saveMatchup - Hibernate failed to rollback - " +
                    exc1);
            }

            exc.printStackTrace();
            throw new ProcessingException("MatchupDAO.saveMatchup failed - " +
                exc);
        } finally {
            try {
                session.flush();
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "MatchupDAO.saveMatchup - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        return (businessObject);
    }

    /**
    * Removes a Matchup from the persistent store.
    *
    * @param        pk                identity of object to remove
    * @exception    ProcessingException
    */
    public void deleteMatchup(MatchupPrimaryKey pk) throws ProcessingException {
        Transaction tx = null;
        Session session = null;

        try {
            Matchup bo = findMatchup(pk);

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
                    "MatchupDAO.deleteMatchup - Hibernate failed to rollback - " +
                    exc1);
            }

            exc.printStackTrace();
            throw new ProcessingException("MatchupDAO.deleteMatchup failed - " +
                exc);
        } finally {
            try {
                session.flush();
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "MatchupDAO.deleteMatchup - Hibernate failed to close the Session - " +
                    exc);
            }
        }
    }
}
