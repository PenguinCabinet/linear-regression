(ns linear-regression.core
  (:require [clojure.core.matrix :as matrix]
            [clojure.math :as math]
            [clojure.string :as string]
            [clojure.data.csv :as csv]))

(defn
  predict
  [w x]
  (reduce
   +
   (map
    #(* %1 %2)
    w
    (conj x 1))))

(defn fit-matrix [x y]
  (let [x (mapv #(conj %1 1) x) x_T (matrix/transpose x)]
    (matrix/mmul
     (matrix/mmul
      (matrix/inverse (matrix/mmul x_T x))
      x_T)
     y)))

(defn fit [x y]
  (into [] (fit-matrix x y)))

(defn get-R2 [y pred_y]
  (let [mean_y (/ (reduce + y) (float (count y)))]
    (- 1 (/
          (reduce + (mapv #(math/pow (- %1 %2) 2) y pred_y))
          (reduce + (mapv #(math/pow (- %1 mean_y) 2) y))))))



(defn -main
  "main"
  [input_csv_name]
  (let [text (slurp input_csv_name :encoding "utf-8")
        rows (csv/read-csv text)
        data (mapv (fn [x] (mapv (fn [x2] (Float/parseFloat x2)) x)) rows)
        train_x (mapv #(into [] (drop-last %1)) data)
        train_y (mapv #(last %1) data)]
    (println "Learning...\n")
    (let [pred_w (fit train_x train_y)
          pred_y (mapv #(predict pred_w %1) train_x)]
      (println "Predict Function")
      (printf "f(x) = %s+%f\n"
              (string/join (map-indexed #(str %2 "*X" %1 "+") (drop-last pred_w)))
              (last pred_w))
      (println)

      (println "Example prediction")
      (printf "x=%s, pred_y=%f, true_y=%f\n"
              (-> (first train_x) str)
              (first pred_y)
              (first train_y))
      (println)

      (println "Coefficients")

      (printf "R^2 = %f\n"
              (get-R2
               train_y pred_y)))))
