(ns links.database
    (:require [korma.core :as k])
    (:require [korma.db :refer [mysql with-db defdb]])
    (:require [crossref.util.config :refer [config]]))

(defdb db
  (mysql {:user (:database-username config)
          :password (:database-password config)
          :db (:database-name config)}))

(k/defentity links
  (k/entity-fields
    :id
    :subject
    :subjectType
    :predicate
    :object
    :objectType
    :provenance))

(defn find-links
  [subject subject-type predicate object object-type offset limit]
  (prn [subject subject-type predicate object object-type offset limit])
  (let [where-clause 
        (-> {}
            (conj (when subject {:subject subject}))
            (conj (when subject-type {:subjectType subject-type}))
            (conj (when predicate {:predicate predicate}))
            (conj (when object {:object object}))
            (conj (when object-type {:objectType object-type})))
        
        query (k/select links (k/where where-clause) (k/limit limit) (k/offset offset))]
      query))
  
(defn insert-link
  [subject subject-type object object-type predicate provenance]
  (k/insert links (k/values {:subject subject
                             :subjectType subject-type
                             :predicate predicate
                             :object object
                             :objectType object-type
                             :provenance provenance})))

