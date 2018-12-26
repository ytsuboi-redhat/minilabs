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
  - ci
    - CI パイプライン関連コンテナ
      - git
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
  - $PWD = 本README.mdが存在するディレクトリ
  
  1. フロントエンドビルド用に Vue CLI をインストール
  ```
  $ npm install -g @vue/cli
  ```
  2. フロントエンドのソースコードをビルド
  ```
  $ cd $PWD/todo-frontend
  $ npm run build
  ```
  3. バックエンドのソースコードをビルド
  ```
  $ cd $PWD/todo-backend
  $ mvn clean package
  ```
  4. フロントエンド・バックエンド・MySQL各コンテナをビルドして起動
  ```
  $ cd $PWD
  $ docker-compose up --build
  ```
  5. `http://localhost/todo` にアクセス

## CIパイプラインの動作確認方法

### 前提
  - Git が利用可能であること
  - Docker が利用可能であること
  - Docker Compose が利用可能であること（2018/12/18時点の暫定）

### 手順
  - $PWD = 本README.mdが存在するディレクトリ

  1. Git・Jenkins・SonarQube各コンテナをビルドして起動
  ```
  $ cd $PWD/ci
  $ docker-compose up --build
  ```
  2. 立ち上がったCI用Gitからtodo-app用の空のリポジトリをクローンし、todo-appソースコード（必要なものだけ）をコミット・プッシュ
  ```
  $ cd /tmp
  $ git clone git://localhost/todo-app.git
  $ cp -r $PWD/* ./todo-app
  $ cd todo-app
  $ git add todo-backend
  $ git update-index --add --chmod=+x todo-backend/wait-for-it.sh
  $ git add todo-frontend
  $ git add todo-mysql
  $ git add Jenkinsfile
  $ git commit -m "initial commit"
  $ git push
  ```
  3. Jenkins (http://localhost:1080) にアクセスし、画面を通して初期設定を実施
     - アカウントは適当に設定
     - Suggested Plugin をインストール
  4. 初期設定完了後、`Jenkinsの管理 -> プラグインの管理` で追加プラグインをインストール
     - NodeJS
     - SonarQube Scanner
  5. `Jenkinsの管理 -> システム設定 -> SonarQube servers` でSonarQubeを設定
     - Name: default
     - Server URL: http://workshop-sonar:9000
  6. `Jenkinsの管理 -> Global Tool Configuration` で以下のツールを設定
     - `SonarQube Scanner`
       - Name: sonarqube-scanner
       - 自動インストール: Yes
       - バージョン: SonarQube Scanner 3.2.x
     - `Maven`
       - 名前: maven3.6.0
       - 自動インストール: Yes
       - バージョン: 3.6.0
     - `NodeJS`
       - 名前: Node 10.14.x
       - 自動インストール: Yes
       - Version: NodeJS 10.14.x
       - Force 32bit architecture: No
       - Global npm packages to install: @vue/cli
  7. Jenkinsのトップ画面から新規ジョブ作成
     - パイプラインを選択
     - 詳細設定画面 -> パイプライン
       - 定義: Pipeline script from SCM
       - SCM: Git
         - リポジトリURL: git://git/todo-app.git
         - 認証情報: なし
         - ビルドするブランチ: */master
       - Script Path: Jenkinsfile
  8. ビルド実行

以上