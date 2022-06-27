(ns year-paper-backend.year-paper.routes
  (:require
   [muuntaja.core                      :as m]
   [integrant.core                     :as ig]
   [reitit.swagger                     :as swagger]
   [reitit.swagger-ui                  :as swagger-ui]
   [reitit.coercion.spec]
   [reitit.ring                        :as ring]
   [reitit.dev.pretty                  :as pretty]
   [reitit.ring.coercion               :as coercion]
   [reitit.ring.middleware.muuntaja    :as muuntaja]
   [reitit.ring.middleware.parameters  :as parameters]
   [ring.middleware.cors               :refer [wrap-cors]]

   [year-paper-backend.year-paper.spec :as spec]
   [year-paper-backend.year-paper.handlers :as h]
   [year-paper-backend.year-paper.middleware :as middleware]
   ))

(defn routes [{:keys [db]}]
  [""
   ["/docs/*"
    {:no-doc true
     :get    (swagger-ui/create-swagger-ui-handler
              {:url "/swagger.json"
               :config {:validatorUrl     nil
                        :operationsSorter "alpha"}})}]
   ["/swagger.json"
    {:get {:no-doc true
           :swagger {:info {:title "Year paper"}
                     :tags []}
           :handler (swagger/create-swagger-handler)}}]
   ["/health"
    {:get {:no-doc true
           :handler h/health}}]
   ["" {:swagger {:tags ["Auth"]}}
    ["/login"
     {:post {:summary    "Login user"
             :parameters {:body {:email    spec/email? ;; validate email
                                 :password spec/password? ;; validate password
                                 }}
             :responses  {200 {:body any?}
                          404 {:body ::spec/message.response}}
             :handler    (h/login db)}}]  ;; TODO: add login handler
    ["/register"
     {:post {:summary    "Register user"
             :parameters {:body {:username string?
                                 :email    spec/email? ;; validate email
                                 :password spec/password? ;; validate password
                                 }}
             :responses  {200 {:body any?}
                          404 {:body ::spec/message.response}}
             :handler    (h/register db)}}]]]) ;; TODO: add register handler

(defmethod ig/init-key ::handler
  [_ config]
  (ring/ring-handler
   (ring/router (routes config)
                {:exception pretty/exception
                 :data      {:coercion   reitit.coercion.spec/coercion
                             :muuntaja   m/instance
                             :middleware [middleware/wrap-string-response
                                          [wrap-cors
                                           :access-control-allow-origin [#".*"]
                                           :access-control-allow-methods [:get :post :put :delete :head]]
                                          swagger/swagger-feature
                                          parameters/parameters-middleware
                                          muuntaja/format-negotiate-middleware
                                          muuntaja/format-response-middleware
                                          muuntaja/format-request-middleware
                                          coercion/coerce-response-middleware
                                          coercion/coerce-request-middleware]}})))
