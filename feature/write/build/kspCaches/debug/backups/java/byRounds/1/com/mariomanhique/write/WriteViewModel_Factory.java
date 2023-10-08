package com.mariomanhique.write;

import androidx.lifecycle.SavedStateHandle;
import com.mariomanhique.firestore.repository.firebaseDB.FirestoreDB;
import com.mariomanhique.firestore.repository.imageRepo.ImageRepository;
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
public final class WriteViewModel_Factory implements Factory<WriteViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<FirestoreDB> firestoreRepositoryProvider;

  private final Provider<ImageRepository> imageRepositoryProvider;

  public WriteViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<FirestoreDB> firestoreRepositoryProvider,
      Provider<ImageRepository> imageRepositoryProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.firestoreRepositoryProvider = firestoreRepositoryProvider;
    this.imageRepositoryProvider = imageRepositoryProvider;
  }

  @Override
  public WriteViewModel get() {
    return newInstance(savedStateHandleProvider.get(), firestoreRepositoryProvider.get(), imageRepositoryProvider.get());
  }

  public static WriteViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<FirestoreDB> firestoreRepositoryProvider,
      Provider<ImageRepository> imageRepositoryProvider) {
    return new WriteViewModel_Factory(savedStateHandleProvider, firestoreRepositoryProvider, imageRepositoryProvider);
  }

  public static WriteViewModel newInstance(SavedStateHandle savedStateHandle,
      FirestoreDB firestoreRepository, ImageRepository imageRepository) {
    return new WriteViewModel(savedStateHandle, firestoreRepository, imageRepository);
  }
}
