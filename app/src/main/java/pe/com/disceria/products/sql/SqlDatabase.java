package pe.com.disceria.products.sql;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.Optional;

import pe.com.disceria.products.sql.dao.ProductoDao;
import pe.com.disceria.products.sql.entity.Producto;
import pe.com.disceria.products.util.Constantes;

/**
 * Clase que representa la declaración de la base de datos y sus respectivos objetos de acceso a
 * datos (DAO). Las anotaciones en esta clase y sus respectivos DAO generan el código que se
 * necesita para gestionar la base de datos local.
 *
 * @author un-chalan-mas
 * @see RoomDatabase
 */
@Database(entities = {
    Producto.class
}, version = Constantes.VERSION_BASE_DATOS)
@TypeConverters({
    ConvertidorTipoDato.class
})
public abstract class SqlDatabase extends RoomDatabase {

  /**
   * Instancia de base de datos local.
   */
  private static SqlDatabase appDatabase;

  /**
   * Obtiene la instancia de base de datos local, o crea una de no existir.
   *
   * @param context      Contexto de la aplicación.
   * @param databaseName Nombre de la base de datos.
   * @return Una instancia {@link SqlDatabase}.
   */
  public static synchronized SqlDatabase getInstance(Context context, String databaseName) {
    return Optional.ofNullable(appDatabase).orElseGet(() -> {
      appDatabase = create(context, databaseName);
      return appDatabase;
    });
  }

  /**
   * Crea una instancia de base de datos local.
   *
   * @param context      Contexto de la aplicación.
   * @param databaseName Nombre de la base de datos.
   * @return Una instancia {@link SqlDatabase}.
   */
  private static SqlDatabase create(final Context context, String databaseName) {
    return Room.databaseBuilder(context, SqlDatabase.class, databaseName)
        .allowMainThreadQueries()
        .build();
  }

  /**
   * Obtiene el objeto de acceso a datos (DAO) para {@link Producto}.
   *
   * @return El objeto de acceso a datos (DAO) para {@link Producto}.
   */
  public abstract ProductoDao getProductoDao();

}
