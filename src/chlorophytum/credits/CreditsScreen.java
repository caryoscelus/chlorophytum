/*
 *  Copyright (C) 2013 caryoscelus
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *  Additional permission under GNU GPL version 3 section 7:
 *  If you modify this Program, or any covered work, by linking or combining
 *  it with Clojure (or a modified version of that library), containing parts
 *  covered by the terms of EPL 1.0, the licensors of this Program grant you
 *  additional permission to convey the resulting work. {Corresponding Source
 *  for a non-source form of such a combination shall include the source code
 *  for the parts of Clojure used as well as that of the covered work.}
 */

package chlorophytum.credits;

import chlorophytum.Scripting;
import chlorophytum.UiManager;
import chlorophytum.util.Invokable;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;

/**
 * Credits screen.
 * Probably need to separate view (stage) and screen itself
 */
public class CreditsScreen implements Screen, AbstractCreditsScreen {
    protected boolean inited = false;
    protected CreditsData creditsData = new CreditsData();
    
    protected CreditsStage stage;
    
    /**
     * What to do when this screen should be exited.
     * TODO: move this to some super-class?..
     */
    public Invokable onExit;
    
    @Override
    public void show () {
        if (!inited) {
            init();
        }
    }
    
    @Override
    public void hide () {
    }
    
     @Override
    public void resize (int width, int height) {
    }
    
    @Override
    public void pause () {
    }
    
    @Override
    public void resume () {
    }
    
    @Override
    public void dispose () {
    }
    
    /**
     * Init.
     * Loads credits clojure lib and credits script itself
     * Also inits ui
     */
    public void init () {
        Scripting.run("scripts/base-credits.clj");
        Scripting.run("data/scripts/credits.clj");
        Scripting.getVar("credits", "set-data").invoke(creditsData);
        
        initUi();
    }
    
    protected void initUi () {
        stage = new CreditsStage(this, creditsData);
        stage.updateUi();
    }
    
    /**
     * Logic update
     * @param dt float delta time in seconds
     */
    public void update (float dt) {
        stage.act(dt);
    }
    
    @Override
    public void render (float dt) {
        update(dt);
        stage.draw();
    }
    
    /**
     * User clicked on stage.
     * Currently triggers onExit
     */
    @Override
    public void processClick () {
        if (onExit != null) {
            onExit.invoke();
        }
    }
}
