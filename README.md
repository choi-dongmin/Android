# BreadcrumbsAndroid_automatically_update

 ## Dependencies
```
.
.
.
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.5.0'
.
.
```

## MainActivity
```kotlin

String strAdd = "111.111.111.111" 
int nport = 8000

private val aus = AutoUpdateService();

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    aus.DoUpdate(this, strAdd, nPort)
}

```
