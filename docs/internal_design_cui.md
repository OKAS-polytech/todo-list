# CUI版TODOリストアプリケーション 内部設計書

## 1. 概要
本文書は、CUI版TODOリストアプリケーションの内部設計を定義するものである。オブジェクト指向に基づいたクラス設計、責務の分離、および主要な処理フローについて記述する。

## 2. 設計方針
- **オブジェクト指向**: 現実世界の概念（タスク、タスクリスト）をオブジェクトとしてモデル化する。
- **責務の分離**: 関心事ごとにクラスを分割し、各クラスが単一の責務を持つように設計する (Single Responsibility Principle)。
  - **ドメイン層**: アプリケーションの核となるビジネスロジックを担当。
  - **データ層 (リポジトリ)**: データの永続化（今回はインメモリ）を担当。
  - **プレゼンテーション層 (UI)**: ユーザーとの対話を担当。
  - **アプリケーション層 (メイン)**: 全体の処理フローを制御する。

## 3. パッケージ構成
ソースコードは、以下のパッケージ構成で管理する。

```
com.todolist.app
├── domain      // ビジネスロジックとドメインオブジェクト
├── repository  // データアクセス層
├── ui          // ユーザーインターフェース層
└── Main        // アプリケーションのエントリポイント
```

## 4. クラス設計

### 4.1. `com.todolist.app.domain.Task` (ドメインオブジェクト)
一つのタスクを表すクラス。

- **フィールド:**
  - `id` (int): タスクの一意なID。
  - `description` (String): タスクの内容。
  - `isDone` (boolean): タスクの完了状態。
- **メソッド:**
  - `getId()`: IDゲッター。
  - `getDescription()`: 内容ゲッター。
  - `isDone()`: 完了状態ゲッター。
  - `setDone(boolean)`: 完了状態セッター。
  - コンストラクタ

### 4.2. `com.todolist.app.repository.TaskRepository` (データ層)
タスクの集合を管理し、データストアへのアクセスを抽象化するクラス。今回はインメモリでデータを保持する。

- **フィールド:**
  - `tasks` (Map<Integer, Task>): タスクをIDをキーにして格納するマップ。
  - `nextId` (int): 次に採番するタスクID。
- **メソッド:**
  - `create(String description)`: 新しいタスクを作成し、マップに追加する。
  - `findAll()`: すべてのタスクをリストで返す。
  - `findById(int id)`: IDを指定してタスクを検索する。
  - `update(Task task)`: 既存のタスク情報を更新する。
  - `delete(int id)`: IDを指定してタスクを削除する。

### 4.3. `com.todolist.app.domain.TodoService` (ドメイン層)
ビジネスロジックを実装するサービスクラス。`TaskRepository` を利用してタスク操作を行う。

- **フィールド:**
  - `taskRepository` (TaskRepository): データアクセス用のリポジトリ。
- **メソッド:**
  - `addTask(String description)`: タスクを追加するビジネスロジック。
  - `getAllTasks()`: 全タスクを取得する。
  - `completeTask(int id)`: タスクを完了状態にするビジネスロジック。
  - `deleteTask(int id)`: タスクを削除するビジネスロジック。

### 4.4. `com.todolist.app.ui.ConsoleController` (プレゼンテーション層)
ユーザーからの入力を受け付け、`TodoService` を呼び出し、結果をコンソールに出力する責務を持つ。

- **フィールド:**
  - `todoService` (TodoService): ビジネスロジックを呼び出すためのサービス。
  - `scanner` (Scanner): ユーザー入力を受け付けるためのスキャナ。
- **メソッド:**
  - `start()`: アプリケーションのメインループを開始する。コマンドの入力を待ち受け、解析し、適切な処理をディスパッチする。
  - (private) `displayTasks()`: タスク一覧を表示する。
  - (private) `addTask()`: タスク追加の処理を行う。
  - (private) `completeTask()`: タスク完了の処理を行う。
  - (private) `deleteTask()`: タスク削除の処理を行う。

### 4.5. `com.todolist.app.Main` (アプリケーション層)
アプリケーションのエントリーポイント。`ConsoleController` のインスタンスを生成し、処理を開始する。

- **メソッド:**
  - `main(String[] args)`: プログラムの開始地点。依存関係の注入（DI）を行い、`ConsoleController#start()` を呼び出す。

## 5. 処理フロー (メインループ)
1. `Main.main` メソッドが実行される。
2. `TaskRepository`, `TodoService`, `ConsoleController` のインスタンスが生成される（依存性の注入）。
3. `ConsoleController.start()` メソッドが呼び出される。
4. ユーザーにコマンドプロンプトが表示される。
5. ユーザーがコマンドを入力する。
6. 入力されたコマンド文字列を解析する。
7. コマンドに応じて、`ConsoleController` が `TodoService` の適切なメソッドを呼び出す。
8. `TodoService` は `TaskRepository` を通じてデータを操作する。
9. `ConsoleController` は処理結果を整形し、コンソールに表示する。
10. ユーザーが `exit` コマンドを入力するまで、4〜9を繰り返す。
