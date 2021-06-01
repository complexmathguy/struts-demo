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
 * Implements Struts action processing for business entity League.
 *
 * @author Dev Team
 */
public class LeagueAction extends BaseStrutsAction {
    private static final Logger LOGGER = Logger.getLogger(League.class.getName());

    //************************************************************************    
    // Attributes
    //************************************************************************
    protected List<League> leagueList = null;
    protected League league = null;

    public League getLeague() {
        if (league == null) {
            league = new League();
        }

        return (league);
    }

    // delegate access methods to underlying model, including primary keys
    public Long getLeagueId() {
        return getLeague().getLeagueId();
    }

    public void setLeagueId(Long arg) {
        getLeague().setLeagueId(arg);
    }

    public String getName() {
        return getLeague().getName();
    }

    public void setName(String arg) {
        getLeague().setName(arg);
    }

    public List getGridModel() {
        return (leagueList);
    }

    public List getList() {
        return (leagueList);
    }

    public Set<Player> getPlayers() {
        return (league.getPlayers());
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
     * Handles saving a League BO by either creating a new one
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
     * Handles creating a League BO
     *
     * @exception     Exception
     */
    public String create() throws Exception {
        try {
            League league = getLeague();

            // Create the League by calling the 
            // create League method on LeagueBusinessDelegate
            this.league = LeagueBusinessDelegate.getLeagueInstance()
                                                .createLeague(league);

            if (league != null) {
                LOGGER.info(
                    "LeagueAction:create() - successfully created League - " +
                    league.toString());
            }
        } catch (Throwable exc) {
            LOGGER.severe("LeagueAction:create() - failed on League - " +
                exc.getMessage());
            addMessage("failed to create League");

            return ERROR;
        }

        return SUCCESS;
    }

    public String update() throws Exception {
        // store provided data
        League tmp = getLeague();

        // load actual data from db
        load();

        // copy provided data into actual data
        league.copyShallow(tmp);

        try {
            // create the LeagueBusiness Delegate            
            LeagueBusinessDelegate delegate = LeagueBusinessDelegate.getLeagueInstance();
            this.league = delegate.saveLeague(league);

            if (this.league != null) {
                LOGGER.info(
                    "LeagueAction:update() - successfully updated League - " +
                    league.toString());
            }
        } catch (Throwable exc) {
            LOGGER.severe("LeagueAction:update() - failed to update League - " +
                exc.getMessage());
            addMessage("failed to update League with key " +
                league.getLeaguePrimaryKey().valuesAsCollection());

            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * Handles deleting a League BO
     *
     * @exception       Exception
     */
    public String delete() throws Exception {
        try {
            LeagueBusinessDelegate delegate = LeagueBusinessDelegate.getLeagueInstance();

            if (getChildIds() == null) {
                delegate.delete(league.getLeaguePrimaryKey());
                LOGGER.info(
                    "LeagueAction:delete() - successfully deleted League with key " +
                    league.getLeaguePrimaryKey().valuesAsCollection());
            } else {
                for (Long id : getChildIds()) {
                    delegate.delete(new LeaguePrimaryKey(id));
                }
            }
        } catch (Throwable exc) {
            LOGGER.info("LeagueAction:delete() - " + exc.getMessage());
            addMessage("failed to delete League with key " +
                league.getLeaguePrimaryKey().valuesAsCollection());

            return ERROR;
        }

        return NONE;
    }

    /**
     * Handles loading a League BO, overloaded from BaseStrutsAction
     *
     * @exception       Exception
     */
    public String load() throws Exception {
        LeaguePrimaryKey pk = null;

        try {
            League league = getLeague();

            if (hasPrimaryKey()) {
                pk = league.getLeaguePrimaryKey();
            }

            if (pk != null) {
                // load the League
                this.league = LeagueBusinessDelegate.getLeagueInstance()
                                                    .getLeague(pk);

                LOGGER.info("LeagueAction:load() - successfully loaded - " +
                    this.league.toString());
            } else {
                LOGGER.warning(
                    "LeagueAction:load() - unable to locate the primary key as an attribute or a selection for - " +
                    league.toString());
                addMessage("failed to load League with key " +
                    pk.valuesAsCollection());

                return ERROR;
            }
        } catch (Throwable exc) {
            LOGGER.severe("LeagueAction:load() - failed to load - " +
                this.league.toString() + ", " + exc.getMessage());
            addMessage("failed to load League with key " +
                pk.valuesAsCollection());

            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * Handles loading all League BO
     *
     * @exception       ProcessingException
     */
    public String loadAll() throws Exception {
        try {
            // load the League
            leagueList = LeagueBusinessDelegate.getLeagueInstance()
                                               .getAllLeague();

            if (leagueList != null) {
                LOGGER.info(
                    "LeagueAction:loadAllLeague() - successfully loaded all Leagues");
            }

            paginate(leagueList);
        } catch (Throwable exc) {
            LOGGER.warning(
                "LeagueAction:loadAll() - failed to load all Leagues - " +
                exc.getMessage());
            addMessage("failed to loadAll League");

            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * Returns true if the league is non-null and has it's primary key field(s) set
     */
    protected boolean hasPrimaryKey() {
        League league = getLeague();
        boolean hasPK = false;

        if ((league != null) &&
                (league.getLeaguePrimaryKey().hasBeenAssigned() == true)) {
            hasPK = true;
        }

        return (hasPK);
    }

    public String loadPlayers() throws Exception {
        String result = load();

        if (result == SUCCESS) {
            paginate(league.getPlayers());
        }

        return result;
    }

    public String savePlayers() throws Exception {
        if (load() == ERROR) {
            return (ERROR);
        }

        PlayerPrimaryKey pk = null;
        Player child = null;
        PlayerBusinessDelegate childDelegate = PlayerBusinessDelegate.getPlayerInstance();
        LeagueBusinessDelegate parentDelegate = LeagueBusinessDelegate.getLeagueInstance();

        // creating or saving one 
        if (childId != null) {
            pk = new PlayerPrimaryKey(childId);

            // find the Player
            child = childDelegate.getPlayer(pk);
            // add it to the Players 
            league.getPlayers().add(child);
        } else {
            // clear out the Players but 
            league.getPlayers().clear();

            // finally, find each child and add it
            if (getChildIds() != null) {
                for (Long id : getChildIds()) {
                    pk = new PlayerPrimaryKey(id);
                    child = childDelegate.getPlayer(pk);

                    // add it to the Players List
                    league.getPlayers().add(child);
                }
            }
        }

        // save the League
        parentDelegate.saveLeague(league);

        // paginate it
        paginate(league.getPlayers());

        return NONE;
    }

    public String deletePlayers() throws Exception {
        if (load() == ERROR) {
            return (ERROR);
        }

        if (getChildIds() != null) {
            PlayerPrimaryKey pk = null;
            PlayerBusinessDelegate childDelegate = PlayerBusinessDelegate.getPlayerInstance();
            LeagueBusinessDelegate parentDelegate = LeagueBusinessDelegate.getLeagueInstance();
            Set<Player> children = league.getPlayers();
            Player child = null;

            for (Long id : getChildIds()) {
                try {
                    pk = new PlayerPrimaryKey(id);

                    // first remove the relevant child from the list
                    child = childDelegate.getPlayer(pk);
                    children.remove(child);

                    // then safe to delete the child				
                    childDelegate.delete(pk);
                } catch (Exception exc) {
                    LOGGER.severe("LeagueAction:deletePlayers() failed - " +
                        exc.getMessage());
                }
            }

            // assign the modified list of Player back to the league
            league.setPlayers(children);
            // save it 
            parentDelegate.saveLeague(league);
        }

        return NONE;
    }
}
