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
 * Implements Struts action processing for business entity Tournament.
 *
 * @author Dev Team
 */
public class TournamentAction extends BaseStrutsAction {
    private static final Logger LOGGER = Logger.getLogger(Tournament.class.getName());

    //************************************************************************    
    // Attributes
    //************************************************************************
    protected List<Tournament> tournamentList = null;
    protected Tournament tournament = null;

    public Tournament getTournament() {
        if (tournament == null) {
            tournament = new Tournament();
        }

        return (tournament);
    }

    // delegate access methods to underlying model, including primary keys
    public Long getTournamentId() {
        return getTournament().getTournamentId();
    }

    public void setTournamentId(Long arg) {
        getTournament().setTournamentId(arg);
    }

    public String getName() {
        return getTournament().getName();
    }

    public void setName(String arg) {
        getTournament().setName(arg);
    }

    public TournamentType getType() {
        return getTournament().getType();
    }

    public void setType(TournamentType arg) {
        getTournament().setType(arg);
    }

    public List getGridModel() {
        return (tournamentList);
    }

    public List getList() {
        return (tournamentList);
    }

    public Set<Matchup> getMatchups() {
        return (tournament.getMatchups());
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
     * Handles saving a Tournament BO by either creating a new one
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
     * Handles creating a Tournament BO
     *
     * @exception     Exception
     */
    public String create() throws Exception {
        try {
            Tournament tournament = getTournament();

            // Create the Tournament by calling the 
            // create Tournament method on TournamentBusinessDelegate
            this.tournament = TournamentBusinessDelegate.getTournamentInstance()
                                                        .createTournament(tournament);

            if (tournament != null) {
                LOGGER.info(
                    "TournamentAction:create() - successfully created Tournament - " +
                    tournament.toString());
            }
        } catch (Throwable exc) {
            LOGGER.severe("TournamentAction:create() - failed on Tournament - " +
                exc.getMessage());
            addMessage("failed to create Tournament");

            return ERROR;
        }

        return SUCCESS;
    }

    public String update() throws Exception {
        // store provided data
        Tournament tmp = getTournament();

        // load actual data from db
        load();

        // copy provided data into actual data
        tournament.copyShallow(tmp);

        try {
            // create the TournamentBusiness Delegate            
            TournamentBusinessDelegate delegate = TournamentBusinessDelegate.getTournamentInstance();
            this.tournament = delegate.saveTournament(tournament);

            if (this.tournament != null) {
                LOGGER.info(
                    "TournamentAction:update() - successfully updated Tournament - " +
                    tournament.toString());
            }
        } catch (Throwable exc) {
            LOGGER.severe(
                "TournamentAction:update() - failed to update Tournament - " +
                exc.getMessage());
            addMessage("failed to update Tournament with key " +
                tournament.getTournamentPrimaryKey().valuesAsCollection());

            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * Handles deleting a Tournament BO
     *
     * @exception       Exception
     */
    public String delete() throws Exception {
        try {
            TournamentBusinessDelegate delegate = TournamentBusinessDelegate.getTournamentInstance();

            if (getChildIds() == null) {
                delegate.delete(tournament.getTournamentPrimaryKey());
                LOGGER.info(
                    "TournamentAction:delete() - successfully deleted Tournament with key " +
                    tournament.getTournamentPrimaryKey().valuesAsCollection());
            } else {
                for (Long id : getChildIds()) {
                    delegate.delete(new TournamentPrimaryKey(id));
                }
            }
        } catch (Throwable exc) {
            LOGGER.info("TournamentAction:delete() - " + exc.getMessage());
            addMessage("failed to delete Tournament with key " +
                tournament.getTournamentPrimaryKey().valuesAsCollection());

            return ERROR;
        }

        return NONE;
    }

    /**
     * Handles loading a Tournament BO, overloaded from BaseStrutsAction
     *
     * @exception       Exception
     */
    public String load() throws Exception {
        TournamentPrimaryKey pk = null;

        try {
            Tournament tournament = getTournament();

            if (hasPrimaryKey()) {
                pk = tournament.getTournamentPrimaryKey();
            }

            if (pk != null) {
                // load the Tournament
                this.tournament = TournamentBusinessDelegate.getTournamentInstance()
                                                            .getTournament(pk);

                LOGGER.info("TournamentAction:load() - successfully loaded - " +
                    this.tournament.toString());
            } else {
                LOGGER.warning(
                    "TournamentAction:load() - unable to locate the primary key as an attribute or a selection for - " +
                    tournament.toString());
                addMessage("failed to load Tournament with key " +
                    pk.valuesAsCollection());

                return ERROR;
            }
        } catch (Throwable exc) {
            LOGGER.severe("TournamentAction:load() - failed to load - " +
                this.tournament.toString() + ", " + exc.getMessage());
            addMessage("failed to load Tournament with key " +
                pk.valuesAsCollection());

            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * Handles loading all Tournament BO
     *
     * @exception       ProcessingException
     */
    public String loadAll() throws Exception {
        try {
            // load the Tournament
            tournamentList = TournamentBusinessDelegate.getTournamentInstance()
                                                       .getAllTournament();

            if (tournamentList != null) {
                LOGGER.info(
                    "TournamentAction:loadAllTournament() - successfully loaded all Tournaments");
            }

            paginate(tournamentList);
        } catch (Throwable exc) {
            LOGGER.warning(
                "TournamentAction:loadAll() - failed to load all Tournaments - " +
                exc.getMessage());
            addMessage("failed to loadAll Tournament");

            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * Returns true if the tournament is non-null and has it's primary key field(s) set
     */
    protected boolean hasPrimaryKey() {
        Tournament tournament = getTournament();
        boolean hasPK = false;

        if ((tournament != null) &&
                (tournament.getTournamentPrimaryKey().hasBeenAssigned() == true)) {
            hasPK = true;
        }

        return (hasPK);
    }

    public String loadMatchups() throws Exception {
        String result = load();

        if (result == SUCCESS) {
            paginate(tournament.getMatchups());
        }

        return result;
    }

    public String saveMatchups() throws Exception {
        if (load() == ERROR) {
            return (ERROR);
        }

        MatchupPrimaryKey pk = null;
        Matchup child = null;
        MatchupBusinessDelegate childDelegate = MatchupBusinessDelegate.getMatchupInstance();
        TournamentBusinessDelegate parentDelegate = TournamentBusinessDelegate.getTournamentInstance();

        // creating or saving one 
        if (childId != null) {
            pk = new MatchupPrimaryKey(childId);

            // find the Matchup
            child = childDelegate.getMatchup(pk);
            // add it to the Matchups 
            tournament.getMatchups().add(child);
        } else {
            // clear out the Matchups but 
            tournament.getMatchups().clear();

            // finally, find each child and add it
            if (getChildIds() != null) {
                for (Long id : getChildIds()) {
                    pk = new MatchupPrimaryKey(id);
                    child = childDelegate.getMatchup(pk);

                    // add it to the Matchups List
                    tournament.getMatchups().add(child);
                }
            }
        }

        // save the Tournament
        parentDelegate.saveTournament(tournament);

        // paginate it
        paginate(tournament.getMatchups());

        return NONE;
    }

    public String deleteMatchups() throws Exception {
        if (load() == ERROR) {
            return (ERROR);
        }

        if (getChildIds() != null) {
            MatchupPrimaryKey pk = null;
            MatchupBusinessDelegate childDelegate = MatchupBusinessDelegate.getMatchupInstance();
            TournamentBusinessDelegate parentDelegate = TournamentBusinessDelegate.getTournamentInstance();
            Set<Matchup> children = tournament.getMatchups();
            Matchup child = null;

            for (Long id : getChildIds()) {
                try {
                    pk = new MatchupPrimaryKey(id);

                    // first remove the relevant child from the list
                    child = childDelegate.getMatchup(pk);
                    children.remove(child);

                    // then safe to delete the child				
                    childDelegate.delete(pk);
                } catch (Exception exc) {
                    LOGGER.severe("TournamentAction:deleteMatchups() failed - " +
                        exc.getMessage());
                }
            }

            // assign the modified list of Matchup back to the tournament
            tournament.setMatchups(children);
            // save it 
            parentDelegate.saveTournament(tournament);
        }

        return NONE;
    }
}
