(ns bidoof.cards
  (:require
    [brute.entity :as br.entity]
    [bidoof.component :as bi.component]))

(def card-suits [:spades :hearts :diamonds :clubs])

(defn get-cards []
  (for [suit card-suits
        rank (range 1 14)]
      (bi.component/->Card suit rank)))

(defn create-deck-component []
  (let [cards (get-cards)]
    (bi.component/->Deck cards)))

(defn create-entities [system]
  (let [deck-entity (br.entity/create-entity)]
    (-> system
        (br.entity/add-entity deck-entity)
        (br.entity/add-component deck-entity (create-deck-component)))))