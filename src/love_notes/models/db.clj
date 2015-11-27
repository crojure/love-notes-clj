(ns love-notes.models.db
  (:require [clojure.java.jdbc :as sql])
  (:import java.sql.DriverManager))

(def db
  {:classname "org.sqlite.JDBC", :subprotocol "sqlite", :subname "db.sq3"})

(defn create-note-table []
  (sql/with-connection db
    (sql/create-table :note
                      [:id "INTEGER PRIMARY KEY AUTOINCREMENT"]
                      [:created "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"]
                      [:sent "TIMESTAMP"]
                      [:message "TEXT"])
    (sql/do-commands "CREATE INDEX created_index ON note (created)")))

(defn read-notes []
  (sql/with-connection db
    (sql/with-query-results res
      ["SELECT * FROM note ORDER BY created DESC"]
      (doall res))))

(defn save-note [message]
  (sql/with-connection
    db
    (sql/insert-values :note [:message] [message])))
