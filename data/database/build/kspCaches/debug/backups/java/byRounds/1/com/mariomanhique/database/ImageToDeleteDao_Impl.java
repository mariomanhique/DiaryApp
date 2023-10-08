package com.mariomanhique.database;

import android.database.Cursor;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.mariomanhique.database.entity.ImageToDelete;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ImageToDeleteDao_Impl implements ImageToDeleteDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ImageToDelete> __insertionAdapterOfImageToDelete;

  private final SharedSQLiteStatement __preparedStmtOfCleanupImage;

  public ImageToDeleteDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfImageToDelete = new EntityInsertionAdapter<ImageToDelete>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `image_to_delete_table` (`id`,`remoteImagePath`) VALUES (nullif(?, 0),?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ImageToDelete value) {
        stmt.bindLong(1, value.getId());
        if (value.getRemoteImagePath() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getRemoteImagePath());
        }
      }
    };
    this.__preparedStmtOfCleanupImage = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM image_to_delete_table WHERE id=?";
        return _query;
      }
    };
  }

  @Override
  public void addImageToDelete(final ImageToDelete imageToDelete) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfImageToDelete.insert(imageToDelete);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void cleanupImage(final int imageId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfCleanupImage.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, imageId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfCleanupImage.release(_stmt);
    }
  }

  @Override
  public Flow<List<ImageToDelete>> getAllImages() {
    final String _sql = "SELECT * FROM image_to_delete_table ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"image_to_delete_table"}, new Callable<List<ImageToDelete>>() {
      @Override
      public List<ImageToDelete> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRemoteImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteImagePath");
          final List<ImageToDelete> _result = new ArrayList<ImageToDelete>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final ImageToDelete _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpRemoteImagePath;
            if (_cursor.isNull(_cursorIndexOfRemoteImagePath)) {
              _tmpRemoteImagePath = null;
            } else {
              _tmpRemoteImagePath = _cursor.getString(_cursorIndexOfRemoteImagePath);
            }
            _item = new ImageToDelete(_tmpId,_tmpRemoteImagePath);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
