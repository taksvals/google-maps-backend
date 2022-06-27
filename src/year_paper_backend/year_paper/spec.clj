(ns year-paper-backend.year-paper.spec
  (:require
   [clojure.spec.alpha :as s]))

(defn email? [email]
  (string? email))

(defn password? [password]
  (string? password))

(s/def :response/message string?)

(s/def :message/data
  (s/keys :req-un [:response/message]))

(s/def ::message.response
  (s/keys :req-un [:message/data]))
