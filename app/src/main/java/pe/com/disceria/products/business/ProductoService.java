package pe.com.disceria.products.business;

import android.content.Context;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

import pe.com.disceria.products.sql.SqlDatabase;
import pe.com.disceria.products.sql.dao.ProductoDao;
import pe.com.disceria.products.sql.entity.Producto;
import pe.com.disceria.products.util.Constantes;

/**
 * Clase que representa la lógica de negocio de la aplicación.
 *
 * @author un-chalan-mas
 */
public class ProductoService {

  /**
   * Instancia del gestor de base de datos.
   */
  private SqlDatabase localDatabase;

  /**
   * Instancia del gestor de los registros de {@link Producto}.
   */
  private ProductoDao dao;

  /**
   * Constructor por defecto.
   *
   * @param context Contexto de la aplicación.
   */
  public ProductoService(Context context) {
    this.localDatabase = SqlDatabase.getInstance(context, Constantes.NOMBRE_BASE_DATOS);
    this.dao = this.localDatabase.getProductoDao();
  }

  /**
   * Inserta un nuevo producto.
   *
   * @param producto Datos de producto a insertar.
   * @return El registro con su nuevo identificador.
   */
  public ListenableFuture<Producto> insertarNuevo(Producto producto) {
    this.localDatabase.runInTransaction(() -> {
      Long id = this.dao.insertar(producto);
      producto.setId(id);
    });
    return Futures.immediateFuture(producto);
  }

  /**
   * Actualiza los datos de un producto.
   *
   * @param producto Datos de producto a actualizar.
   * @return Una instancia {@link Void}
   */
  public ListenableFuture<Void> actualizar(Producto producto) {
    this.localDatabase.runInTransaction(() -> this.dao.actualizar(producto));
    return Futures.immediateVoidFuture();
  }

  /**
   * Elimina un registro de la base de datos.
   *
   * @param producto Producto a eliminar
   * @return Una instancia {@link Void}
   */
  public ListenableFuture<Void> eliminar(Producto producto) {
    this.localDatabase.runInTransaction(() -> this.dao.eliminar(producto));
    return Futures.immediateVoidFuture();
  }

  /**
   * Lista todos los productos.
   *
   * @return Una lista de {@link Producto}.
   */
  public ListenableFuture<List<Producto>> listarTodo() {
    return Futures.immediateFuture(this.dao.listarTodo());
  }

  /**
   * Busca un registro de producto especificado a partir de su identificador único.
   *
   * @param id Identificador único de registro de {@link Producto}.
   * @return Datos de {@link Producto} encontrados.
   */
  public ListenableFuture<Producto> obtenerPorId(Long id) {
    return Futures.immediateFuture(this.dao.obtenerPorId(id));
  }

}
