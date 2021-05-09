(ns bidoof.background
  (:require
    [brute.entity :as br.entity]
    [play-cljc.gl.core :as pc.core]
    [play-cljc.gl.entities-2d :as pc.e2d]
    [bidoof.component :as bi.component]
    [bidoof.utils :as bi.utils]))

(defrecord Background [screen-entity])

(def initial-screen-entity {
  :viewport {:x 0 :y 0 :width 0 :height 0}
  :clear {:color [(/ 0 255) (/ 0 255) (/ 0 255) 1] :depth 1}})

(defn- update-screen-entity! [system new-width new-height]
  (let [background-component (bi.component/get-singleton-component system Background)
        screen-entity (:screen-entity background-component)]
    (swap! screen-entity #(update % :viewport assoc :width new-width :height new-height)))
  system)

(defn- render-screen-entity [system]
  (let [game (system :cljc-game)
        background-component (bi.component/get-singleton-component system Background)
        screen-entity (:screen-entity background-component)]
    (pc.core/render game @screen-entity))
  system)

(defn create-entities [system]
  (let [background-entity (br.entity/create-entity)]
    (-> system
        (br.entity/add-entity background-entity)
        (br.entity/add-component background-entity (->Background (atom initial-screen-entity))))))

(defn process-one-game-tick [system _]
  (let [game (system :cljc-game)
        game-width (bi.utils/get-width game)
        game-height (bi.utils/get-height game)]
    (cond
      (and (pos? game-width) (pos? game-height))
        (-> system
            (update-screen-entity! game-width game-height)
            (render-screen-entity))
      :else system)))
