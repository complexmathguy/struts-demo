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
 * Matchup PrimaryKey class.
 *
 * @author    Dev Team
 */

// AIB : #getPrimaryKeyClassDecl() 
public class MatchupPrimaryKey extends BasePrimaryKey {
    //************************************************************************
    // Protected / Private Methods
    //************************************************************************

    //************************************************************************
    // Attributes
    //************************************************************************

    // DO NOT ASSIGN VALUES DIRECTLY TO THE FOLLOWING ATTRIBUTES.  SET THE VALUES
    // WITHIN THE Matchup class.

    // AIB : #getKeyFieldDeclarations()
    public Long matchupId;

    // ~AIB

    //************************************************************************
    // Public Methods
    //************************************************************************

    /**
     * default constructor - should be normally used for dynamic instantiation
     */
    public MatchupPrimaryKey() {
    }

    /**
     * single value constructor
     */
    public MatchupPrimaryKey(Object matchupId) {
        this.matchupId = (matchupId != null) ? new Long(matchupId.toString())
                                             : null;
    }

    //************************************************************************
    // Access Methods
    //************************************************************************

    // AIB : #getKeyFieldAccessMethods()
    /**
         * Returns the matchupId.
         * @return    Long
     */
    public Long getMatchupId() {
        return (this.matchupId);
    }

    /**
         * Assigns the matchupId.
         * @return    Long
     */
    public void setMatchupId(Long id) {
        this.matchupId = id;
    }

    // ~AIB 	         	     

    /**
     * Retrieves the value(s) as a single List
     * @return List
     */
    public List keys() {
        // assign the attributes to the Collection back to the parent
        ArrayList keys = new ArrayList();

        keys.add(matchupId);

        return (keys);
    }

    public Object getFirstKey() {
        return (matchupId);
    }

    // ~AIB 	        
}
