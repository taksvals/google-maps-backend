(ns year-paper-backend.year-paper.db
  (:require [monger.collection :as mc]))

(defn save-user! [{:keys [db] :as _db}
                  username email password]
  (mc/save db ":collection/users" {:username username
                                   :email    email
                                   :password password})
  {:username username
   :email    email
   :password password
   :active   true})

(defn username->user [{:keys [db] :as _db}
                      email password]
  (some-> (mc/find-one-as-map db ":collection/users" {:email    email
                                                      :password password})
          
          (dissoc :_id)
          (assoc :active true)))

