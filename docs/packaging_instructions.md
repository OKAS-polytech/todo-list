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

## 4. Windows用EXEファイルの作成 (jpackageを使用)
`jpackage`ツールは、Javaアプリケーションをターゲットプラットフォームのネイティブ形式（Windowsの場合はEXE）にパッケージングします。

### 4.1. jpackageの実行
Windows環境のコマンドプロンプトまたはPowerShellで、以下のコマンドを実行します。

1.  事前に、上記の手順で`dist/todolist-cui.jar`が作成されていることを確認してください。

2.  プロジェクトのルートディレクトリで、以下の`jpackage`コマンドを実行します。
    ```bash
    jpackage --name todolist-cui `
      --input dist `
      --main-jar todolist-cui.jar `
      --main-class com.todolist.app.Main `
      --type exe `
      --dest dist `
      --win-console
    ```
    - `--name`: アプリケーション名を指定します。
    - `--input`: 入力となるファイルが含まれるディレクトリ（JARファイルがある場所）を指定します。
    - `--main-jar`: メインのJARファイルを指定します。
    - `--main-class`: エントリーポイントとなるクラスを指定します。
    - `--type`: 生成するパッケージの形式を指定します。Windowsの場合は`exe`または`msi`が選択できます。
    - `--dest`: 出力先ディレクトリを指定します。
    - `--win-console`: これを指定することで、コンソールアプリケーションとしてEXEが作成されます。

### 4.2. 実行確認
`dist`ディレクトリ内に`todolist-cui`という名前のディレクトリが生成され、その中に`todolist-cui.exe`ファイルが作成されているはずです。

コマンドプロンプトから`todolist-cui.exe`を実行し、アプリケーションが正しく起動することを確認してください。
