(ns linear-regression.core-test
  (:require [clojure.test :refer :all]
            [linear-regression.core :refer :all]
            [clojure.math :as math]
            [clojure.core.matrix :as matrix]))

(defn almost-same [true_v measurement error]
  (<= (abs (- true_v measurement)) error))

(defn box-muller []
  (let [X (rand) Y (rand)]
    (* (math/sqrt (* -2 (math/log X))) (math/cos (* 2 3.14 Y)))))

(deftest func-test
  (testing "predict test."
    (is (= 6 (predict [2,2] [2])))
    (is (= 4 (predict [2,0] [2])))))

(deftest transpose-test
  (testing "transpose test."
    (is (= (matrix/transpose [[1,2],[3,4]]) [[1,3],[2,4]]))))

(deftest almost-same-test
  (testing "almost same test."
    (is (true? (almost-same 1.0 1.5 1.0)))
    (is (true? (almost-same 1.0 2.0 1.0)))
    (is (not (almost-same 1.0 2.5 1.0)))))

(def rand-for-test (fn [] (- (rand-int 30) 15)))

(defn fit-test-func [x w same-func noise-func]
  (is (= (+ 1 (count (first x))) (count w)))
  (let [y (mapv #(+ (predict w %1) (noise-func)) x)
        pred_w (fit x y)]

    (is (= (count y) (count x)))
    (is (= (count pred_w) (count w)))

    (doseq [ew (zipmap w pred_w)] (is (apply same-func ew)))))


(def x-for-test (mapv (fn [a] (mapv (fn [b] (float b)) a)) (filter #(= (mod (first %1) 2) 0) (mapv #(vector %1) (range -5 6 1)))))

(deftest fit-test
  (testing "fit test."
    (dotimes [i 100]
      (fit-test-func x-for-test [(float (/ i 10)) (float (mod i 10))] #(almost-same %1 %2 0.01) #(float 0)))))

(deftest fit-with-noise-test
  (testing "fit with noise test."
    (dotimes [i 100]
      (fit-test-func x-for-test [(float (/ i 10)) (float (mod i 10))] #(almost-same %1 %2 1) #(/ (box-muller) 2.0)))))

(deftest get-R2-test
  (testing "get R2 test."
    (is (= (get-R2 [1,2,3] [1,2,3]) 1.0))
    (is (< (get-R2 [1,2,3] [1,0,3]) (get-R2 [1,2,3] [1,2,3])))))

