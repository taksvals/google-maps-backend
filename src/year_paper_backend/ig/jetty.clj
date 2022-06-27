(ns year-paper-backend.ig.jetty
  (:require
   [integrant.core     :as ig]
   [ring.adapter.jetty :as jetty])
  (:import (org.eclipse.jetty.server Server)))

(defmethod ig/init-key :year-paper-backend.ig/jetty
  [_ {:keys [port join? handler]}]
  (println "Starting jetty on port: " port)
  (jetty/run-jetty handler {:port  port
                            :join? join?}))

(defmethod ig/halt-key! :year-paper-backend.ig/jetty
  [_ ^Server server]
  (.stop server))

