package com.mariomanhique.database.imageRepo;

import com.mariomanhique.database.ImageToDeleteDao;
import com.mariomanhique.database.ImageToUploadDao;
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
public final class ImageRepository_Factory implements Factory<ImageRepository> {
  private final Provider<ImageToUploadDao> imageToUploadDaoProvider;

  private final Provider<ImageToDeleteDao> imageToDeleteDaoProvider;

  public ImageRepository_Factory(Provider<ImageToUploadDao> imageToUploadDaoProvider,
      Provider<ImageToDeleteDao> imageToDeleteDaoProvider) {
    this.imageToUploadDaoProvider = imageToUploadDaoProvider;
    this.imageToDeleteDaoProvider = imageToDeleteDaoProvider;
  }

  @Override
  public ImageRepository get() {
    return newInstance(imageToUploadDaoProvider.get(), imageToDeleteDaoProvider.get());
  }

  public static ImageRepository_Factory create(Provider<ImageToUploadDao> imageToUploadDaoProvider,
      Provider<ImageToDeleteDao> imageToDeleteDaoProvider) {
    return new ImageRepository_Factory(imageToUploadDaoProvider, imageToDeleteDaoProvider);
  }

  public static ImageRepository newInstance(ImageToUploadDao imageToUploadDao,
      ImageToDeleteDao imageToDeleteDao) {
    return new ImageRepository(imageToUploadDao, imageToDeleteDao);
  }
}
