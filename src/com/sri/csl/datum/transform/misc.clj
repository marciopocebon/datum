(ns com.sri.csl.datum.transform.misc
  (:require [clojure.string :as string]
            [com.sri.csl.datum.transform.common :as c]))

(defn parse-comment [text]
  {:comment (string/trim text)})

(defn oligo [text]
  {:comment (string/trim (str "oligo:" text))})

(defn timepoint [tpt & mag]
  [tpt (count mag)])

(defn ip
  ([] {:ip false})
  ([_] {:ip true}))

(defn stpt [t unit]
  {:stpt {:time t
          :unit unit}})

(defn change [& vals]
  {:change (string/join " " vals)})

(def transformers
  {:change change
   :comment parse-comment
   :pmid (c/component :pmid)
   :figures (c/multi :figures)
   :source (c/named-merge :source)

   :nat #(Integer. %)
   :num #(Double. %)

   :timepoint timepoint
   :timepoints (c/multi :times)
   :unit (c/component :unit)
   :times (c/named-merge :times)
   :stpt stpt
   :excuse (c/component :excuse)
   
   ;; Oligo currently treated as comment
   :oligo oligo
   ;; :oligo (c/named-merge :extra {:type "oligo"})
   ;; :oligo_str (c/component :name)
   ;; :oligo_seq (c/component :sequence)

   :ipfrom (c/named-merge :ipfrom)

   :strings (c/multi :strings)

   :ip ip
   :handle (c/component :handle)})
