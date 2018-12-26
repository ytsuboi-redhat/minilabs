Feature: TODO表示
前提として、TODOが作成されていること。

  Scenario: TODOトップ画面にてボタンをクリックすると作成されているTODOが表示されること。
    TODOトップ画面にアクセス後、ボタンをクリックしてフォームをサブミットすると、作成されているTODOがリストで表示される。
    Given TODOのトップ画面を開く
    Then  TODOのトップ画面が表示される
    When  トップ画面のボタンをクリックする
    Then  TODOリストが表示される