(defmulti task first)

(defmethod task :default
  [[task-name]]
  (println "Unknown task:" task-name)
  (System/exit 1))

(require '[figwheel.main :as figwheel])

(defmethod task nil
  [_]
  (figwheel/-main "--build" "dev"))

(require '[bidoof.start])

(defmethod task "native"
  [_]
  (bidoof.start/start))

(defmethod task "repl"
  [_]
  (clojure.main/repl :init #(doto 'bidoof.start-dev require in-ns)))

(task *command-line-args*)
