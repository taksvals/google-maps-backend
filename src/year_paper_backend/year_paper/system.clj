(ns year-paper-backend.year-paper.system
  (:require
   [integrant.core :as ig]))

(let [lock (Object.)]
  (defn load-namespaces
    [system-config]
    (locking lock
      (ig/load-namespaces system-config))))

(defn mk-system [cfg {:keys [keys]}]
  (load-namespaces cfg)
  (if keys
    (ig/init (ig/prep cfg) keys)
    (ig/init (ig/prep cfg))))
