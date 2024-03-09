package pe.com.disceria.products.sql.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pe.com.disceria.products.sql.entity.Producto;

/**
 * Clase que representa un objeto de acceso a datos (DAO) para los registros de {@link Producto}
 *
 * @author un-chalan-mas
 */
@Dao
public interface ProductoDao {

  /**
   * Inserta un nuevo producto
   *
   * @param producto Datos de producto a insertar.
   * @return El identificador generado después de la inserción.
   */
  @Insert
  Long insertar(Producto producto);

  /**
   * Actualiza los datos de un producto.
   *
   * @param producto Datos de producto a actualizar.
   */
  @Update
  void actualizar(Producto producto);

  /**
   * Elimina un registro de la base de datos.
   *
   * @param producto Datos de producto a eliminar.
   */
  @Delete
  void eliminar(Producto producto);

  /**
   * Lista todos los productos.
   *
   * @return Una lista de {@link Producto}.
   */
  @Query("SELECT * FROM PRODUCTO;")
  List<Producto> listarTodo();

  /**
   * Busca un registro de producto especificado a partir de su identificador único.
   *
   * @param id Identificador único de registro de {@link Producto}.
   * @return Datos de {@link Producto} encontrados.
   */
  @Query("SELECT * FROM PRODUCTO WHERE ID=:id;")
  Producto obtenerPorId(Long id);

}
