/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.console.state;

import is.project2.console.MusicApp;

/**
 *
 * @author Flávio
 */
public abstract class AbstractState {

    /**
     * Application.
     */
    protected final MusicApp app;

    /**
     *
     * @param app
     */
    public AbstractState(MusicApp app) {
        assert (app != null);
        this.app = app;
    }

    /**
     * Processes the current state and returns the next state. A null state
     * means it should terminate.
     *
     * @return Next state or null.
     */
    public abstract AbstractState process();
}
