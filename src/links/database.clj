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
    :predicateType
    :object
    :objectType
    :provenance))

;https://coderwall.com/p/omnlba/workaround-korma-fields-variadic-arguments
(defmacro only-fields
    [query fields]
    `(let [field-list# (list* ~fields)
             fargs# (cons ~query field-list#)]       
        (apply k/fields fargs#)))
  
(defn find-links
  [subject subject-type predicate predicate-type object object-type offset limit distinct-fields]
  (prn [subject subject-type predicate predicate-type object object-type offset limit distinct-fields])
  (let [distinct-fields (when (not (empty? distinct-fields)) distinct-fields)
        where-clause 
        (-> {}
            (conj (when subject {:subject subject}))
            (conj (when subject-type {:subjectType subject-type}))
            (conj (when predicate {:predicate predicate}))
            (conj (when predicate-type {:predicateType predicate-type}))
            (conj (when object {:object object}))
            (conj (when object-type {:objectType object-type})))
        query (-> :links k/select*
                        (k/where where-clause)
                        (k/limit limit)
                        (#(if distinct-fields
                           (only-fields % distinct-fields)
                           %))
                        (#(if distinct-fields
                           (k/modifier % "distinct")
                           %))
                        (k/offset offset))]
      (k/select query)))

(defn insert-link
  [subject subject-type object object-type predicate provenance]
  (k/insert links (k/values {:subject subject
                             :subjectType subject-type
                             :predicate predicate
                             :object object
                             :objectType object-type
                             :provenance provenance})))
