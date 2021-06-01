/*******************************************************************************
  Turnstone Biologics Confidential

  2018 Turnstone Biologics
  All Rights Reserved.

  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.

  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.action;

import com.harbormaster.action.*;

import com.harbormaster.bo.*;

import com.harbormaster.delegate.*;

import com.harbormaster.exception.*;

import com.harbormaster.primarykey.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.*;


/**
 * Implements Struts action processing for business entity Player.
 *
 * @author Dev Team
 */
public class PlayerAction extends BaseStrutsAction {
    private static final Logger LOGGER = Logger.getLogger(Player.class.getName());

    //************************************************************************    
    // Attributes
    //************************************************************************
    protected List<Player> playerList = null;
    protected Player player = null;

    public Player getPlayer() {
        if (player == null) {
            player = new Player();
        }

        return (player);
    }

    // delegate access methods to underlying model, including primary keys
    public Long getPlayerId() {
        return getPlayer().getPlayerId();
    }

    public void setPlayerId(Long arg) {
        getPlayer().setPlayerId(arg);
    }

    public String getName() {
        return getPlayer().getName();
    }

    public void setName(String arg) {
        getPlayer().setName(arg);
    }

    public Date getDateOfBirth() {
        return getPlayer().getDateOfBirth();
    }

    public void setDateOfBirth(Date arg) {
        getPlayer().setDateOfBirth(arg);
    }

    public Double getHeight() {
        return getPlayer().getHeight();
    }

    public void setHeight(Double arg) {
        getPlayer().setHeight(arg);
    }

    public Boolean getIsProfessional() {
        return getPlayer().getIsProfessional();
    }

    public void setIsProfessional(Boolean arg) {
        getPlayer().setIsProfessional(arg);
    }

    public List getGridModel() {
        return (playerList);
    }

    public List getList() {
        return (playerList);
    }

    public String executeGridAction() throws Exception {
        String result = "error";
        String operator = getOper();

        if (operator != null) {
            if (operator.equalsIgnoreCase("add") ||
                    operator.equalsIgnoreCase("edit")) {
                result = save();
            } else if (operator.equalsIgnoreCase("del")) {
                result = delete();
            }
        }

        return (result);
    }

    /**
     * Handles saving a Player BO by either creating a new one
     * or updating an existing one.  The lack of or presense of a primary key field is
     * the difference
     *
     * @exception     Exception
     */
    public String save() throws Exception {
        if (hasPrimaryKey()) {
            return (update());
        } else {
            return (create());
        }
    }

    /**
     * Handles creating a Player BO
     *
     * @exception     Exception
     */
    public String create() throws Exception {
        try {
            Player player = getPlayer();

            // Create the Player by calling the 
            // create Player method on PlayerBusinessDelegate
            this.player = PlayerBusinessDelegate.getPlayerInstance()
                                                .createPlayer(player);

            if (player != null) {
                LOGGER.info(
                    "PlayerAction:create() - successfully created Player - " +
                    player.toString());
            }
        } catch (Throwable exc) {
            LOGGER.severe("PlayerAction:create() - failed on Player - " +
                exc.getMessage());
            addMessage("failed to create Player");

            return ERROR;
        }

        return SUCCESS;
    }

    public String update() throws Exception {
        // store provided data
        Player tmp = getPlayer();

        // load actual data from db
        load();

        // copy provided data into actual data
        player.copyShallow(tmp);

        try {
            // create the PlayerBusiness Delegate            
            PlayerBusinessDelegate delegate = PlayerBusinessDelegate.getPlayerInstance();
            this.player = delegate.savePlayer(player);

            if (this.player != null) {
                LOGGER.info(
                    "PlayerAction:update() - successfully updated Player - " +
                    player.toString());
            }
        } catch (Throwable exc) {
            LOGGER.severe("PlayerAction:update() - failed to update Player - " +
                exc.getMessage());
            addMessage("failed to update Player with key " +
                player.getPlayerPrimaryKey().valuesAsCollection());

            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * Handles deleting a Player BO
     *
     * @exception       Exception
     */
    public String delete() throws Exception {
        try {
            PlayerBusinessDelegate delegate = PlayerBusinessDelegate.getPlayerInstance();

            if (getChildIds() == null) {
                delegate.delete(player.getPlayerPrimaryKey());
                LOGGER.info(
                    "PlayerAction:delete() - successfully deleted Player with key " +
                    player.getPlayerPrimaryKey().valuesAsCollection());
            } else {
                for (Long id : getChildIds()) {
                    delegate.delete(new PlayerPrimaryKey(id));
                }
            }
        } catch (Throwable exc) {
            LOGGER.info("PlayerAction:delete() - " + exc.getMessage());
            addMessage("failed to delete Player with key " +
                player.getPlayerPrimaryKey().valuesAsCollection());

            return ERROR;
        }

        return NONE;
    }

    /**
     * Handles loading a Player BO, overloaded from BaseStrutsAction
     *
     * @exception       Exception
     */
    public String load() throws Exception {
        PlayerPrimaryKey pk = null;

        try {
            Player player = getPlayer();

            if (hasPrimaryKey()) {
                pk = player.getPlayerPrimaryKey();
            }

            if (pk != null) {
                // load the Player
                this.player = PlayerBusinessDelegate.getPlayerInstance()
                                                    .getPlayer(pk);

                LOGGER.info("PlayerAction:load() - successfully loaded - " +
                    this.player.toString());
            } else {
                LOGGER.warning(
                    "PlayerAction:load() - unable to locate the primary key as an attribute or a selection for - " +
                    player.toString());
                addMessage("failed to load Player with key " +
                    pk.valuesAsCollection());

                return ERROR;
            }
        } catch (Throwable exc) {
            LOGGER.severe("PlayerAction:load() - failed to load - " +
                this.player.toString() + ", " + exc.getMessage());
            addMessage("failed to load Player with key " +
                pk.valuesAsCollection());

            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * Handles loading all Player BO
     *
     * @exception       ProcessingException
     */
    public String loadAll() throws Exception {
        try {
            // load the Player
            playerList = PlayerBusinessDelegate.getPlayerInstance()
                                               .getAllPlayer();

            if (playerList != null) {
                LOGGER.info(
                    "PlayerAction:loadAllPlayer() - successfully loaded all Players");
            }

            paginate(playerList);
        } catch (Throwable exc) {
            LOGGER.warning(
                "PlayerAction:loadAll() - failed to load all Players - " +
                exc.getMessage());
            addMessage("failed to loadAll Player");

            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * Returns true if the player is non-null and has it's primary key field(s) set
     */
    protected boolean hasPrimaryKey() {
        Player player = getPlayer();
        boolean hasPK = false;

        if ((player != null) &&
                (player.getPlayerPrimaryKey().hasBeenAssigned() == true)) {
            hasPK = true;
        }

        return (hasPK);
    }
}
