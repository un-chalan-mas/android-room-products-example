package pe.com.disceria.products.business;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

import pe.com.disceria.products.R;
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
  private final SqlDatabase localDatabase;

  /**
   * Instancia del gestor de los registros de {@link Producto}.
   */
  private final ProductoDao dao;

  /**
   * Vista principal de la actividad.
   */
  private final View vistaPrincipal;

  /**
   * Vista de progreso de la actividad
   */
  private final View vistaProgreso;

  /**
   * Constructor por defecto.
   *
   * @param context        Contexto de la aplicación.
   * @param vistaPrincipal Contenedor de la vista principal.
   * @param vistaProgreso  Contenedor de la vista de progreso
   */
  public ProductoService(Context context, View vistaPrincipal, View vistaProgreso) {
    this.localDatabase = SqlDatabase.getInstance(context, Constantes.NOMBRE_BASE_DATOS);
    this.dao = this.localDatabase.getProductoDao();
    this.vistaPrincipal = vistaPrincipal;
    this.vistaProgreso = vistaProgreso;
  }

  /**
   * Guarda un producto nuevo o existente.
   *
   * @param producto Datos de producto a insertar o actualizar.
   * @return El registro guardado en base de datos.
   */
  public ListenableFuture<Producto> guardar(Producto producto) {
    this.localDatabase.runInTransaction(() -> {
      if (producto.getId() != null) {
        this.dao.actualizar(producto);
      } else {
        Long id = this.dao.insertar(producto);
        producto.setId(id);
      }
    });
    return Futures.immediateFuture(producto);
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

  /**
   * Oculta los elementos de una actividad.
   */
  public void bloquearActividad() {
    this.vistaPrincipal.setVisibility(View.GONE);
    this.vistaProgreso.setVisibility(View.VISIBLE);
  }

  /**
   * Muestra los elementos de una actividad.
   */
  public void desbloquearActividad() {
    this.vistaProgreso.setVisibility(View.GONE);
    this.vistaPrincipal.setVisibility(View.VISIBLE);
  }

  /**
   * Construye una instancia {@link FutureCallback} para manejar las respuestas de las operaciones
   * hacia la base de datos local.
   *
   * @param exitoso Método a consumir en caso la operación haya sido exitosa.
   * @param context Contexto de la aplicación.
   * @param <T>     Tipo de respuesta de la operación.
   * @return Una instancia {@link FutureCallback}.
   */
  public <T> FutureCallback<T> construirFutureCallback(java.util.function.Consumer<T> exitoso,
                                                       Context context) {
    return new FutureCallback<T>() {

      /**
       * Método a implementar en caso de éxito.
       * @param result Dato enviado como respuesta.
       */
      @Override
      public void onSuccess(T result) {
        exitoso.accept(result);
        ProductoService.this.desbloquearActividad();
      }

      /**
       * Método a implementar en caso de error.
       * @param t Excepción lanzada.
       */
      @Override
      public void onFailure(@NonNull Throwable t) {
        ProductoService.this.desbloquearActividad();
        android.util.Log.e(this.getClass().getName(), t.getMessage(), t);
        Toast.makeText(context, context.getString(R.string.error_base_datos), Toast.LENGTH_SHORT)
            .show();
      }
    };
  }

}
