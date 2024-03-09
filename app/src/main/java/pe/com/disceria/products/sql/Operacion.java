package pe.com.disceria.products.sql;

import java.io.Serializable;

/**
 * Operaciones en los registros de producto.
 *
 * @author un-chalan-mas
 */
public enum Operacion implements Serializable {

  /**
   * Cuando se inserta un producto.
   */
  INSERTAR,

  /**
   * Cuando se actualiza un producto.
   */
  ACTUALIZAR,

  /**
   * Cuando se elimina un producto.
   */
  ELIMINAR;

}
