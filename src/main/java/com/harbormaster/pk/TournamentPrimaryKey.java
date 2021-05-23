/*******************************************************************************
  Turnstone Biologics Confidential

  2018 Turnstone Biologics
  All Rights Reserved.

  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.

  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.primarykey;

import com.harbormaster.primarykey.*;

import java.util.*;


/**
 * Tournament PrimaryKey class.
 *
 * @author    Dev Team
 */

// AIB : #getPrimaryKeyClassDecl() 
public class TournamentPrimaryKey extends BasePrimaryKey {
    //************************************************************************
    // Protected / Private Methods
    //************************************************************************

    //************************************************************************
    // Attributes
    //************************************************************************

    // DO NOT ASSIGN VALUES DIRECTLY TO THE FOLLOWING ATTRIBUTES.  SET THE VALUES
    // WITHIN THE Tournament class.

    // AIB : #getKeyFieldDeclarations()
    public Long tournamentId;

    // ~AIB

    //************************************************************************
    // Public Methods
    //************************************************************************

    /**
     * default constructor - should be normally used for dynamic instantiation
     */
    public TournamentPrimaryKey() {
    }

    /**
     * single value constructor
     */
    public TournamentPrimaryKey(Object tournamentId) {
        this.tournamentId = (tournamentId != null)
            ? new Long(tournamentId.toString()) : null;
    }

    //************************************************************************
    // Access Methods
    //************************************************************************

    // AIB : #getKeyFieldAccessMethods()
    /**
         * Returns the tournamentId.
         * @return    Long
     */
    public Long getTournamentId() {
        return (this.tournamentId);
    }

    /**
         * Assigns the tournamentId.
         * @return    Long
     */
    public void setTournamentId(Long id) {
        this.tournamentId = id;
    }

    // ~AIB 	         	     

    /**
     * Retrieves the value(s) as a single List
     * @return List
     */
    public List keys() {
        // assign the attributes to the Collection back to the parent
        ArrayList keys = new ArrayList();

        keys.add(tournamentId);

        return (keys);
    }

    public Object getFirstKey() {
        return (tournamentId);
    }

    // ~AIB 	        
}
