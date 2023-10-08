package com.mariomanhique.auth.authWithCredentials;

import com.mariomanhique.firestore.repository.authWithCredentials.AuthRepository;
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
public final class AuthWithCredentialsViewModel_Factory implements Factory<AuthWithCredentialsViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  public AuthWithCredentialsViewModel_Factory(Provider<AuthRepository> authRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public AuthWithCredentialsViewModel get() {
    return newInstance(authRepositoryProvider.get());
  }

  public static AuthWithCredentialsViewModel_Factory create(
      Provider<AuthRepository> authRepositoryProvider) {
    return new AuthWithCredentialsViewModel_Factory(authRepositoryProvider);
  }

  public static AuthWithCredentialsViewModel newInstance(AuthRepository authRepository) {
    return new AuthWithCredentialsViewModel(authRepository);
  }
}
