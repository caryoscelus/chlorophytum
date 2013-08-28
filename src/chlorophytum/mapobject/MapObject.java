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

package chlorophytum.mapobject;

import chlorophytum.*;
import chlorophytum.map.*;
import chlorophytum.story.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

import java.util.Map;
import java.util.HashMap;

/**
 * Object that can be placed on map.
 * Needs lots of cleaning
 */
public class MapObject extends StoryObject {
    public ChloroMap onMap = null;
    public String onMapName = null;
    public final Vector2 position = new Vector2();
    public final Vector2 move = new Vector2();
    
    protected Map<String, Vector2> mapPositions = new HashMap();
    
    protected MapObjectViewData viewData = null;
    protected MapObjectView view = null;
    
    public void render (SpriteBatch batch) {
        view.render(batch, viewData);
    }
    
    /**
     * Update this object
     */
    public void update(float dt) {
    }
    
    /**
     * Move to an offset
     * @param dx float dx
     * @param dy float dy
     */
    public void move (float dx, float dy) {
        move(new Vector2(dx, dy));
    }
    
    /**
     * Move to an offset.
     * This will also call moved method
     * @param offset Vector2 offset between current position and target
     */
    public void move (Vector2 offset) {
        position.add(offset);
        moved();
    }
    
    /**
     * Override this to get notified about movements
     */
    public void moved () {
    }
    
    /**
     * Move to specific map.
     * Will use either saved position or get spawn-x and spawn-y properties
     * @param mapName name of map
     */
    public void moveTo (String mapName) {
        Vector2 xy = mapPositions.get(mapName);
        float x, y;
        if (xy != null) {
            x = xy.x;
            y = xy.y;
        } else {
            // this isn't too good :/
            ChloroMap tmap = World.instance().loadMap(mapName);
            x = Float.parseFloat(tmap.map.getProperties().get("spawn-x", "0", String.class));
            y = Float.parseFloat(tmap.map.getProperties().get("spawn-y", "0", String.class));
        }
        moveTo(mapName, x, y);
    }
    
    /**
     * Move to specific map and position
     * @param mapName name of map
     * @param x x-coordinate on new map
     * @param y y-coordinate on new map
     */
    public void moveTo (String mapName, float x, float y) {
        if (onMapName != mapName) {
            if (onMap != null) {
                onMap.removeObject(this);
            }
            
            mapPositions.put(onMapName, position.cpy());
            onMapName = mapName;
            onMap = World.instance().loadMap(mapName);
            
            onMap.addObject(this);
        }
        position.set(x, y);
    }
    
    /**
     * Move to specific map and position
     * @param mapName name of map
     * @param xy new position
     */
    public void moveTo (String mapName, Vector2 xy) {
        moveTo(mapName, xy.x, xy.y);
    }
    
    /**
     * Get tile from layer with lid at current position
     * @param lid Layer id to get tile from
     */
    protected TiledMapTile getTile (int lid) {
        return getTile(lid, 0, 0);
    }
    
    /**
     * Get tile from layer with name at current position
     * @param name Layer name to get tile from
     */
    protected TiledMapTile getTile (String name) {
        return getTile(name, 0, 0);
    }
    
    /**
     * Get tile from layer with lid at offseted position
     * @param lid Layer id to get tile from
     * @param dx delta x
     * @param dy delta y
     */
    protected TiledMapTile getTile (int lid, float dx, float dy) {
        if (onMap != null) {
            TiledMapTileLayer layer = (TiledMapTileLayer) onMap.map.getLayers().get(lid);
            return getTile(layer, dx, dy);
        }
        return null;
    }
    
    /**
     * Get tile from layer with name at offseted position
     * @param name Layer name to get tile from
     * @param dx delta x
     * @param dy delta y
     */
    protected TiledMapTile getTile (String name, float dx, float dy) {
        if (onMap != null) {
            TiledMapTileLayer layer = (TiledMapTileLayer) onMap.map.getLayers().get(name);
            return getTile(layer, dx, dy);
        }
        return null;
    }
    
    /**
     * Get tile from specific layer at offseted position
     * @param layer Layer to get tile from
     * @param dx delta x
     * @param dy delta y
     */
    protected TiledMapTile getTile (TiledMapTileLayer layer, float dx, float dy) {
        if (layer != null) {
            int cx = (int) (getCentre().x+dx);
            int cy = (int) (getCentre().y+dy);
            TiledMapTileLayer.Cell cell = layer.getCell(cx, cy);
            if (cell != null) {
                return cell.getTile();
            }
        }
        return null;
    }
    
    /**
     * Get centre of this object
     */
    protected Vector2 getCentre () {
        return new Vector2().set(position).add(1, 1);
    }
}
