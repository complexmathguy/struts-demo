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
 * Implements Struts action processing for business entity Game.
 *
 * @author Dev Team
 */
public class GameAction extends BaseStrutsAction {
    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    //************************************************************************    
    // Attributes
    //************************************************************************
    protected List<Game> gameList = null;
    protected Game game = null;

    public Game getGame() {
        if (game == null) {
            game = new Game();
        }

        return (game);
    }

    // delegate access methods to underlying model, including primary keys
    public Long getGameId() {
        return getGame().getGameId();
    }

    public void setGameId(Long arg) {
        getGame().setGameId(arg);
    }

    public Integer getFrames() {
        return getGame().getFrames();
    }

    public void setFrames(Integer arg) {
        getGame().setFrames(arg);
    }

    public List getGridModel() {
        return (gameList);
    }

    public List getList() {
        return (gameList);
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
     * Handles saving a Game BO by either creating a new one
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
     * Handles creating a Game BO
     *
     * @exception     Exception
     */
    public String create() throws Exception {
        try {
            Game game = getGame();

            // Create the Game by calling the 
            // create Game method on GameBusinessDelegate
            this.game = GameBusinessDelegate.getGameInstance().createGame(game);

            if (game != null) {
                LOGGER.info(
                    "GameAction:create() - successfully created Game - " +
                    game.toString());
            }
        } catch (Throwable exc) {
            LOGGER.severe("GameAction:create() - failed on Game - " +
                exc.getMessage());
            addMessage("failed to create Game");

            return ERROR;
        }

        return SUCCESS;
    }

    public String update() throws Exception {
        // store provided data
        Game tmp = getGame();

        // load actual data from db
        load();

        // copy provided data into actual data
        game.copyShallow(tmp);

        try {
            // create the GameBusiness Delegate            
            GameBusinessDelegate delegate = GameBusinessDelegate.getGameInstance();
            this.game = delegate.saveGame(game);

            if (this.game != null) {
                LOGGER.info(
                    "GameAction:update() - successfully updated Game - " +
                    game.toString());
            }
        } catch (Throwable exc) {
            LOGGER.severe("GameAction:update() - failed to update Game - " +
                exc.getMessage());
            addMessage("failed to update Game with key " +
                game.getGamePrimaryKey().valuesAsCollection());

            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * Handles deleting a Game BO
     *
     * @exception       Exception
     */
    public String delete() throws Exception {
        try {
            GameBusinessDelegate delegate = GameBusinessDelegate.getGameInstance();

            if (getChildIds() == null) {
                delegate.delete(game.getGamePrimaryKey());
                LOGGER.info(
                    "GameAction:delete() - successfully deleted Game with key " +
                    game.getGamePrimaryKey().valuesAsCollection());
            } else {
                for (Long id : getChildIds()) {
                    delegate.delete(new GamePrimaryKey(id));
                }
            }
        } catch (Throwable exc) {
            LOGGER.info("GameAction:delete() - " + exc.getMessage());
            addMessage("failed to delete Game with key " +
                game.getGamePrimaryKey().valuesAsCollection());

            return ERROR;
        }

        return NONE;
    }

    /**
     * Handles loading a Game BO, overloaded from BaseStrutsAction
     *
     * @exception       Exception
     */
    public String load() throws Exception {
        GamePrimaryKey pk = null;

        try {
            Game game = getGame();

            if (hasPrimaryKey()) {
                pk = game.getGamePrimaryKey();
            }

            if (pk != null) {
                // load the Game
                this.game = GameBusinessDelegate.getGameInstance().getGame(pk);

                LOGGER.info("GameAction:load() - successfully loaded - " +
                    this.game.toString());
            } else {
                LOGGER.warning(
                    "GameAction:load() - unable to locate the primary key as an attribute or a selection for - " +
                    game.toString());
                addMessage("failed to load Game with key " +
                    pk.valuesAsCollection());

                return ERROR;
            }
        } catch (Throwable exc) {
            LOGGER.severe("GameAction:load() - failed to load - " +
                this.game.toString() + ", " + exc.getMessage());
            addMessage("failed to load Game with key " +
                pk.valuesAsCollection());

            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * Handles loading all Game BO
     *
     * @exception       ProcessingException
     */
    public String loadAll() throws Exception {
        try {
            // load the Game
            gameList = GameBusinessDelegate.getGameInstance().getAllGame();

            if (gameList != null) {
                LOGGER.info(
                    "GameAction:loadAllGame() - successfully loaded all Games");
            }

            paginate(gameList);
        } catch (Throwable exc) {
            LOGGER.warning("GameAction:loadAll() - failed to load all Games - " +
                exc.getMessage());
            addMessage("failed to loadAll Game");

            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * Returns true if the game is non-null and has it's primary key field(s) set
     */
    protected boolean hasPrimaryKey() {
        Game game = getGame();
        boolean hasPK = false;

        if ((game != null) &&
                (game.getGamePrimaryKey().hasBeenAssigned() == true)) {
            hasPK = true;
        }

        return (hasPK);
    }

    public String saveMatchup() throws Exception {
        if (load() == ERROR) {
            return (ERROR);
        }

        if (childId != null) {
            MatchupBusinessDelegate childDelegate = MatchupBusinessDelegate.getMatchupInstance();
            GameBusinessDelegate parentDelegate = GameBusinessDelegate.getGameInstance();
            Matchup child = null;

            child = childDelegate.getMatchup(new MatchupPrimaryKey(childId));

            game.setMatchup(child);

            // save it
            parentDelegate.saveGame(game);
        }

        return NONE;
    }

    public String deleteMatchup() throws Exception {
        if (load() == ERROR) {
            return (ERROR);
        }

        if (game.getMatchup() != null) {
            MatchupPrimaryKey pk = game.getMatchup().getMatchupPrimaryKey();

            // null out the parent first so there's no constraint during deletion
            game.setMatchup(null);

            GameBusinessDelegate parentDelegate = GameBusinessDelegate.getGameInstance();

            parentDelegate.saveGame(game);

            // safe to delete the child
            MatchupBusinessDelegate childDelegate = MatchupBusinessDelegate.getMatchupInstance();
            childDelegate.delete(pk);
        }

        return NONE;
    }

    public String savePlayer() throws Exception {
        if (load() == ERROR) {
            return (ERROR);
        }

        if (childId != null) {
            PlayerBusinessDelegate childDelegate = PlayerBusinessDelegate.getPlayerInstance();
            GameBusinessDelegate parentDelegate = GameBusinessDelegate.getGameInstance();
            Player child = null;

            child = childDelegate.getPlayer(new PlayerPrimaryKey(childId));

            game.setPlayer(child);

            // save it
            parentDelegate.saveGame(game);
        }

        return NONE;
    }

    public String deletePlayer() throws Exception {
        if (load() == ERROR) {
            return (ERROR);
        }

        if (game.getPlayer() != null) {
            PlayerPrimaryKey pk = game.getPlayer().getPlayerPrimaryKey();

            // null out the parent first so there's no constraint during deletion
            game.setPlayer(null);

            GameBusinessDelegate parentDelegate = GameBusinessDelegate.getGameInstance();

            parentDelegate.saveGame(game);

            // safe to delete the child
            PlayerBusinessDelegate childDelegate = PlayerBusinessDelegate.getPlayerInstance();
            childDelegate.delete(pk);
        }

        return NONE;
    }
}
