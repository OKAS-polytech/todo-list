# GUI版TODOリストアプリケーション 内部設計書

## 1. 概要
本文書は、GUI版TODOリストアプリケーションの内部設計を定義するものである。MVC（Model-View-Controller）アーキテクチャに基づいたクラス設計、責務の分離、および主要な処理フローについて記述する。

## 2. 設計方針
- **MVCアーキテクチャ**:
  - **Model**: アプリケーションのビジネスロジックとデータを担当。既存の`domain`および`repository`パッケージ（`TodoService`, `Task`, `TaskRepository`）をそのまま再利用する。
  - **View**: ユーザーインターフェースの表示とレイアウトを担当。Swingコンポーネントで構築する。
  - **Controller**: Viewからのユーザー入力を受け取り、Modelを操作し、必要に応じてViewの表示を更新する。
- **責務の分離**: Model, View, Controllerの責務を明確に分離し、コンポーネント間の結合度を低く保つ。

## 3. パッケージ構成
GUI関連のソースコードは、新しく作成する`gui`パッケージ内に配置する。

```
com.todolist.app
├── cui         // CUI関連クラス
├── domain      // (Model) ビジネスロジック
├── repository  // (Model) データアクセス層
└── gui         // GUI関連クラス
    ├── view
    ├── controller
    └── Main      // GUIアプリケーションのエントリポイント
```

## 4. クラス設計

### 4.1. Model (既存コード)
- `com.todolist.app.domain.TodoService`: ビジネスロジックの中心。Controllerから利用される。
- `com.todolist.app.domain.Task`: タスクのデータ構造。
- `com.todolist.app.repository.TaskRepository`: データストア。

### 4.2. View
- **`com.todolist.app.gui.view.TodoFrame`**:
  - アプリケーションのメインウィンドウ (`JFrame`)。
  - UIコンポーネント（テキストフィールド、ボタン、リスト）の配置とレイアウトを行う。
  - Controllerからの指示を受けて、タスクリストの表示を更新する責務を持つ。
  - ユーザーのアクション（ボタンクリックなど）をControllerに通知するためのリスナー登録メソッドを提供する。
  - Modelの存在を直接知らない。

- **フィールド**:
  - `JFrame frame`
  - `JList<Task> taskList`
  - `DefaultListModel<Task> listModel`
  - `JTextField taskInputField`
  - `JButton addButton, completeButton, deleteButton`

- **メソッド**:
  - `initialize()`: UIコンポーネントを初期化し、ウィンドウに配置する。
  - `getTaskInputField()`: 入力フィールドのインスタンスを返す。
  - `getTaskList()`: タスクリストのインスタンスを返す。
  - `addAddButtonListener(ActionListener)`: 追加ボタンにリスナーを登録する。
  - `addCompleteButtonListener(ActionListener)`: 完了ボタンにリスナーを登録する。
  - `addDeleteButtonListener(ActionListener)`: 削除ボタンにリスナーを登録する。
  - `updateTaskList(List<Task>)`: `listModel`をクリアし、引数で受け取ったタスクリストで更新する。
  - `clearInputField()`: 入力フィールドを空にする。
  - `showErrorMessage(String)`: エラーダイアログを表示する。

### 4.3. Controller
- **`com.todolist.app.gui.controller.GuiController`**:
  - View (`TodoFrame`) と Model (`TodoService`) の仲介役。
  - Viewにイベントリスナーを登録し、ユーザー操作を捕捉する。
  - ユーザー操作に応じて`TodoService`のメソッドを呼び出し、タスクの追加、完了、削除を行う。
  - Modelの処理結果を受け取り、`TodoFrame`の表示更新メソッドを呼び出す。

- **フィールド**:
  - `TodoService todoService`
  - `TodoFrame todoFrame`

- **メソッド**:
  - コンストラクタ(`TodoService`, `TodoFrame`): 依存性を注入し、イベントリスナーを登録する。
  - `initView()`: アプリケーション開始時にタスクリストの初期表示を指示する。
  - `(private) addTask()`: 追加ボタンが押されたときの処理。
  - `(private) completeTasks()`: 完了ボタンが押されたときの処理。
  - `(private) deleteTasks()`: 削除ボタンが押されたときの処理。

### 4.4. Main (エントリポイント)
- **`com.todolist.app.gui.Main`**:
  - GUIアプリケーションのエントリーポイント。
  - Model, View, Controllerの各インスタンスを生成し、互いに関連付ける（依存性の注入）。
  - SwingのイベントディスパッチスレッドでViewの表示を開始する。

## 5. 処理フロー (タスク追加の例)
1.  `gui.Main`がModel, View, Controllerをインスタンス化する。
2.  `GuiController`が`TodoFrame`の"追加"ボタンに`ActionListener`を登録する。
3.  ユーザーがテキストフィールドに文字を入力し、"追加"ボタンをクリックする。
4.  登録された`ActionListener` (in `GuiController`) がイベントを検知する。
5.  `GuiController`は`TodoFrame`から入力テキストを取得する。
6.  `GuiController`が`TodoService.addTask(text)`を呼び出す。
7.  `TodoService`は`TaskRepository`を通じてタスクをデータストアに追加する。
8.  `GuiController`は`TodoService.getAllTasks()`を呼び出して最新のタスクリストを取得する。
9.  `GuiController`が`TodoFrame.updateTaskList(tasks)`を呼び出す。
10. `TodoFrame`は`listModel`を更新し、画面の表示が変更される。
11. `GuiController`が`TodoFrame.clearInputField()`を呼び出し、入力欄をクリアする。
