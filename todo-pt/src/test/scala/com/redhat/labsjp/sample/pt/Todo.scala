package pt

import collection.JavaConverters._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.util.Random
import scala.collection.mutable.ArrayBuffer
import java.util.List
import java.util.ArrayList

class Todo extends Simulation {

  val baseUrl = System.getProperty("gatling.baseUrl")

  val httpConf = http
    .baseURL(baseUrl)
    .acceptHeader("application/json,text/javascript,*/*;q=0.01")
    .userAgentHeader("gatling")
    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")

  val headers = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"
  )

	before {
    // TODO
  }

	after {
    // TODO
  }
  
  val scn = scenario("Todoリストの負荷テスト")
    .exec(http("GET /todos")
        .get("/todos")
        .headers(headers)
        .check(
            status.in(200),
            responseTimeInMillis.lessThan(500)
        )
     )

  // シミュレーション定義
  setUp(scn.inject(
      // シミュレーションの実行（負荷のかけ方）を定義する
      // 実行するユーザー数の変動を指定することで、負荷のかけ方を設定する
      rampUsersPerSec(1) to 1 during(3 minutes) randomized
    )
    .protocols(httpConf)
    )
  // アサーション
  // 　シミュレーション実行結果全体に対する検証の定義
  // 　Jenkinsなどにテストを失敗と判断させるには、この assertion ブロックでチェックする必要がある
  // 　個別のリクエストに記載した check ブロックではリクエストの成功、失敗を判断するが、テスト自体の失敗判定ではないので注意。
  .assertions(
    global.successfulRequests.percent.gte(100),    // 成功リクエストが全体の100％以上
    global.responseTime.percentile3.lt(2000),     // レスポンスタイムの95pctが3秒未満
    global.responseTime.max.lt(4500)             // レスポンスタイムの最大値が4.5秒未満
  )
}
