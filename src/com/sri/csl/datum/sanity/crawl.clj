(ns com.sri.csl.datum.sanity.crawl
  (:require [com.sri.csl.datum.sanity.check :as check]))

(declare crawl)

(defn check-node [datum path checkers node]
  (let [applicable-checks (check/applicable checkers path)]
    (->> applicable-checks
         (map #(% datum path node))
         flatten
         (filter identity)
         (map (fn [err]
                {:path path
                 :error err})))))

(defn crawl-map [datum path checkers m]
  (mapcat
   (fn [[label child]]
     (crawl datum (conj path label) checkers child))
   m))

(defn crawl-seq [datum path checkers s]
  (apply concat
         (map-indexed
          (fn [idx child]
            (crawl datum (conj path idx) checkers child))
          s)))

(defn crawl [datum path checkers node]
  (concat
   (check-node datum path checkers node)
   (cond
     (map? node)
     (crawl-map datum path checkers node)

     (sequential? node)
     (crawl-seq datum path checkers node)

     :else
     (check-node datum (conj path node) checkers nil))))
