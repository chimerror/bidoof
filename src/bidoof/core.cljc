(ns bidoof.core
  (:require
    [brute.entity :as br.entity]
    [brute.system :as br.system]
    #?(
      :clj  [play-cljc.macros-java :refer [gl]]
      :cljs [play-cljc.macros-js :refer-macros [gl]])
    [bidoof.background :as bi.background]
    [bidoof.cards :as bi.cards]))

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
      (bi.background/create-entities)
      (bi.cards/create-entities)
      (br.system/add-system-fn bi.cards/process-one-game-tick)
      (br.system/add-system-fn bi.background/process-one-game-tick)
      (as-> s (reset! *system s))
      (:cljc-game)))

(defn tick [game]
  (let [system @*system
        delta (game :delta-time)]
    (-> system
        (assoc :cljc-game game)
        (br.system/process-one-game-tick delta)
        (as-> s (reset! *system s))
        (:cljc-game))))
