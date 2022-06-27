(ns year-paper-backend.ig.db
  (:require
   [integrant.core    :as ig]
   [monger.core       :as mg]))

(defmethod ig/init-key :year-paper-backend.ig/db [_ {:keys [uri]}]
  (mg/connect-via-uri uri))
