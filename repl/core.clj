(ns repl.core
  (:require
   [integrant.core :as ig]
   [year-paper-backend.year-paper.conf :as conf]
   [year-paper-backend.year-paper.system :as system]))

(def system-ref (atom nil))

(defn stop! []
  (when-some [sys @system-ref]
    (ig/halt! sys)
    (reset! system-ref nil))
  nil)

(defn start! []
  (when @system-ref
    (stop!))
  (reset! system-ref
          (-> {:stage :local}
              conf/mk-config
              (system/mk-system {})))
  :STARTED)


(defn restart! []
  (stop!)
  (start!))
