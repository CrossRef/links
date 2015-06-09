(ns links.handlers
  (:require [crossref.util.doi :as cr-doi])
  (:require [compojure.core :refer :all]
            [compojure.route :as route])
  (:require [links.database :as database])
  (:require [clojure.data.json :as json]
            [clojure.string :as string])
  (:require [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.stacktrace :refer [wrap-stacktrace]])
  (:require [liberator.core :refer [defresource resource]])
  (:require [clj-time.format :as format])
  (:require [crossref.util.config :refer [config]]))

(def page-limit 50)

(defresource get-links
  []
  :allowed-methods [:get]
  :available-media-types ["application/json"]
  :handle-ok (fn [ctx]
               (let [offset (or (get-in ctx [:request :params "offset"]) 0)
                     subject (get-in ctx [:request :params "subject"])
                     subject-type (get-in ctx [:request :params "subject-type"])
                     predicate (get-in ctx [:request :params "predicate"])
                     object (get-in ctx [:request :params "object"])
                     object-type (get-in ctx [:request :params "object-type"])
                     links (database/find-links subject subject-type predicate object object-type offset page-limit)
                     links (map #(select-keys % [:subject :subjectType :predicate :object :objectType :provenance]) links)
                     ]
                 {:status "ok",
                  :message-type "link-list",
                  :message-version "1.0.0",
                  :message {
                    :items-per-page page-limit,
                    :items links}})))

(defresource post-links
  []
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :post! (fn [ctx]
                 (let [body (-> ctx :request :body slurp json/read-str)
                       subject (get body "subject")
                       subject-type (get body "subject-type")
                       predicate (get body "predicate")
                       object (get body "object")
                       object-type (get body "object-type")
                       provenance (get body "provenance")]
                   (prn "subject" subject "subject-type" subject-type "predicate" predicate "object" object "object-type" object-type "provenance" provenance)
                 (when (and subject subject-type object object-type predicate provenance)
                   (database/insert-link subject subject-type object object-type predicate provenance)))))

(defroutes app-routes
  (GET ["/links"] [] (get-links))
  (POST ["/links"] [] (post-links))
  (route/not-found "<h1>Page not found</h1>"))

(def app
  (-> app-routes
     (wrap-params)))

