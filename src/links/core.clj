(ns links.core
  (:require [org.httpkit.server :as server])
  (:require [links.handlers :as handlers]
            [crossref.util.config :refer [config]])
  (:gen-class))

; For use in REPL.
(defonce s (atom nil))

(defn stop-server
  []
  (@s)
  (reset! s nil)
  (prn "Stop Server" @s))

(defn start-server []
  (reset! s (server/run-server #'handlers/app {:port (:port config)}))
  (prn "Start Server" @s))

(defn restart-server []
  (stop-server)
  (start-server))

(defn -main
  [& args]
  (when (empty? args)
    (server/run-server #'handlers/app {:port (:port config)})))
