package com.mariomanhique.firestore.repository.authWithCredentials;

import com.google.firebase.auth.FirebaseAuth;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.realm.kotlin.mongodb.App;
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
public final class AuthRepositoryImpl_Factory implements Factory<AuthRepositoryImpl> {
  private final Provider<FirebaseAuth> firebaseAuthProvider;

  private final Provider<App.Companion> mongoAuthProvider;

  public AuthRepositoryImpl_Factory(Provider<FirebaseAuth> firebaseAuthProvider,
      Provider<App.Companion> mongoAuthProvider) {
    this.firebaseAuthProvider = firebaseAuthProvider;
    this.mongoAuthProvider = mongoAuthProvider;
  }

  @Override
  public AuthRepositoryImpl get() {
    return newInstance(firebaseAuthProvider.get(), mongoAuthProvider.get());
  }

  public static AuthRepositoryImpl_Factory create(Provider<FirebaseAuth> firebaseAuthProvider,
      Provider<App.Companion> mongoAuthProvider) {
    return new AuthRepositoryImpl_Factory(firebaseAuthProvider, mongoAuthProvider);
  }

  public static AuthRepositoryImpl newInstance(FirebaseAuth firebaseAuth, App.Companion mongoAuth) {
    return new AuthRepositoryImpl(firebaseAuth, mongoAuth);
  }
}
