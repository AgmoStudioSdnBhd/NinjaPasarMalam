# Firebase Firestore

## Sample Data

1. Go to https://console.firebase.google.com

2. On the side menu, click on **Database**

3. Select Firestore

4. Pick one Pasar Malam from this set of data. https://github.com/AgmoStudioSdnBhd/Firebase-Pasar-Malam-Locator/raw/master/Database/pasar-malam-locator-export.json

5. Create the Collection `pasarmalam` and the Document

## Adding Firebase Database

> Manual way
- add `implementation 'com.google.firebase:firebase-firestore:11.0.4'` to your dependencies in your app/build.gradle
- Ensure you have `apply plugin: 'com.google.gms.google-services'` at the end of your app/build.gradle file
- Ensure you have added `classpath 'com.google.gms:google-services:3.1.0'` in the project level build.gradle

1. Open the Firebase Assistant side menu or via **Tools** > **Firebase**

2. Select **Realtime Database** > **Save and retrieve data**.

3. Click on Step 1 (Connect to Firebase) and Step 2 button (Add the realtime database to your app)

4. Remove the added firebase-database from `build.gradle` file and add firestore.

```gradle
dependencies {

//    implementation 'com.google.firebase:firebase-database:11.0.4'
    implementation 'com.google.firebase:firebase-firestore:11.4.2'
}
```

## PasarMalam model

1. Create a new Java class named `PasarMalam.java`
  ```java
  public class PasarMalam {

      private String name;
      private String desc;
      private String hours;
      private GeoPoint coordinate;

      public String getName() {
          return name;
      }

      public String getDesc() {
          return desc;
      }

      public String getHours() {
          return hours;
      }

      public GeoPoint getCoordinate() {
          return coordinate;
      }
  }
  ```

## Displaying Data from Firebase

1. In the `onCreate` method, add the code to listen to Firestore.
  ```java
  public class MainActivity extends AppCompatActivity {

      @Override
      protected void onCreate(Bundle savedInstanceState) {
          ...

          FirebaseFirestore db = FirebaseFirestore.getInstance();
          db.collection("pasarmalam")
                  .get()
                  .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<QuerySnapshot> task) {
                          if (task.isSuccessful()) {
                              for (DocumentSnapshot document : task.getResult()) {
                                  Log.d("doc", document.getId() + " => " + document.getData());
                              }
                          } else {
                              Log.w("doc", "Error getting documents.", task.getException());
                          }
                      }
                  });
      }
  }

  ```

2. Run the app

3. Check your LogCat and check for response

## Displaying to UI

1. Open activity_main.xml

2. Update your UI to have 2 TextView to display the Name and Operating Hours.

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="com.agmostudio.pasarmalam.MainActivity">

    <TextView
        android:id="@+id/name"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textAppearance="@style/TextAppearance.AppCompat.Medium"
        android:textStyle="bold"
        tools:text="SS17" />

    <TextView
        android:id="@+id/hours"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        tools:text="6:00pm - 11:00pm" />
</LinearLayout>
  ```
3. In your `MainActivity.java`, find the view
  ```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView nameTextView = findViewById(R.id.name);
        final TextView hoursTextView = findViewById(R.id.hours);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ...
    }
  ```

4. In the Firestore OnCompleteListener, display the info to the `TextView`
  ```java
  if (task.isSuccessful()) {
      for (DocumentSnapshot document : task.getResult()) {
          Log.d("doc", document.getId() + " => " + document.getData());

          PasarMalam item = document.toObject(PasarMalam.class);
          nameTextView.setText(item.getName());
          hoursTextView.setText(item.getHours());
      }
  }
  ```

5. Build and run the app.
