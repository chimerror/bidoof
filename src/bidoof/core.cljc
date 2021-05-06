(ns bidoof.core
  (:require
    [brute.entity :as br.entity]
    [bidoof.cards :as bi.cards]
    [bidoof.utils :as bi.utils]
    [play-cljc.gl.core :as pc.core]
    #?(
      :clj  [play-cljc.macros-java :refer [gl]]
      :cljs [play-cljc.macros-js :refer-macros [gl]])))

(defonce *state (atom {
  :mouse-x 0
  :mouse-y 0
  :pressed-keys #{}}))

(defonce *system (atom nil))

(defn init [game]
  ;; allow transparency in images
  (gl game enable (gl game BLEND))
  (gl game blendFunc (gl game SRC_ALPHA) (gl game ONE_MINUS_SRC_ALPHA))
  (-> (br.entity/create-system)
      (assoc :cljc-game game)
      (bi.cards/create-entities)
      (as-> s (reset! *system s))))
  ;; TODO: 1. load using bi.utils/get-image, passing in a callback to create an entity
  ;; TODO: 2. create an entity with play-cljc.gl.entities-2d /->image-entity, then pc.core/compile,
  ;; load images and put them in the state atom

(def screen-entity {
  :viewport {:x 0 :y 0 :width 0 :height 0}
  :clear {:color [(/ 0 255) (/ 0 255) (/ 0 255) 1] :depth 1}})

(defn tick [game]
  ;; TODO: use pc.core/render on an ran through pc.e2d/->image-entity and pc.core/compile
  (let [game-width (bi.utils/get-width game)
        game-height (bi.utils/get-height game)]
    (when (and (pos? game-width) (pos? game-height))
      ;; render the blue background
      (pc.core/render game (update screen-entity :viewport assoc :width game-width :height game-height)))
  game))
