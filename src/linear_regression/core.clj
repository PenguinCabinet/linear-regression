(ns linear-regression.core
  (:require [clojure.core.matrix :as matrix]
            [clojure.math :as math]))

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

(require '[clojure.data.csv :as csv])


(defn -main
  "I don't do a whole lot."
  [input_csv_name]
  (let [text (slurp input_csv_name :encoding "utf-8")
        rows (csv/read-csv text)
        data rows
        train_x (mapv #(into [] (drop-last
                                 (mapv (fn [x] (Float/parseFloat x))
                                       %1)))
                      data)
        train_y (mapv #(last
                        (mapv (fn [x] (Float/parseFloat x))
                              %1))
                      data)]
    (println "Learning...\n")
    (let [pred_w (fit train_x train_y)]
      (println "Predict Function")
      (println (apply str
                      (concat "f(x) = "
                              (map #(str %1 "*X" %2 "+")
                                   pred_w
                                   (range (- (count pred_w) 1)))
                              (str (last pred_w)))))
      (println)

      (println "Example prediction")
      (println (apply str
                      (concat "x=" (str (first train_x)) ", "
                              "pred_y=" (str (predict pred_w (first train_x))) ", "
                              "true_y=" (str (first train_y)))))
      (println)

      (println "Coefficients")
      (println "R^2 =" (get-R2
                        train_y (mapv #(predict pred_w %1) train_x))))))
