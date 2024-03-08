package pe.com.disceria.products.sql.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Clase que representa la categoría de un producto.
 *
 * @author un-chalan-mas
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum Categoria {

  /**
   * Electrónica
   */
  ELECTRONICA(0),

  /**
   * Hogar
   */
  HOGAR(0),

  /**
   * Vestimenta
   */
  VESTIMENTA(0),

  /**
   * Otros
   */
  OTROS(0);

  private int nombre;

}
