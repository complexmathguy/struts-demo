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
 * Encapsulates data for business entity Game.
 *
 * @author Dev Team
 */
@JsonIgnoreProperties(ignoreUnknown = true)
// AIB : #getBOClassDecl()
public class Game extends Base {
    // attributes

    // AIB : #getAttributeDeclarations( true  )
    protected Long gameId = null;
    protected Integer frames = new Integer("0");
    protected Matchup matchup = null;
    protected Player player = null;

    // ~AIB

    //************************************************************************
    // Constructors
    //************************************************************************

    /**
     * Default Constructor
     */
    public Game() {
    }

    //************************************************************************
    // Accessor Methods
    //************************************************************************

    /**
     * Returns the GamePrimaryKey
     * @return GamePrimaryKey
     */
    public GamePrimaryKey getGamePrimaryKey() {
        GamePrimaryKey key = new GamePrimaryKey();
        key.setGameId(this.gameId);

        return (key);
    }

    // AIB : #getBOAccessorMethods(true)
    /**
    * Returns the frames
    * @return Integer
    */
    public Integer getFrames() {
        return this.frames;
    }

    /**
                  * Assigns the frames
        * @param frames        Integer
        */
    public void setFrames(Integer frames) {
        this.frames = frames;
    }

    /**
    * Returns the Matchup
    * @return Matchup
    */
    public Matchup getMatchup() {
        return this.matchup;
    }

    /**
                  * Assigns the matchup
        * @param matchup        Matchup
        */
    public void setMatchup(Matchup matchup) {
        this.matchup = matchup;
    }

    /**
    * Returns the Player
    * @return Player
    */
    public Player getPlayer() {
        return this.player;
    }

    /**
                  * Assigns the player
        * @param player        Player
        */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
    * Returns the gameId
    * @return Long
    */
    public Long getGameId() {
        return this.gameId;
    }

    /**
                  * Assigns the gameId
        * @param gameId        Long
        */
    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    // ~AIB

    /**
     * Performs a shallow copy.
     * @param object         Game                copy source
     * @exception IllegalArgumentException         Thrown if the passed in obj is null. It is also
     *                                                         thrown if the passed in businessObject is not of the correct type.
     */
    public Game copyShallow(Game object) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(
                " Game:copy(..) - object cannot be null.");
        }

        // Call base class copy
        super.copy(object);

        // Set member attributes

        // AIB : #getCopyString( false )
        this.gameId = object.getGameId();
        this.frames = object.getFrames();

        // ~AIB 
        return this;
    }

    /**
     * Performs a deep copy.
     * @param object         Game                copy source
     * @exception IllegalArgumentException         Thrown if the passed in obj is null. It is also
     *                                                         thrown if the passed in businessObject is not of the correct type.
     */
    public Game copy(Game object) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(
                " Game:copy(..) - object cannot be null.");
        }

        // Call base class copy
        super.copy(object);

        copyShallow(object);

        // Set member attributes

        // AIB : #getCopyString( true )
        if (object.getMatchup() != null) {
            this.matchup = new Matchup();
            this.matchup.copyShallow(object.getMatchup());
        } else {
            this.matchup = null;
        }

        if (object.getPlayer() != null) {
            this.player = new Player();
            this.player.copyShallow(object.getPlayer());
        } else {
            this.player = null;
        }

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
        returnString.append("gameId = " + this.gameId + ", ");
        returnString.append("frames = " + this.frames + ", ");

        // ~AIB 
        return returnString.toString();
    }

    public java.util.Collection<String> getAttributesByNameUserIdentifiesBy() {
        Collection<String> names = new java.util.ArrayList<String>();

        return (names);
    }

    public String getIdentity() {
        StringBuilder identity = new StringBuilder("Game");

        identity.append("::");
        identity.append(gameId);

        return (identity.toString());
    }

    public String getObjectType() {
        return ("Game");
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

        if (!(object instanceof Game)) {
            return false;
        }

        Game bo = (Game) object;

        return (getGamePrimaryKey().equals(bo.getGamePrimaryKey()));
    }

    // ~AIB
}
