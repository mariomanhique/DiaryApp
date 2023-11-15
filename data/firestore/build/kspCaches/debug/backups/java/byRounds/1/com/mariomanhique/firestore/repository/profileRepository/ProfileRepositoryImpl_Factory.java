package com.mariomanhique.firestore.repository.profileRepository;

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
public final class ProfileRepositoryImpl_Factory implements Factory<ProfileRepositoryImpl> {
  private final Provider<FirebaseFirestore> firestoreProvider;

  public ProfileRepositoryImpl_Factory(Provider<FirebaseFirestore> firestoreProvider) {
    this.firestoreProvider = firestoreProvider;
  }

  @Override
  public ProfileRepositoryImpl get() {
    return newInstance(firestoreProvider.get());
  }

  public static ProfileRepositoryImpl_Factory create(
      Provider<FirebaseFirestore> firestoreProvider) {
    return new ProfileRepositoryImpl_Factory(firestoreProvider);
  }

  public static ProfileRepositoryImpl newInstance(FirebaseFirestore firestore) {
    return new ProfileRepositoryImpl(firestore);
  }
}
