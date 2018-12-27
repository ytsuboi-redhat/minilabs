# base-app

## 構成
  - todo-backend
    - Spring Boot によるバックエンドアプリケーションとコンテナ
      - Spring Boot: 2.1.1.RELEASE
        - Spring Data JDBC
  - todo-frontend
    - Vue.js によるフロントエンドアプリケーションとコンテナ
      - Vue.js: 2.5.17
        - Vue CLI: 
      - Nginx: 1.14.0
        - フロントエンドをホスティング
        - バックエンドへの通信をリバースプロキシ
  - todo-mysql
    - MySQL によるデータベースコンテナ
      - MySQL: 5.7
  - todo-at
    - Acceptance Test 実行用プロジェクト
  - ci
    - CI パイプライン関連コンテナ
      - Jenkins
      - SonarQube

## アプリケーションの動作確認方法

### 前提
  - Node.js (>= 8.11.0) が利用可能であること
  - JDK (= 1.8.0) が利用可能であること
  - Apache Maven (>= 3.3.9) が利用可能であること
  - Docker が利用可能であること
  - Docker Compose が利用可能であること（2018/12/18時点の暫定）

### 手順
  - {PWD} = 本README.mdが存在するディレクトリ
  
  1. フロントエンドビルド用に Vue CLI をインストール
  ```
  $ npm install -g @vue/cli
  ```
  2. フロントエンドのソースコードをビルド
  ```
  $ cd {PWD}/todo-frontend
  $ npm run build
  ```
  3. バックエンドのソースコードをビルド
  ```
  $ cd {PWD}/todo-backend
  $ mvn clean package
  ```
  4. フロントエンド・バックエンド・MySQL各コンテナをビルドして起動
  ```
  $ cd {PWD}
  $ docker-compose up --build
  ```
  5. `http://localhost/todo` にアクセス

## CIパイプラインの動作確認方法

### 前提
  - Git クライアントが利用可能であること
  - GitHub にワークショップで利用するためのPublicな空リポジトリが用意されていること
  - Docker が利用可能であること
  - Docker Compose が利用可能であること（2018/12/18時点の暫定）

### 手順
  - {PWD} = 本README.mdが存在するディレクトリ
  - {GITHUB_ACCOUNT} = GitHub アカウント
  - {GITHUB_REPO_NAME} = ワークショップ用リポジトリ名

  1. 事前に準備してあるGitHubのワークショップ用リポジトリをクローンし、todo-appソースコードをコミット・プッシュ
  ```
  $ cd /tmp
  $ git clone https://github.com/{GITHUB_ACCOUNT}/{GITHUB_REPO_NAME}.git
  $ cp -r {PWD}/* ./{GITHUB_REPO_NAME}
  $ cd {GITHUB_REPO_NAME}
  $ git add *
  $ git update-index --add --chmod=+x todo-backend/wait-for-it.sh
  $ git commit -m "initial commit"
  $ git push
  ```
  2. Jenkins・SonarQube各コンテナをビルドして起動
    - ログ（標準出力）に出力される初期パスワードを保存しておく
  ```
  $ cd {PWD}/ci
  $ docker-compose up --build
  ...snip...
  workshop-jenkins | *************************************************************
  workshop-jenkins | *************************************************************
  workshop-jenkins | *************************************************************
  workshop-jenkins |
  workshop-jenkins | Jenkins initial setup is required. An admin user has been created and a password generated.
  workshop-jenkins | Please use the following password to proceed to installation:
  workshop-jenkins |
  workshop-jenkins | 8b53e5f0056f4d00be763f121a29a9f3
  workshop-jenkins |
  workshop-jenkins | This may also be found at: /var/jenkins_home/secrets/initialAdminPassword
  workshop-jenkins |
  workshop-jenkins | *************************************************************
  workshop-jenkins | *************************************************************
  ...snip...
  workshop-jenkins | INFO: Jenkins is fully up and running
  ...snip...
  workshop-sonar | 2018.12.26 02:06:27 INFO  app[][o.s.a.SchedulerImpl] SonarQube is up
  ```
  3. Jenkins (http://localhost:1080) にアクセスし、画面を通して初期設定を実施
     - 初期パスワードを入力
     - Suggested Plugin を選択
       - プラグインはインストール済みのためすぐに完了
     - 管理ユーザは適当に作成
     - Jenkins URL はそのまま
  4. `Jenkinsの管理 -> システム設定 -> SonarQube servers` でSonarQubeを設定
     - SonarQube installations で「Add SonarQube」をクリックする
     - 表示されたフォームに以下の情報を入力
       - Name: default
       - Server URL: http://workshop-sonar:9000
     - 画面下部「保存」をクリックする
  5. `Jenkinsの管理 -> Global Tool Configuration` で以下のツールを設定
     - `SonarQube Scanner`
       - インストール済みSonarQube Scanner で「SonarQube Scanner追加」をクリックする
       - 表示されたフォームに以下の情報を入力
         - Name: sonarqube-scanner
         - 自動インストール: Yes
         - "Install from Maven Central"
           - バージョン: SonarQube Scanner 3.2.x
     - `Maven`
       - インストール済みMaven で「Maven追加」をクリックする
       - 表示されたフォームに以下の情報を入力
         - 名前: maven3.6.0
         - 自動インストール: Yes
         - "Apacheからインストール"
           - バージョン: 3.6.0
     - `NodeJS`
       - インストール済みNodeJS で「NodeJS追加」をクリックする
       - 表示されたフォームに以下の情報を入力
         - 名前: Node 10.14.x
         - 自動インストール: Yes
         - "Install from nodejs.org"
           - Version: NodeJS 10.14.x
           - Force 32bit architecture: No
           - Global npm packages to install: @vue/cli
           - Global npm packages refresh hours: 72
     - 画面下部「Save」をクリックする
  6. Jenkinsのトップ画面から新規ジョブ作成
     - "Enter an item name" で "todo-app" と入力、パイプラインを選択して「OK」をクリックする
     - 遷移先にて以下の情報を選択/入力
       - パイプライン
         - 定義: Pipeline script from SCM
         - SCM: Git
           - リポジトリ
             - リポジトリURL: https://github.com/{GITHUB_ACCOUNT}/{GITHUB_REPO_NAME}.git
             - 認証情報: なし
           - ビルドするブランチ
             - ブランチ指定子: */master
         - Script Path: Jenkinsfile
     - 画面下部「保存」をクリックする
  7. todo-appジョブ画面からビルド実行

以上