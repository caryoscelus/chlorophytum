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

package chlorophytum.map;

import chlorophytum.mapobject.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.math.*;

import java.util.HashSet;

/**
 * Contains TiledMap and objects placed on it
 */
public class ChloroMap implements Disposable {
    public TiledMap map;
    public HashSet<MapObject> objects = new HashSet();
    
    public ChloroMap (TiledMap tmap) {
        map = tmap;
        if (map == null) {
            throw new NullPointerException();
        }
    }
    
    public MapLayer getLayer (int lid) {
        return map.getLayers().get(lid);
    }
    
    public MapLayer getLayer (String name) {
        return map.getLayers().get(name);
    }
    
    protected float tileWidth () {
        return ((TiledMapTileLayer) map.getLayers().get(0)).getTileWidth();
    }
    
    protected float tileHeight () {
        return ((TiledMapTileLayer) map.getLayers().get(0)).getTileHeight();
    }
    
    /**
     * Check if some object overlays some point
     * @param layerName name of layer to search for objects on
     * @param point point which object should contain (in tiles)
     * @return mapobjects that contain given point
     */
    // ENJOY JAVA NAMING AND EXPORTING CONVENTIONS!
    public com.badlogic.gdx.maps.MapObjects checkObjectLayer(String layerName, Vector2 point) {
        com.badlogic.gdx.maps.MapObjects objs = new com.badlogic.gdx.maps.MapObjects();
        
        MapLayer layer = getLayer(layerName);
        if (layer == null) {
            Gdx.app.log("chloromap", "can't find layer");
            return objs;
        }
        
        for (com.badlogic.gdx.maps.MapObject obj : layer.getObjects()) {
            RectangleMapObject rectObj = (RectangleMapObject) obj;
            
            if (rectObj != null) {
                Rectangle rect = rectObj.getRectangle();
                rect = new Rectangle(rect.getX()/tileWidth()-1,
                                     rect.getY()/tileHeight()-1,
                                     rect.getWidth()/tileWidth(),
                                     rect.getHeight()/tileHeight()
                );
                
                if (rect.contains(point)) {
                    objs.add(obj);
                }
            }
        }
        
        return objs;
    }
    
    /**
     * Remove object from map
     */
    public void removeObject (MapObject obj) {
        objects.remove(obj);
    }
    
    /**
     * Add object to map
     */
    public void addObject (MapObject obj) {
        objects.add(obj);
    }
    
    @Override
    public void dispose () {
        map.dispose();
    }
}
