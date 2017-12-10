# Pasar Malam List

## Creating the List
1. Start by adding the RecyclerView and FirebaseUI library in your `app/build.gradle` dependencies.

  ```java
  dependencies {
      // ...
      implementation 'com.android.support:recyclerview-v7:26.1.0'
      implementation 'com.firebaseui:firebase-ui-firestore:3.1.0'
  }
  ```
1. The layout of your `activity_main.xml` will now be a row. Create a file called `list_item.xml` and copy the layout over.
2. Remove the contents in your `activity_main.xml` and add a RecyclerView.
```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.agmostudio.pasarmalam.MainActivity">

    <android.support.v7.widget.RecyclerView
        android:id="@+id/recyclerview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layoutManager="LinearLayoutManager"
        tools:listitem="@layout/list_item" />

</FrameLayout>
```

4. Open `MainActivity.java`. Most of the code here will be replaced. Find the RecyclerView that you just added
  ```java
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      RecyclerView recyclerView = findViewById(R.id.recyclerview);
  }
  ```

3. Create the file `PasarMalamHolder.java` and find the views
  ```java
  public class PasarMalamHolder extends RecyclerView.ViewHolder {

      TextView nameTextView;
      TextView hoursTextView;

      public PasarMalamHolder(View itemView) {
          super(itemView);
          nameTextView = itemView.findViewById(R.id.name);
          hoursTextView = itemView.findViewById(R.id.hours);
      }
  }
  ```

## Using FirebaseUI's FirestoreRecyclerOptions

1. We need to pass the `Query` to use `FirestoreRecyclerOptions` to let it know how to get your data.

  ```java
      Query query = FirebaseFirestore.getInstance().collection("pasarmalam");

      FirestoreRecyclerOptions<PasarMalam> options = new FirestoreRecyclerOptions.Builder<PasarMalam>()
        .setQuery(query, PasarMalam.class)
        .setLifecycleOwner(this)
        .build();
  ```

2. Create the `FirestoreRecyclerAdapter`

  ```java
  FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<PasarMalam, PasarMalamHolder>(options) {
      @Override
      public PasarMalamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
          View view = LayoutInflater.from(parent.getContext())
                  .inflate(R.layout.list_item, parent, false);

          return new PasarMalamHolder(view);
      }

      @Override
      protected void onBindViewHolder(final PasarMalamHolder holder, int position, PasarMalam model) {
          holder.nameTextView.setText(model.getName());
          holder.hoursTextView.setText(model.getHours());

          holder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  DocumentSnapshot doc = getSnapshots().getSnapshot(holder.getAdapterPosition());

                  Intent mapIntent = new Intent(v.getContext(), MapsActivity.class);
                  mapIntent.putExtra("documentId", doc.getId());
                  startActivity(mapIntent);
              }
          });
      }
  };
  ```

4. Set the adapter to RecyclerView to display the data
```java
    recyclerView.setAdapter(adapter);
```

5. Build and run the app.
