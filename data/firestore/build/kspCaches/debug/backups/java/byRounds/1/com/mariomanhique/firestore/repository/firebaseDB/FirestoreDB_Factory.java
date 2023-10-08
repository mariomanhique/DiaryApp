package com.mariomanhique.firestore.repository.firebaseDB;

import com.google.firebase.firestore.FirebaseFirestore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class FirestoreDB_Factory implements Factory<FirestoreDB> {
  private final Provider<FirebaseFirestore> firestoreProvider;

  public FirestoreDB_Factory(Provider<FirebaseFirestore> firestoreProvider) {
    this.firestoreProvider = firestoreProvider;
  }

  @Override
  public FirestoreDB get() {
    return newInstance(firestoreProvider.get());
  }

  public static FirestoreDB_Factory create(Provider<FirebaseFirestore> firestoreProvider) {
    return new FirestoreDB_Factory(firestoreProvider);
  }

  public static FirestoreDB newInstance(FirebaseFirestore firestore) {
    return new FirestoreDB(firestore);
  }
}
