/*******************************************************************************
  Turnstone Biologics Confidential

  2018 Turnstone Biologics
  All Rights Reserved.

  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.

  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.harbormaster.bo.*;

import com.harbormaster.primarykey.*;

import java.util.*;


/**
 * Encapsulates data for business entity Player.
 *
 * @author Dev Team
 */
@JsonIgnoreProperties(ignoreUnknown = true)
// AIB : #getBOClassDecl()
public class Player extends Base {
    // attributes

    // AIB : #getAttributeDeclarations( true  )
    protected Long playerId = null;
    protected String name = null;
    protected Date dateOfBirth = null;
    protected Double height = new Double("0.0");
    protected Boolean isProfessional = new Boolean("false");

    // ~AIB

    //************************************************************************
    // Constructors
    //************************************************************************

    /**
     * Default Constructor
     */
    public Player() {
    }

    //************************************************************************
    // Accessor Methods
    //************************************************************************

    /**
     * Returns the PlayerPrimaryKey
     * @return PlayerPrimaryKey
     */
    public PlayerPrimaryKey getPlayerPrimaryKey() {
        PlayerPrimaryKey key = new PlayerPrimaryKey();
        key.setPlayerId(this.playerId);

        return (key);
    }

    // AIB : #getBOAccessorMethods(true)
    /**
    * Returns the name
    * @return String
    */
    public String getName() {
        return this.name;
    }

    /**
                  * Assigns the name
        * @param name        String
        */
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Returns the dateOfBirth
    * @return Date
    */
    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    /**
                  * Assigns the dateOfBirth
        * @param dateOfBirth        Date
        */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
    * Returns the height
    * @return Double
    */
    public Double getHeight() {
        return this.height;
    }

    /**
                  * Assigns the height
        * @param height        Double
        */
    public void setHeight(Double height) {
        this.height = height;
    }

    /**
    * Returns the isProfessional
    * @return Boolean
    */
    public Boolean getIsProfessional() {
        return this.isProfessional;
    }

    /**
                  * Assigns the isProfessional
        * @param isProfessional        Boolean
        */
    public void setIsProfessional(Boolean isProfessional) {
        this.isProfessional = isProfessional;
    }

    /**
    * Returns the playerId
    * @return Long
    */
    public Long getPlayerId() {
        return this.playerId;
    }

    /**
                  * Assigns the playerId
        * @param playerId        Long
        */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    // ~AIB

    /**
     * Performs a shallow copy.
     * @param object         Player                copy source
     * @exception IllegalArgumentException         Thrown if the passed in obj is null. It is also
     *                                                         thrown if the passed in businessObject is not of the correct type.
     */
    public Player copyShallow(Player object) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(
                " Player:copy(..) - object cannot be null.");
        }

        // Call base class copy
        super.copy(object);

        // Set member attributes

        // AIB : #getCopyString( false )
        this.playerId = object.getPlayerId();
        this.name = object.getName();
        this.dateOfBirth = object.getDateOfBirth();
        this.height = object.getHeight();
        this.isProfessional = object.getIsProfessional();

        // ~AIB 
        return this;
    }

    /**
     * Performs a deep copy.
     * @param object         Player                copy source
     * @exception IllegalArgumentException         Thrown if the passed in obj is null. It is also
     *                                                         thrown if the passed in businessObject is not of the correct type.
     */
    public Player copy(Player object) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(
                " Player:copy(..) - object cannot be null.");
        }

        // Call base class copy
        super.copy(object);

        copyShallow(object);

        // Set member attributes

        // AIB : #getCopyString( true )
        // ~AIB 
        return (this);
    }

    /**
     * Returns a string representation of the object.
     * @return String
     */
    public String toString() {
        StringBuilder returnString = new StringBuilder();

        returnString.append(super.toString() + ", ");

        // AIB : #getToString( false )
        returnString.append("playerId = " + this.playerId + ", ");
        returnString.append("name = " + this.name + ", ");
        returnString.append("dateOfBirth = " + this.dateOfBirth + ", ");
        returnString.append("height = " + this.height + ", ");
        returnString.append("isProfessional = " + this.isProfessional + ", ");

        // ~AIB 
        return returnString.toString();
    }

    public java.util.Collection<String> getAttributesByNameUserIdentifiesBy() {
        Collection<String> names = new java.util.ArrayList<String>();

        return (names);
    }

    public String getIdentity() {
        StringBuilder identity = new StringBuilder("Player");

        identity.append("::");
        identity.append(playerId);

        return (identity.toString());
    }

    public String getObjectType() {
        return ("Player");
    }

    //************************************************************************
    // Object Overloads
    //************************************************************************
    public boolean equals(Object object) {
        Object tmpObject = null;

        if (this == object) {
            return true;
        }

        if (object == null) {
            return false;
        }

        if (!(object instanceof Player)) {
            return false;
        }

        Player bo = (Player) object;

        return (getPlayerPrimaryKey().equals(bo.getPlayerPrimaryKey()));
    }

    // ~AIB
}
