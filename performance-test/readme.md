# 負荷テストサンプル

負荷テストを実施するテストサンプルを提示する。
本サンプルは架空のAPIを対象としているため、実行するには送信するリクエストの修正を必要とする。

### 実行環境

Gatling の実行には、JDK8 が必要となる。また、サンプルではテストの起動に Maven を使用しているため、Maven が必要となる。

これらの環境構築については、本資料では記載しない。

## テストコードの構造

Gatling のテストコードは、Simulation クラスを継承した Scala のクラスを実装することになる。
テストクラスは、大きく4つのブロックで構成される。
詳細は公式ドキュメントを参照
- https://gatling.io/docs/current/general/simulation_structure/

ここでは、シナリオ定義ブロックとシミュレーション定義ブロックについて説明する。

## シナリオ定義ブロック

シナリオ定義は、シナリオに対して exec をブロックチェーンで連結することで定義します。
シナリオを作成するには、この exec の記述方法と exec へ変数を渡す場合に利用する Session の使い方がポイントになります。

```scala
val scn = scenario("シナリオの名前")
  .exec(http("全件取得").get("/articles"))
  .exec(http("１件取得").get("/articles/${id}"))
```

※ exec の中に exec を入れ子にする記載方法もある。exec の定義を再利用したい場合はexecを入れ子にする方がコード量は少ないが、シナリオ定義としては分かりにくくなるため、ここでは割愛する。
公式ドキュメント シナリオ： https://gatling.io/docs/current/general/scenario/

サンプルコードを使ってポイントを説明する。

## exec の書き方

基本的な exec ブロックは下記のようになる。
その他、繰り返しや条件分岐など基本的な記述方法をサンプルコードに記載している。

```scala
.exec(
  http("全件取得")         // リクエストに名前をつける。レポートではこのリクエスト毎にレスポンスタイムが集計される
  .get("/articles")       // メソッドとURLを指定する
  .check(                 // リクエストに対するチェックを定義
    status.in(200, 304),                  // 　HTTPステータスが 200 or 304 であること
    responseTimeInMillis.lessThan(3000))  // 　レスポンスタイムが 3000ms 未満であること
)
```

### Session について

仮想ユーザー数を増やしても同じデータにアクセスしてしまうと、キャッシュのテストになってしまう。そのため、仮想ユーザーの状態に応じてシナリオのステップを動的にすることが重要となる。
Session とはシミュレーションを実行する仮想ユーザーの状態を保持する Map型の変数である。
公式ドキュメント Session： https://gatling.io/docs/current/session/

### Session への値の登録と使い方

Session へ値を登録する方法は３つある。
- Feeders を利用してテストデータを定義したファイルから読み込む
- exec ブロック内で set する
- check ブロックにてリクエストレスポンスを Session に保存する

Session へ登録した値の利用方法は２つある。
- Gatling’s Expression Language を使って埋め込む
 -  https://gatling.io/docs/current/session/expression_el/
- Session API を使って取得する

```scala
// 登録処理
// 　Article を新規登録するリクエスト
// 　title には、ランダムな文字列を生成して指定する。
// 　登録成功時には、主キー（id）とランダムに生成した タイトル（title）をSessionに保存する
.feed(csv("test_data.csv").circular)                                                         // Feeders を利用してcsvファイルから読み込む
.exec((session: Session) => session.set("randomStr", Random.alphanumeric.take(10).mkString)) // ランダム生成した文字列を Session に set する
.exec(http("登録処理")
  .post("/articles")
  .body(StringBody("""{ "title": "${randomStr}", "body": "${body}" }""")).asJSON // ELで title と body を埋め込む
  .check(
    status.in(200 to 210),                  // ステータスコードを範囲指定で検証することも可能
    jsonPath("$..id").saveAs("id"),         // 応答のJSONから id を取得して Session に保存する
    jsonPath("$..title").saveAs("title"),   // 応答のJSONから title を取得して Session に保存する
    responseTimeInMillis.lessThan(3000)     // レスポンスタイムが 3秒以内であればOKとみなす
  )
)
```

## シミュレーション定義ブロック

ここでは、サーバーにかける負荷と実行結果全体にかけるアサーションを定義する。

### サーバーにかける負荷の定義

仮想ユーザー数をコントロールすることで、サーバーにかける負荷を調整する。
代表的なメソッドをサンプルに記載する。

```scala
// シミュレーション定義
setUp(scn.inject(
    // シミュレーションの実行（負荷のかけ方）を定義する
    // 　実行するユーザー数の変動を指定することで、負荷のかけ方を設定する
    atOnceUsers(5),                 // 5ユーザーで1回だけ実行する
    rampUsers(5).over(5 seconds),   // 5秒かけて5ユーザーまで増やしながら実行する
    constantUsersPerSec(1).during(20 seconds),     // 毎秒1ユーザーを20秒間追加する
    rampUsersPerSec(1).to(3).during(20 seconds)     // 毎秒ランダムに1〜3ユーザー（期待値2）を20秒間追加する
  )
  .protocols(httpConf))
```

サンプル以外のメソッドについては、公式ドキュメントを参照
https://gatling.io/docs/current/general/simulation_setup/


### アサーション

シミュレーション実行結果全体に対する検証の定義となる。
MavenやJenkinsなどにテストを失敗と判断させるには、この assertion ブロックでチェックする必要がある。個別のリクエストに記載した check ブロックではリクエストの成功(OK)、失敗(KO)を判断するだけで、テスト自体の失敗判定ではないので注意。

```scala
// アサーション
.assertions(
  global.successfulRequests.percent.gte(80),    // 成功リクエストが全体の80％以上
  global.responseTime.percentile3.lt(5000),     // レスポンスタイムの95pctが5秒未満
  global.responseTime.max.lt(10000)             // レスポンスタイムの最大値が10秒未満
)
```

サンプル以外のメソッドについては、公式ドキュメントを参照
https://gatling.io/docs/current/general/assertions/

## 実行方法

Maven を使って起動する

```
mvn gatling:test
```

負荷テストのベースURL（対象となるサーバーのURL）は、pom.xml で設定可能
Jenkins から実行する場合の例は、Jenkinsfile を参照

## レポートの見方

レポートには target/gatling/ に作成され、下記の内容が表とグラフで表示される。

- 秒間のリクエスト数、レスポンス数、アクティブユーザー数の推移
- レスポンスタイムの基本統計情

作成されるレポートの詳細は下記を参照
https://gatling.io/docs/current/general/reports/
