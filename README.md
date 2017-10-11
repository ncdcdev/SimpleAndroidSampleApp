このリポジトリでは、AppPotのAndroid SDKを使用したAndroidアプリのサンプルを公開しています。

## ログイン画面
以下の処理がjp.apppot.android.sample.LoginActivityに実装されています。
1. AppPot SDKの初期化
    initAppInfo()メソッドを参照
2. データベースの作成
    createDatabase()を参照
3. ログイン処理
    login(String userId, String password)メソッドを参照

## モデル定義
Androidアプリで定義したモデルに沿って、アプリ内部のDBおよび、サーバーのDBが生成されます。
jp.apppot.android.sample.Companyクラスが、このサンプルで使用しているモデル定義です。

このクラスには以下の処理が実装されています。
- objectIdを指定した単件取得
    getByObjectId(String objectId)
- 全件取得
    getList(APResponseSelectHandler handler)
- 登録および更新
    save()
- 削除
    delete()

## Company一覧画面
jp.apppot.android.sample.CompanyListActivityで、Companyの一覧画面を実装しています。
Companyクラスに含まれるメソッドを利用して、データを取得し、画面の描画を行っています。

## Company詳細画面
jp.apppot.android.sample.CompanyDetailActivityで、Companyの詳細画面を実装しています。
Companyクラスに含まれるメソッドを利用して、データの単件取得、更新、削除を行っています。
