package pe.com.disceria.products.sql.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pe.com.disceria.products.sql.entity.Producto;

@Dao
public interface ProductoDao {

  @Insert
  Long insertar(Producto producto);

  @Update
  void actualizar(Producto producto);

  @Delete
  void eliminar(Producto producto);

  @Query("SELECT * FROM PRODUCTO;")
  List<Producto> listarTodo();

  @Query("SELECT * FROM PRODUCTO WHERE ID=:id;")
  Producto obtenerPorId(Long id);

}
