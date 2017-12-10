# Allow user to Suggest

## Creating Menu
1. Create a new menu. Add a new menu item.
  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <menu xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto">

      <item
          android:id="@+id/action_suggest"
          android:title="Suggest"
          app:showAsAction="always" />
  </menu>
  }
  ```

2. Open `MainActivity.java`

3. Override `onCreateOptionsMenu` and add our menu
```java
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_suggest, menu);
    return super.onCreateOptionsMenu(menu);
}
```

4. Override `onOptionsItemSelected` to know if user click on Suggest menu. We will call the `showSuggestionDialog()` method to show the dialog.
```java
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_suggest) {
        showSuggestionDialog();
        return true;
    }
    return super.onOptionsItemSelected(item);
}
```

5. We wanted our Dialog to have an `EditText` for user to type their suggestion. Create the layout file named `dialog_suggest.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <EditText
        android:id="@+id/suggestion"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

</LinearLayout>

```

5. Back in the `MainActivity.java`. In the `showSuggestionDialog`. We will inflate the layout and set it as your Dialog UI.
```java

  private void showSuggestionDialog() {
      View dialogLayout = LayoutInflater.from(this).inflate(R.layout.dialog_suggest, null);
      final EditText suggestText = dialogLayout.findViewById(R.id.suggestion);

      new AlertDialog.Builder(this)
              .setTitle("Suggest a Location")
              .setView(dialogLayout)
              .setNegativeButton("Cancel", null)
              .setPositiveButton("Suggest", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      // todo
                  }
              })
              .show();

  }
```

6. Add the `onClick` to handle user clicks on Suggest.
```java
  String text = suggestText.getText().toString().trim();

  if (TextUtils.isEmpty(text)) {
    return;
  }

  Map<String, Object> data = new HashMap<>();
  data.put("name", text);

  FirebaseFirestore.getInstance()
        .collection("suggestions")
        .add(data)
        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                String msg;
                if (task.isSuccessful()) {
                    msg = "Thank you for your suggestion";
                } else {
                    msg = task.getException().getMessage();
                }

                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

            }
        });

```
