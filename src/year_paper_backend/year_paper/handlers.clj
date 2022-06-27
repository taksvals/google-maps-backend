(ns year-paper-backend.year-paper.handlers
  (:require
;;    [reitit.ring :as reitit-ring]
;;    [hiccup.page :refer [include-js include-css html5]]
;;    [config.core :refer [env]]
   [ring.util.http-response :as response]
   [year-paper-backend.year-paper.db :as db]))

(def not-found 
  (response/not-found {:data {:message "Not Found"}}))

(defn health [_req]
  (response/ok {:message "Server is ok"}))

(defn login [db]
  (fn [{{{:keys [email password]} :body} :parameters}]
    (try
      (if-let [res (db/username->user db email password)]
        (response/ok {:message res})
        not-found)
      (catch Exception ex
        (print ex)))))

(defn register [db]
  (fn [{{{:keys [username email password]} :body} :parameters}]
    (try
      (if-let [res (db/save-user! db username email password)]
        (response/ok {:message res})
        not-found)
      (catch Exception ex
        (print ex)))))
