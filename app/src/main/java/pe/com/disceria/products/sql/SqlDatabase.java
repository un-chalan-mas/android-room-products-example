package pe.com.disceria.products.sql;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.Optional;

import pe.com.disceria.products.sql.dao.ProductoDao;
import pe.com.disceria.products.sql.entity.Producto;
import pe.com.disceria.products.util.Constantes;

@Database(entities = {
    Producto.class
}, version = Constantes.VERSION_BASE_DATOS)
public abstract class SqlDatabase extends RoomDatabase {

  private static SqlDatabase appDatabase;

  public static synchronized SqlDatabase getInstance(Context context, String databaseName) {
    return Optional.ofNullable(appDatabase).orElseGet(() -> {
      appDatabase = create(context, databaseName);
      return appDatabase;
    });
  }

  private static SqlDatabase create(final Context context, String databaseName) {
    return Room.databaseBuilder(context, SqlDatabase.class, databaseName)
        .allowMainThreadQueries()
        .build();
  }

  public abstract ProductoDao getProductoDao();

}
