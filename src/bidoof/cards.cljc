(ns bidoof.cards
  (:require
    [brute.entity :as br.entity]
    [play-cljc.gl.core :as pc.core]
    [play-cljc.gl.entities-2d :as pc.e2d]
    [play-cljc.transforms :as pc.transforms]
    [bidoof.component :as bi.component]
    [bidoof.utils :as bi.utils]))

(defrecord Card [suit rank])
(defrecord Deck [cards])

(def suit-names {
  :spades "Spades"
  :hearts "Hearts"
  :diamonds "Diamonds"
  :clubs "Clubs"})

(def card-suits [:spades :hearts :diamonds :clubs])

(def card-scaling-factor (/ 190 140))

(defn get-card-folder-path [suit use-four-color]
  (cond (and use-four-color (or (identical? suit :clubs) (identical? suit :diamonds))) "images/4colorCards/card"
        :else "images/cards/card"))

(defn prepare-card-images [system]
  (doseq [suit card-suits
          rank (range 1 14)]
    (let [suit-name (suit-names suit)
          rank-name (cond
            (and (>= rank 2) (<= rank 10)) (str rank)
            (== rank 1) "A"
            (== rank 11) "J"
            (== rank 12) "Q"
            (== rank 13) "K")
          use-four-color (:four-colors system)
          image-path (str (get-card-folder-path suit use-four-color) suit-name rank-name ".png")
          game (:cljc-game system)
          card-images (:card-images system)]
          (bi.utils/get-image image-path
            (fn [{:keys [data width height]}]
              (-> (pc.e2d/->image-entity game data width height)
                  (as-> e (pc.core/compile game e))
                  (as-> e (swap! card-images assoc-in [suit rank] e)))))))
  system)

(defn get-card-image [system suit rank]
  (let [card-images (:card-images system)]
    (get-in @card-images [suit rank])))

#_{:clj-kondo/ignore [:unused-binding]} ;; We'll use system eventually
(defn get-cards [system]
  (for [suit card-suits
        rank (range 1 14)]
    (->Card suit rank)))

(defn create-deck-component [system]
  (let [cards (shuffle (get-cards system))]
    (->Deck cards)))

(defn create-entities [system]
  (let [deck-entity (br.entity/create-entity)]
    (-> system
        (assoc :card-images (atom {}))
        (assoc :four-colors true)
        (prepare-card-images)
        (br.entity/add-entity deck-entity)
        (as-> s (br.entity/add-component s deck-entity (create-deck-component s))))))

(defn get-card-order-number [suit rank] (- (* 20 (+ (.indexOf card-suits suit) 1)) (if (== rank 1) 14 rank)))

(defn process-one-game-tick [system _]
  (let [game (:cljc-game system)
        game-width (bi.utils/get-width game)
        game-height(bi.utils/get-height game)
        card-width (/ game-width 13)
        card-height (* card-width card-scaling-factor)
        deck (bi.component/get-singleton-component system Deck)
        hand (->> (:cards deck)
                  (take 13)
                  (sort-by #(get-card-order-number (:suit %) (:rank %)))
                  (vec))]
    (doseq [current-card (range 13)
            :let [x (* current-card card-width)
                  card (hand current-card)
                  card-image (get-card-image system (:suit card) (:rank card))]]
      (when card-image
        (pc.core/render game
          (-> card-image
              (pc.transforms/project game-width game-height)
              (pc.transforms/translate x 0)
              (pc.transforms/scale card-width card-height)))))
    system))