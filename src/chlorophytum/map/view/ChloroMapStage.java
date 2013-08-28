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

package chlorophytum.map.view;

import chlorophytum.map.ChloroMap;

import com.badlogic.gdx.maps.tiled.renderers.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
// import com.badlogic.gdx.maps.tiled.*;

public class ChloroMapStage extends Stage {
    public OrthogonalTiledMapRenderer renderer;
    protected OrthographicCamera camera;
    
    protected float tileSize;
    protected float tilesNX;
    protected float tilesNY;
    
    protected ChloroMap map;
    
    public void init (float tSize, float tnX, float tnY) {
        tileSize = tSize;
        tilesNX = tnX;
        tilesNY = tnY;
        
        renderer = new OrthogonalTiledMapRenderer(null, 1 / tileSize);
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, tilesNX, tilesNY);
        camera.update();
    }
    
    public void setMap (ChloroMap newMap) {
        map = newMap;
        renderer.setMap(map.map);
    }
    
    public void setPosition (Vector2 newPos) {
        camera.position.x = newPos.x;
        camera.position.y = newPos.y;
    }
    
    public Vector2 getCamPosition() {
        return new Vector2(camera.position.x-1, camera.position.y-1);
    }
    
    @Override
    public void draw () {
        super.draw();
        
        camera.update();
        
        renderer.setView(camera);
        renderer.render();
    }
}
