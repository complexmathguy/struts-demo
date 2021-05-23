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

import com.harbormaster.persistent.FrameworkPersistenceHelper;

import org.hibernate.*;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Base class for all Hibernate DAO classes generated and supported
 * by the framework.
 *
 * @author        Harbormaster, Inc.
 */
public class FrameworkHibernateDAO {
    // attributes
    private static final Logger LOGGER = Logger.getLogger(FrameworkHibernateDAO.class.getName());
    public static final ThreadLocal session = new ThreadLocal();
    protected Session externalSession = null;
    protected Transaction externalTransaction = null;

    public static SessionFactory getSessionFactory() {
        return (FrameworkPersistenceHelper.self().getSessionFactory());
    }

    public Session currentSession() throws HibernateException {
        if (externalSession != null) {
            return (externalSession);
        } else {
            Session s = (Session) session.get();

            // Open a new Session, if this Thread has none yet or the session is closed or not connected
            if ((s == null) || !s.isOpen() || !s.isConnected()) {
                // Note: dynamically create the class Interceptor and apply here,			
                // if one is in use ...			
                s = getSessionFactory().openSession( /*getAuditTrailInterceptor() */
                    );
                session.set(s);
                s.setFlushMode(FlushMode.COMMIT);
            }

            return s;
        }
    }

    public void closeSession() throws HibernateException {
        if (externalSession == null) {
            Session s = (Session) session.get();
            session.set(null);

            if (s != null) {
                s.close();
            }
        }
    }

    protected void commitTransaction(Transaction transaction)
        throws HibernateException {
        if (externalTransaction != transaction) {
            transaction.commit();
        }

        // do nothing otherwise since the transaction is external to us...
    }

    protected void rollbackTransaction(Transaction transaction)
        throws HibernateException {
        if (externalTransaction != transaction) {
            transaction.rollback();
        }

        // do nothing otherwise since the transaction is external to us...
    }

    protected Transaction currentTransaction(Session s)
        throws HibernateException {
        if (externalTransaction != null) {
            return (externalTransaction);
        } else {
            return (s.beginTransaction());
        }
    }

    public void assignExternalSession(Session session) {
        externalSession = session;
    }

    public void assignExternalTransaction(Transaction transaction) {
        externalTransaction = transaction;
    }
}
