(ns year-paper-backend.year-paper.middleware)

(defn wrap-string-response [handler]
  (fn [request]
    (let [response (handler request)]
      (try 
        (update response :body slurp)
        (catch Exception _e
          response)))))

