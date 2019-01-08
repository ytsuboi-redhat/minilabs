package st

import collection.JavaConverters._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.util.Random
import scala.collection.mutable.ArrayBuffer
import java.util.List
import java.util.ArrayList
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.UserRepresentation
import org.keycloak.representations.idm.CredentialRepresentation

class UserLogin extends Simulation {

  val baseUrl = System.getProperty("gatling.baseUrl")
  val originUrl = System.getProperty("gatling.originUrl", "http://auth-idp:8000")
  var serverUrl:String = baseUrl + "/auth"
	var realm:String = "ffg"
	var grantType:String = "password"
	var clientId:String = "admin-cli"
	var admin:String = "admin"
	var password:String = "Keyc1o@k"
	var userRep:UserRepresentation = null
	var username:String = "performance-login-tester"
	var keycloak:Keycloak = null

  // HTTP プロトコル設定
  // 　全てのリクエストに共通して使用されるHTTPヘッダーなどの設定を行う
  val httpConf = http
    .baseURL(baseUrl)
    .acceptHeader("application/json,text/javascript,*/*;q=0.01")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
    .header("X-Requested-With", "XMLHttpRequest")
    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")

  val headers = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
		"Origin" -> originUrl,
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	before {
    keycloak = KeycloakBuilder.builder()
			.serverUrl(serverUrl)
			.realm(realm)
			.clientId(clientId)
			.grantType(grantType)
			.username(admin)
			.password(password)
			.resteasyClient(new ResteasyClientBuilder().build())
			.build()

    var cred:CredentialRepresentation = new CredentialRepresentation()
		cred.setType(grantType)
		cred.setValue(password)
		cred.setTemporary(false)

		var credList:List[CredentialRepresentation] = new ArrayList[CredentialRepresentation]()
		credList.add(cred)

		userRep = new UserRepresentation()
		userRep.setUsername(username)
		userRep.setEnabled(true)
		userRep.setEmail(username + "@example.com")
		userRep.setEmailVerified(true)
		userRep.setCredentials(credList)

		keycloak.realm(realm).users().create(userRep)
  }


	after {
	  var user = keycloak.realm(realm).users().list().asScala.toList.filter(p => p.getUsername() == username)(0)
	  keycloak.realm(realm).users().delete(user.getId());

    if(keycloak != null) {
      keycloak.close()
    }
  }
  // シナリオ定義
  // 　シナリオをメソッドチェーンで記載する
  val scn = scenario("ログイン画面の負荷テスト")
    .exec(http("ログイン画面にアクセス")
        .get("/auth/realms/ffg/account")
        .headers(headers)
        .check(
            status.in(200),
            css("#kc-form-login", "action").saveAs("submitURL")
      )
     )
    .pause(5)
    .exec(http("ログインボタンをクリック")
      .post("${submitURL}")
      .headers(headers)
			.formParam("username", username)
			.formParam("password", password)
      .check(
          status.in(200),
          responseTimeInMillis.lessThan(5000)
       )
    )

  // シミュレーション定義
  setUp(scn.inject(
      // シミュレーションの実行（負荷のかけ方）を定義する
      // 実行するユーザー数の変動を指定することで、負荷のかけ方を設定する
      rampUsers(30) over (1 minutes), // ウォーミングアップ
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
