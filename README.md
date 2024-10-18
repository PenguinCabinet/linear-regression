<div align="center">

# 📈 linear-regression


最小二乗法を使った線形回帰

<br>
<br>
</div>

## 🔨 使い方

```bash
lein run -m linear-regression.core test/test.csv
```
```bash
Learning...

Predict Function
f(x) = 2.000000000000001*X0+3.0

Example prediction
x=[0.0], pred_y=3.0, true_y=3.0

Coefficients
R^2 = 1.0
```
### [入力データのフォーマット](https://github.com/PenguinCabinet/linear-regression/blob/main/test/test.csv?plain=1)
単回帰分析の場合、下記のようになります。
```csv
0,3
1,5
2,7
3,9
```
最後の列が出力`y`になります。    

重回帰分析をしたい場合、下記のようになります。
```csv
0,0,3
1,1,5
2,2,7
3,3,9
```
### テスト
```bash
lein test
```

## 🎫 LICENSE

[MIT](./LICENSE)

## ✍ Author

[PenguinCabinet](https://github.com/PenguinCabinet)
