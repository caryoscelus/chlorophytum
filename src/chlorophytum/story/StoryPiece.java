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

package chlorophytum.story;

import com.badlogic.gdx.Gdx;

import java.util.Vector;

public class StoryPiece implements StoryContext {
    protected String mainText = "";
    protected Vector<StoryDialogLine> lines = new Vector();
    
    public boolean isFinished = true;
    
    @Override
    public void load (StoryEvent event) {
        StoryDialog dialog = (StoryDialog) event;
        if (dialog != null) {
            loadDialog(dialog);
        } else {
            Gdx.app.log("StoryPiece.load", "event is not StoryDialog instance");
        }
    }
    
    @Override
    public boolean finished () {
        return isFinished;
    }
    
    @Override
    public void begin () {
        isFinished = false;
    }
    
    @Override
    public void end () {
        isFinished = true;
    }
    
    public void loadDialog (StoryDialog dialog) {
        mainText = dialog.text;
        lines = dialog.options;
    }
    
    public String getText () {
        return mainText;
    }
    
    public Vector<StoryDialogLine> getLines () {
        return lines;
    }
}
