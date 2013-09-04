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

import chlorophytum.UiManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class CreditsStage extends Stage {
    protected AbstractCreditsScreen screen;
    protected CreditsData creditsData;
    
    public CreditsStage (AbstractCreditsScreen scr, CreditsData data) {
        screen = scr;
        creditsData = data;
    }
    
    /**
     * Build ui
     */
    public void updateUi() {
        final Skin skin = UiManager.instance().skin;
        final Table table = new Table();
        table.setFillParent(true);
        
        clear();
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.processClick();
            }
        });
        Gdx.input.setInputProcessor(this);
        
        for (CreditsSection section : creditsData.sections) {
            final Label sectionLabel = new Label(section.name, skin);
            table.add(sectionLabel).colspan(2).center().space(20);
            table.row();
            
            for (CreditsSection.Line line : section.authors) {
                final Label authorLabel = new Label(line.name, skin);
                final Label occupationLabel = new Label(line.occupation, skin);
                table.add(authorLabel).right().space(10);
                table.add(occupationLabel).left().space(10);
                table.row();
            }
        }
        
        addActor(table);
    }
}
