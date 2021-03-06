;;  Copyright (C) 2013 caryoscelus
;;  
;;  This program is free software: you can redistribute it and/or modify
;;  it under the terms of the GNU General Public License as published by
;;  the Free Software Foundation, either version 3 of the License, or
;;  (at your option) any later version.
;;  
;;  This program is distributed in the hope that it will be useful,
;;  but WITHOUT ANY WARRANTY; without even the implied warranty of
;;  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;;  GNU General Public License for more details.
;;  
;;  You should have received a copy of the GNU General Public License
;;  along with this program.  If not, see <http://www.gnu.org/licenses/>.
;;  
;;  Additional permission under GNU GPL version 3 section 7:
;;  If you modify this Program, or any covered work, by linking or combining
;;  it with Clojure (or a modified version of that library), containing parts
;;  covered by the terms of EPL 1.0, the licensors of this Program grant you
;;  additional permission to convey the resulting work. {Corresponding Source
;;  for a non-source form of such a combination shall include the source code
;;  for the parts of Clojure used as well as that of the covered work.}


(ns story)

(import '(chlorophytum.story Story StoryEvent StoryDialog StoryDialogLine)
        '(chlorophytum Loader World Streamer)
        '(java.util Vector)
        '(com.badlogic.gdx Gdx))

(declare event)

(defn log [text]
      (.log (Gdx/app) "log" (str text)))

(defn make-dialog [text save & args]
      (if (= (class text) StoryDialog)
          text
          (let [opts (first args)]
               (cond
                   (nil? opts) (StoryDialog. text save)
                   (list? opts) (StoryDialog. text (Vector. opts) save)
                   (fn? opts) (StoryDialog. text (event opts) save)
                   :else (StoryDialog. text opts save)))))

(defn event [action]
      (cond
          (fn? action)
            (proxy [StoryEvent] []
                    (trigger [context]
                            (let [a (action)]
                                (if (and (= a false) (not (nil? context)))
                                    (.end context)))))
          (nil? action)
            (proxy [StoryEvent] []
                   (trigger [context]
                            (if (not (nil? context))
                                (.end context))))
          (string? action)
            (.getEvent (Story/instance) action)
          :else
            action))

(defn add-event [ename new-event]
      (let [ne (cond
                   (fn? new-event) (event new-event)
                   :else new-event)]
           (.addEvent (Story/instance) ename ne)))

(defn object-move [object new-map & xy]
      (cond
          (empty? xy) (.moveTo object new-map)
          :else (.moveTo object new-map (first xy) (second xy))))

(defn self-move [dx dy]
      (let [self (.getObject (.story (World/instance)) "self")]
           (.move self dx dy)))

(defn change-map [map-name & xy]
      (let [self (.getObject (.story (World/instance)) "self")]
           (apply object-move (concat (list self map-name) xy))))

(defn dialog-line [txt act & opts]
      (let [visible (if (empty? opts)
                        true
                        (first opts))]
           (StoryDialogLine. txt (event act) visible)))

(defmacro redef [var-name value]
          (list
              'do
              (list 'def var-name value)
              '(run "_update")))

;; shortcuts
(def bind add-event)
(defmacro bindf [ename & code-lines] (list 'bind ename (concat (list 'fn []) code-lines)))
(defn t [text & args] (apply make-dialog (concat (list text false) args)))
(defn tr [text & args] (apply make-dialog (concat (list text true) args)))
(def ln dialog-line)
(defn lnh [text ev] (ln text ev false))
(defmacro lnf [text & code-lines] (list 'ln text (concat (list 'fn []) code-lines)))
(defmacro lnhf [text & code-lines] (list 'ln text (concat (list 'fn []) code-lines) false))
(def ev event)
(defn run [revent] (.trigger (Story/instance) (ev revent)))
(defn runafter [revent] (.queue (Story/instance) (ev revent)))



;; music
(defn play-loop [fname]
      (.load (Streamer/instance) fname)
      (.play (Streamer/instance)))
