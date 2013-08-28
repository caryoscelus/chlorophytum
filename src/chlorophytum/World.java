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

package chlorophytum;

import chlorophytum.story.*;
import chlorophytum.map.ChloroMap;

import java.util.HashMap;

/**
 * World singleton.
 * Contains Story singleton and maps
 * This doesn't seem good; game doesn't necessery have anything to do
 * with maps.. but i'll leave it for now
 */
public class World {
    private static World _instance;
    
    public static World instance () {
        if (_instance == null) {
            _instance = new World();
        }
        return _instance;
    }
    
    public final Story story = new Story();
    
    public final HashMap<String,ChloroMap> maps = new HashMap();
    
    public ChloroMap loadMap (String name) {
        ChloroMap map = maps.get(name);
        if (map == null) {
            map = new ChloroMap(Loader.instance().loadMap(name));
            maps.put(name, map);
        }
        return map;
    }
}
