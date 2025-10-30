# アプリケーションパッケージング手順書

## 1. 概要
本文書は、CUI版およびGUI版TODOリストアプリケーションを実行可能なJARファイルおよびWindows用のEXEファイルにパッケージングする手順を記述するものである。

## 2. 依存ライブラリの準備
本アプリケーションはSQLiteデータベースを使用するため、事前にSQLite JDBCドライバをダウンロードする必要があります。

1.  [Maven Central Repository](https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/)にアクセスします。
2.  最新バージョンのディレクトリを選択し、`sqlite-jdbc-X.X.X.X.jar`ファイルをダウンロードします。
3.  プロジェクトのルートディレクトリに`lib`フォルダを作成し、ダウンロードしたJARファイルを配置します。
    ```
    my-todolist-project/
    ├── lib/
    │   └── sqlite-jdbc-3.46.0.0.jar  (←例)
    ├── src/
    └── ...
    ```

## 3. CUI版アプリケーション

### 3.1. コンパイル
```bash
mkdir -p out
javac -d out -cp "lib/*" $(find src -name "*.java")
```

### 3.2. マニフェストファイルの作成 (`MANIFEST.MF`)
```
Main-Class: com.todolist.app.cui.Main
Class-Path: lib/sqlite-jdbc-3.46.0.0.jar
```
**注意:** `Class-Path`の値は、ダウンロードしたJARファイル名に合わせてください。

### 3.3. JARファイルのパッケージング
```bash
mkdir -p dist
jar cvfm dist/todolist-cui.jar MANIFEST.MF -C out .
```

### 3.4. 実行方法
`dist`ディレクトリと`lib`ディレクトリが同じ階層にある状態で、以下のコマンドを実行します。
```bash
java -jar dist/todolist-cui.jar
```

---
## 4. GUI版アプリケーション

### 4.1. マニフェストファイルの作成 (`MANIFEST-GUI.MF`)
```
Main-Class: com.todolist.app.gui.Main
Class-Path: lib/sqlite-jdbc-3.46.0.0.jar
```
**注意:** `Class-Path`の値は、ダウンロードしたJARファイル名に合わせてください。

### 4.2. JARファイルのパッケージング
```bash
jar cvfm dist/todolist-gui.jar MANIFEST-GUI.MF -C out .
```

### 4.3. 実行方法
```bash
java -jar dist/todolist-gui.jar
```

---

## 5. Windows用EXEファイルの作成 (jpackageを使用)
`jpackage`は、`--class-path`で指定されたJARファイルをEXE内にバンドルします。

### 5.1. CUI版EXEの作成
```powershell
jpackage --name todolist-cui --input dist --main-jar todolist-cui.jar --main-class com.todolist.app.cui.Main --type exe --dest dist --win-console --class-path "lib/*"
```

### 5.2. GUI版EXEの作成
```powershell
jpackage --name todolist-gui --input dist --main-jar todolist-gui.jar --main-class com.todolist.app.gui.Main --type exe --dest dist --class-path "lib/*"
```
