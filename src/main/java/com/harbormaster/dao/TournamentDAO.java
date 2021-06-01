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
 * Implements the Hibernate persistence processing for business entity Tournament.
 *
 * @author Dev Team
 */

// AIB : #getDAOClassDecl()
public class TournamentDAO extends BaseDAO// ~AIB
 {
    // AIB : #outputDAOFindAllImplementations()
    // ~AIB

    //*****************************************************
    // Attributes
    //*****************************************************
    private static final Logger LOGGER = Logger.getLogger(Tournament.class.getName());

    /**
     * default constructor
     */
    public TournamentDAO() {
    }

    /**
     * Retrieves a Tournament from the persistent store, using the provided primary key.
     * If no match is found, a null Tournament is returned.
     * <p>
     * @param       pk
     * @return      Tournament
     * @exception   ProcessingException
     */
    public Tournament findTournament(TournamentPrimaryKey pk)
        throws ProcessingException {
        if (pk == null) {
            throw new ProcessingException(
                "TournamentDAO.findTournament(...) cannot have a null primary key argument");
        }

        Query query = null;
        Tournament businessObject = null;

        StringBuilder fromClause = new StringBuilder(
                "from com.harbormaster.bo.Tournament as tournament where ");

        Session session = null;
        Transaction tx = null;

        try {
            session = currentSession();
            tx = currentTransaction(session);

            // AIB : #getHibernateFindFromClause()
            fromClause.append("tournament.tournamentId = " +
                pk.getTournamentId().toString());
            // ~AIB
            query = session.createQuery(fromClause.toString());

            if (query != null) {
                businessObject = new Tournament();
                businessObject.copy((Tournament) query.list().iterator().next());
            }

            commitTransaction(tx);
        } catch (Throwable exc) {
            businessObject = null;
            exc.printStackTrace();
            throw new ProcessingException(
                "TournamentDAO.findTournament failed for primary key " + pk +
                " - " + exc);
        } finally {
            try {
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "TournamentDAO.findTournament - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        return (businessObject);
    }

    /**
     * returns a Collection of all Tournaments
     * @return                ArrayList<Tournament>
     * @exception   ProcessingException
     */
    public ArrayList<Tournament> findAllTournament() throws ProcessingException {
        ArrayList<Tournament> list = new ArrayList<Tournament>();
        ArrayList<Tournament> refList = new ArrayList<Tournament>();
        Query query = null;
        StringBuilder buf = new StringBuilder(
                "from com.harbormaster.bo.Tournament");

        try {
            Session session = currentSession();

            query = session.createQuery(buf.toString());

            if (query != null) {
                list = (ArrayList<Tournament>) query.list();

                Tournament tmp = null;

                for (Tournament listEntry : list) {
                    tmp = new Tournament();
                    tmp.copyShallow(listEntry);
                    refList.add(tmp);
                }
            }
        } catch (Throwable exc) {
            exc.printStackTrace();
            throw new ProcessingException(
                "TournamentDAO.findAllTournament failed - " + exc);
        } finally {
            try {
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "TournamentDAO.findAllTournament - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        if (list.size() <= 0) {
            LOGGER.info("TournamentDAO:findAllTournaments() - List is empty.");
        }

        return (refList);
    }

    /**
     * Inserts a new Tournament into the persistent store.
     * @param       businessObject
     * @return      newly persisted Tournament
     * @exception   ProcessingException
     */
    public Tournament createTournament(Tournament businessObject)
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
                    "TournamentDAO.createTournament - Hibernate failed to rollback - " +
                    exc1);
            }

            exc.printStackTrace();
            throw new ProcessingException(
                "TournamentDAO.createTournament failed - " + exc);
        } finally {
            try {
                session.flush();
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "TournamentDAO.createTournament - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        // return the businessObject
        return (businessObject);
    }

    /**
     * Stores the provided Tournament to the persistent store.
     *
     * @param       businessObject
     * @return      Tournament        stored entity
     * @exception   ProcessingException
     */
    public Tournament saveTournament(Tournament businessObject)
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
                    "TournamentDAO.saveTournament - Hibernate failed to rollback - " +
                    exc1);
            }

            exc.printStackTrace();
            throw new ProcessingException(
                "TournamentDAO.saveTournament failed - " + exc);
        } finally {
            try {
                session.flush();
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "TournamentDAO.saveTournament - Hibernate failed to close the Session - " +
                    exc);
            }
        }

        return (businessObject);
    }

    /**
    * Removes a Tournament from the persistent store.
    *
    * @param        pk                identity of object to remove
    * @exception    ProcessingException
    */
    public void deleteTournament(TournamentPrimaryKey pk)
        throws ProcessingException {
        Transaction tx = null;
        Session session = null;

        try {
            Tournament bo = findTournament(pk);

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
                    "TournamentDAO.deleteTournament - Hibernate failed to rollback - " +
                    exc1);
            }

            exc.printStackTrace();
            throw new ProcessingException(
                "TournamentDAO.deleteTournament failed - " + exc);
        } finally {
            try {
                session.flush();
                closeSession();
            } catch (Throwable exc) {
                LOGGER.info(
                    "TournamentDAO.deleteTournament - Hibernate failed to close the Session - " +
                    exc);
            }
        }
    }
}
