package com.mariomanhique.home;

import com.mariomanhique.database.ImageToDeleteDao;
import com.mariomanhique.firestore.repository.firebaseDB.FirestoreRepository;
import com.mariomanhique.util.connectivity.NetworkConnectivityObserver;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<FirestoreRepository> firestoreRepositoryProvider;

  private final Provider<NetworkConnectivityObserver> connectivityProvider;

  private final Provider<ImageToDeleteDao> imageToDeleteDaoProvider;

  public HomeViewModel_Factory(Provider<FirestoreRepository> firestoreRepositoryProvider,
      Provider<NetworkConnectivityObserver> connectivityProvider,
      Provider<ImageToDeleteDao> imageToDeleteDaoProvider) {
    this.firestoreRepositoryProvider = firestoreRepositoryProvider;
    this.connectivityProvider = connectivityProvider;
    this.imageToDeleteDaoProvider = imageToDeleteDaoProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(firestoreRepositoryProvider.get(), connectivityProvider.get(), imageToDeleteDaoProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<FirestoreRepository> firestoreRepositoryProvider,
      Provider<NetworkConnectivityObserver> connectivityProvider,
      Provider<ImageToDeleteDao> imageToDeleteDaoProvider) {
    return new HomeViewModel_Factory(firestoreRepositoryProvider, connectivityProvider, imageToDeleteDaoProvider);
  }

  public static HomeViewModel newInstance(FirestoreRepository firestoreRepository,
      NetworkConnectivityObserver connectivity, ImageToDeleteDao imageToDeleteDao) {
    return new HomeViewModel(firestoreRepository, connectivity, imageToDeleteDao);
  }
}
