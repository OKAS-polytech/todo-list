# GUI版TODOリストアプリケーション 内部設計書

## 1. 概要
本文書は、GUI版TODOリストアプリケーションの内部設計を定義するものである。MVCアーキテクチャに基づいたクラス設計、責務の分離、および主要な処理フローについて記述する。

## 2. 設計方針
- **MVCアーキテクチャ**:
  - **Model**: ビジネスロジックとデータを担当。`TodoService`を通じてSQLiteデータベースにアクセスする。
  - **View**: UIの表示とレイアウトを担当。
  - **Controller**: Viewからのユーザー入力を受け取り、Modelを操作する。
- **永続化**: Model層はSQLiteデータベース (`todolist.db`) を使用する。

## 3. クラス設計

### 3.1. Model
- `com.todolist.app.domain.Task`:
  - `memo`フィールドを追加。
- `com.todolist.app.repository.DatabaseManager`:
  - データベース接続とテーブル初期化を担当。
- `com.todolist.app.repository.TaskRepository`:
  - データベースに対するCRUD操作を担当。
- `com.todolist.app.domain.TodoService`:
  - `updateMemo`メソッドを追加。

### 3.2. View
- **`com.todolist.app.gui.view.TodoFrame`**:
  - **UIコンポーネント**: `JTextArea` (メモ用), `JButton` (メモ保存用) を追加。レイアウトを`JSplitPane`に変更。
  - **責務**:
    - UIのレイアウト。
    - Controllerからの指示に基づくタスクリストの表示更新。
    - ユーザーアクションをControllerに通知するためのリスナー登録メソッドの提供。
    - ユーザーがリスト項目を選択した際に、そのイベントをControllerに通知する。

### 3.3. Controller
- **`com.todolist.app.gui.controller.GuiController`**:
  - **責務**:
    - ViewとModelの仲介。
    - Viewのボタン（追加、完了、削除、メモ保存）に`ActionListener`を登録し、対応するModelのメソッドを呼び出す。
    - Viewの`JList`に`ListSelectionListener`を登録し、ユーザーのタスク選択を検知して、選択されたタスクのメモをViewに表示させる。
    - Modelの処理結果に基づき、Viewの表示更新を指示する。

### 3.4. Main (エントリポイント)
- **`com.todolist.app.gui.Main`**:
  - **処理フロー**:
    1. `DatabaseManager.initializeDatabase()`を呼び出す。
    2. Swingのイベントディスパッチスレッドで、Model, View, Controllerをインスタンス化する。
    3. `controller.initView()`を呼び出し、初期データを表示させる。
    4. Viewのウィンドウを可視化する。

## 4. 処理フロー (メモ保存の例)
1. ユーザーが`JList`でタスクを選択する。
2. `GuiController`に登録された`ListSelectionListener`がイベントを検知する。
3. `GuiController`は選択された`Task`オブジェクトを取得し、そのメモを`TodoFrame`の`JTextArea`に表示するよう指示する。
4. ユーザーが`JTextArea`の内容を編集し、「メモ保存」ボタンをクリックする。
5. `GuiController`に登録された`ActionListener`がイベントを検知する。
6. `GuiController`は選択中のタスクIDと`JTextArea`のテキストを取得し、`TodoService.updateMemo(id, newMemo)`を呼び出す。
7. `TodoService`が`TaskRepository`を通じてデータベースの該当レコードを更新する。
8. `GuiController`が`refreshTaskList()`を呼び出し、画面表示を最新の状態に更新する。
