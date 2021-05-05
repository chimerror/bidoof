(ns bidoof.component
  (:require [brute.entity :as br.entity]))

(defn get-singleton-entity [system singleton-type]
  (-> system
      (br.entity/get-all-entities-with-component singleton-type)
      (first)))

(defn get-singleton-component
  ([system singleton-type] (get-singleton-component system singleton-type singleton-type))
  ([system singleton-type component-type]
    (-> (get-singleton-entity system singleton-type)
        (as-> entity (br.entity/get-component system entity component-type)))))

(defrecord Card [suit rank])
(defrecord Deck [cards])