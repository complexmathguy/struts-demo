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
 * Implements Struts action processing for business entity Matchup.
 *
 * @author Dev Team
 */
public class MatchupAction extends BaseStrutsAction {
    private static final Logger LOGGER = Logger.getLogger(Matchup.class.getName());

    //************************************************************************    
    // Attributes
    //************************************************************************
    protected List<Matchup> matchupList = null;
    protected Matchup matchup = null;

    public Matchup getMatchup() {
        if (matchup == null) {
            matchup = new Matchup();
        }

        return (matchup);
    }

    // delegate access methods to underlying model, including primary keys
    public Long getMatchupId() {
        return getMatchup().getMatchupId();
    }

    public void setMatchupId(Long arg) {
        getMatchup().setMatchupId(arg);
    }

    public String getName() {
        return getMatchup().getName();
    }

    public void setName(String arg) {
        getMatchup().setName(arg);
    }

    public List getGridModel() {
        return (matchupList);
    }

    public List getList() {
        return (matchupList);
    }

    public Set<Game> getGames() {
        return (matchup.getGames());
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
     * Handles saving a Matchup BO by either creating a new one
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
     * Handles creating a Matchup BO
     *
     * @exception     Exception
     */
    public String create() throws Exception {
        try {
            Matchup matchup = getMatchup();

            // Create the Matchup by calling the 
            // create Matchup method on MatchupBusinessDelegate
            this.matchup = MatchupBusinessDelegate.getMatchupInstance()
                                                  .createMatchup(matchup);

            if (matchup != null) {
                LOGGER.info(
                    "MatchupAction:create() - successfully created Matchup - " +
                    matchup.toString());
            }
        } catch (Throwable exc) {
            LOGGER.severe("MatchupAction:create() - failed on Matchup - " +
                exc.getMessage());
            addMessage("failed to create Matchup");

            return ERROR;
        }

        return SUCCESS;
    }

    public String update() throws Exception {
        // store provided data
        Matchup tmp = getMatchup();

        // load actual data from db
        load();

        // copy provided data into actual data
        matchup.copyShallow(tmp);

        try {
            // create the MatchupBusiness Delegate            
            MatchupBusinessDelegate delegate = MatchupBusinessDelegate.getMatchupInstance();
            this.matchup = delegate.saveMatchup(matchup);

            if (this.matchup != null) {
                LOGGER.info(
                    "MatchupAction:update() - successfully updated Matchup - " +
                    matchup.toString());
            }
        } catch (Throwable exc) {
            LOGGER.severe(
                "MatchupAction:update() - failed to update Matchup - " +
                exc.getMessage());
            addMessage("failed to update Matchup with key " +
                matchup.getMatchupPrimaryKey().valuesAsCollection());

            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * Handles deleting a Matchup BO
     *
     * @exception       Exception
     */
    public String delete() throws Exception {
        try {
            MatchupBusinessDelegate delegate = MatchupBusinessDelegate.getMatchupInstance();

            if (getChildIds() == null) {
                delegate.delete(matchup.getMatchupPrimaryKey());
                LOGGER.info(
                    "MatchupAction:delete() - successfully deleted Matchup with key " +
                    matchup.getMatchupPrimaryKey().valuesAsCollection());
            } else {
                for (Long id : getChildIds()) {
                    delegate.delete(new MatchupPrimaryKey(id));
                }
            }
        } catch (Throwable exc) {
            LOGGER.info("MatchupAction:delete() - " + exc.getMessage());
            addMessage("failed to delete Matchup with key " +
                matchup.getMatchupPrimaryKey().valuesAsCollection());

            return ERROR;
        }

        return NONE;
    }

    /**
     * Handles loading a Matchup BO, overloaded from BaseStrutsAction
     *
     * @exception       Exception
     */
    public String load() throws Exception {
        MatchupPrimaryKey pk = null;

        try {
            Matchup matchup = getMatchup();

            if (hasPrimaryKey()) {
                pk = matchup.getMatchupPrimaryKey();
            }

            if (pk != null) {
                // load the Matchup
                this.matchup = MatchupBusinessDelegate.getMatchupInstance()
                                                      .getMatchup(pk);

                LOGGER.info("MatchupAction:load() - successfully loaded - " +
                    this.matchup.toString());
            } else {
                LOGGER.warning(
                    "MatchupAction:load() - unable to locate the primary key as an attribute or a selection for - " +
                    matchup.toString());
                addMessage("failed to load Matchup with key " +
                    pk.valuesAsCollection());

                return ERROR;
            }
        } catch (Throwable exc) {
            LOGGER.severe("MatchupAction:load() - failed to load - " +
                this.matchup.toString() + ", " + exc.getMessage());
            addMessage("failed to load Matchup with key " +
                pk.valuesAsCollection());

            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * Handles loading all Matchup BO
     *
     * @exception       ProcessingException
     */
    public String loadAll() throws Exception {
        try {
            // load the Matchup
            matchupList = MatchupBusinessDelegate.getMatchupInstance()
                                                 .getAllMatchup();

            if (matchupList != null) {
                LOGGER.info(
                    "MatchupAction:loadAllMatchup() - successfully loaded all Matchups");
            }

            paginate(matchupList);
        } catch (Throwable exc) {
            LOGGER.warning(
                "MatchupAction:loadAll() - failed to load all Matchups - " +
                exc.getMessage());
            addMessage("failed to loadAll Matchup");

            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * Returns true if the matchup is non-null and has it's primary key field(s) set
     */
    protected boolean hasPrimaryKey() {
        Matchup matchup = getMatchup();
        boolean hasPK = false;

        if ((matchup != null) &&
                (matchup.getMatchupPrimaryKey().hasBeenAssigned() == true)) {
            hasPK = true;
        }

        return (hasPK);
    }

    public String loadGames() throws Exception {
        String result = load();

        if (result == SUCCESS) {
            paginate(matchup.getGames());
        }

        return result;
    }

    public String saveGames() throws Exception {
        if (load() == ERROR) {
            return (ERROR);
        }

        GamePrimaryKey pk = null;
        Game child = null;
        GameBusinessDelegate childDelegate = GameBusinessDelegate.getGameInstance();
        MatchupBusinessDelegate parentDelegate = MatchupBusinessDelegate.getMatchupInstance();

        // creating or saving one 
        if (childId != null) {
            pk = new GamePrimaryKey(childId);

            // find the Game
            child = childDelegate.getGame(pk);
            // add it to the Games 
            matchup.getGames().add(child);
        } else {
            // clear out the Games but 
            matchup.getGames().clear();

            // finally, find each child and add it
            if (getChildIds() != null) {
                for (Long id : getChildIds()) {
                    pk = new GamePrimaryKey(id);
                    child = childDelegate.getGame(pk);

                    // add it to the Games List
                    matchup.getGames().add(child);
                }
            }
        }

        // save the Matchup
        parentDelegate.saveMatchup(matchup);

        // paginate it
        paginate(matchup.getGames());

        return NONE;
    }

    public String deleteGames() throws Exception {
        if (load() == ERROR) {
            return (ERROR);
        }

        if (getChildIds() != null) {
            GamePrimaryKey pk = null;
            GameBusinessDelegate childDelegate = GameBusinessDelegate.getGameInstance();
            MatchupBusinessDelegate parentDelegate = MatchupBusinessDelegate.getMatchupInstance();
            Set<Game> children = matchup.getGames();
            Game child = null;

            for (Long id : getChildIds()) {
                try {
                    pk = new GamePrimaryKey(id);

                    // first remove the relevant child from the list
                    child = childDelegate.getGame(pk);
                    children.remove(child);

                    // then safe to delete the child				
                    childDelegate.delete(pk);
                } catch (Exception exc) {
                    LOGGER.severe("MatchupAction:deleteGames() failed - " +
                        exc.getMessage());
                }
            }

            // assign the modified list of Game back to the matchup
            matchup.setGames(children);
            // save it 
            parentDelegate.saveMatchup(matchup);
        }

        return NONE;
    }
}
