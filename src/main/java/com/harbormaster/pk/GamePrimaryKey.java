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
 * Game PrimaryKey class.
 *
 * @author    Dev Team
 */

// AIB : #getPrimaryKeyClassDecl() 
public class GamePrimaryKey extends BasePrimaryKey {
    //************************************************************************
    // Protected / Private Methods
    //************************************************************************

    //************************************************************************
    // Attributes
    //************************************************************************

    // DO NOT ASSIGN VALUES DIRECTLY TO THE FOLLOWING ATTRIBUTES.  SET THE VALUES
    // WITHIN THE Game class.

    // AIB : #getKeyFieldDeclarations()
    public Long gameId;

    // ~AIB

    //************************************************************************
    // Public Methods
    //************************************************************************

    /**
     * default constructor - should be normally used for dynamic instantiation
     */
    public GamePrimaryKey() {
    }

    /**
     * single value constructor
     */
    public GamePrimaryKey(Object gameId) {
        this.gameId = (gameId != null) ? new Long(gameId.toString()) : null;
    }

    //************************************************************************
    // Access Methods
    //************************************************************************

    // AIB : #getKeyFieldAccessMethods()
    /**
         * Returns the gameId.
         * @return    Long
     */
    public Long getGameId() {
        return (this.gameId);
    }

    /**
         * Assigns the gameId.
         * @return    Long
     */
    public void setGameId(Long id) {
        this.gameId = id;
    }

    // ~AIB 	         	     

    /**
     * Retrieves the value(s) as a single List
     * @return List
     */
    public List keys() {
        // assign the attributes to the Collection back to the parent
        ArrayList keys = new ArrayList();

        keys.add(gameId);

        return (keys);
    }

    public Object getFirstKey() {
        return (gameId);
    }

    // ~AIB 	        
}
