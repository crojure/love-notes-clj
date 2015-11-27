(ns love-notes.routes.home
  (:require [compojure.core :refer :all]
            [love-notes.views.layout :as layout]
            [hiccup.form :refer :all]
            [love-notes.models.db :as db]))

(defn show-messages []
  [:ul.messages
    (for [{:keys [message created sent]} (db/read-notes)]
      [:li
        [:blockquote message] [:p "Created - " [:time created]]])])

(defn home [& [message error]] (layout/common
  [:h1 "Love Notes"]
  [:p error]
  (show-messages)
  [:hr]
  (form-to [:post "/"]
  [:p "Message:"]
  (text-area {:rows 10 :cols 40} "message" message) [:br]
  (submit-button "comment"))))

(defn save-message [message]
 (cond
   (empty? message)
   (home message "Don't you have something to say?")
   :else
   (do
     (db/save-note message)
     (home))))

(defroutes home-routes
  (GET "/" [] (home))
  (POST "/" [message] (save-message message)))

