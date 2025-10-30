# アプリケーションパッケージング手順書

## 1. 概要
本文書は、CUI版TODOリストアプリケーションを実行可能なJARファイルおよびWindows用のEXEファイルにパッケージングする手順を記述するものである。

## 2. 前提条件
- Java Development Kit (JDK) 14以上がインストールされていること。
- （EXE作成の場合）Windows環境であること。
- （EXE作成の場合）[WiX Toolset](https://wixtoolset.org/) v3.0以上がインストールされ、環境変数`PATH`に登録されていること。

## 3. 実行可能JARファイルの作成

### 3.1. ソースコードのコンパイル
まず、プロジェクトのソースコードをすべてコンパイルします。

1.  プロジェクトのルートディレクトリで、コンパイル済みファイルを格納する`out`ディレクトリを作成します。
    ```bash
    mkdir out
    ```

2.  `javac`コマンドを使い、ソースコードを`out`ディレクトリにコンパイルします。
    ```bash
    javac -d out $(find src -name "*.java")
    ```

### 3.2. マニフェストファイルの作成
次に、JARファイルのエントリーポイントを指定するマニフェストファイル`MANIFEST.MF`を作成します。

1.  プロジェクトのルートディレクトリに、以下の内容で`MANIFEST.MF`ファイルを作成します。
    ```
    Main-Class: com.todolist.app.Main
    ```
    **注意:** ファイルの末尾には必ず改行を入れてください。

### 3.3. JARファイルのパッケージング
`jar`コマンドを使い、コンパイル済みのクラスファイルとマニフェストファイルを一つのJARファイルにまとめます。

1.  以下のコマンドを実行し、`dist/todolist-cui.jar`を生成します。
    ```bash
    # distディレクトリがなければ作成
    mkdir -p dist

    # jarコマンドでパッケージング
    jar cvfm dist/todolist-cui.jar MANIFEST.MF -C out .
    ```

### 3.4. 実行確認
生成されたJARファイルが正しく実行できることを確認します。
```bash
java -jar dist/todolist-cui.jar
```
アプリケーションが起動し、コマンドプロンプトが表示されれば成功です。

---
## 4. GUI版アプリケーション

### 4.1. マニフェストファイルの作成
GUIアプリケーション用のマニフェストファイル`MANIFEST-GUI.MF`を作成します。

1.  プロジェクトのルートディレクトリに、以下の内容で`MANIFEST-GUI.MF`ファイルを作成します。
    ```
    Main-Class: com.todolist.app.gui.Main
    ```

### 4.2. JARファイルのパッケージング
以下のコマンドを実行し、`dist/todolist-gui.jar`を生成します。
```bash
jar cvfm dist/todolist-gui.jar MANIFEST-GUI.MF -C out .
```

### 4.3. 実行確認
```bash
java -jar dist/todolist-gui.jar
```
TODOリストのウィンドウが表示されれば成功です。

---

## 5. Windows用EXEファイルの作成 (jpackageを使用)
`jpackage`ツールは、Javaアプリケーションをターゲットプラットフォームのネイティブ形式（Windowsの場合はEXE）にパッケージングします。

### 5.1. CUI版EXEの作成
1.  事前に、`dist/todolist-cui.jar`が作成されていることを確認してください。
2.  Windows環境で以下のコマンドを実行します。
    ```powershell
    jpackage --name todolist-cui --input dist --main-jar todolist-cui.jar --main-class com.todolist.app.cui.Main --type exe --dest dist --win-console
    ```
    - `--win-console`: コンソールアプリケーションとしてEXEを作成します。

### 5.2. GUI版EXEの作成
1.  事前に、`dist/todolist-gui.jar`が作成されていることを確認してください。
2.  Windows環境で以下のコマンドを実行します。
    ```powershell
    jpackage --name todolist-gui --input dist --main-jar todolist-gui.jar --main-class com.todolist.app.gui.Main --type exe --dest dist
    ```
    - GUIアプリの場合、`--win-console`は不要です。

### 5.3. 実行確認
`dist`ディレクトリ内に`todolist-cui`および`todolist-gui`という名前のディレクトリが生成され、その中にそれぞれの`.exe`ファイルが作成されているはずです。

各EXEファイルを実行し、アプリケーションが正しく起動することを確認してください。
