(ns year-paper-backend.year-paper.conf
  (:require 
   [integrant.core :as ig]))

(defn mk-config [{:keys [stage]}]
  (let [local? (= stage :local)]
    (merge 
     {:year-paper-backend.ig/db {:uri "mongodb+srv://taksvals:VUgnf4k8B85ETUof@year-paper-cluster.2gbpb.mongodb.net/test?retryWrites=true&w=majority"}
      :year-paper-backend.year-paper.routes/handler {:stage stage
                                                     :db    (ig/ref :year-paper-backend.ig/db)}}
     (when local?
       {:year-paper-backend.ig/jetty {:port    5050
                                      :join?   false
                                      :handler (ig/ref :year-paper-backend.year-paper.routes/handler)}}))))

