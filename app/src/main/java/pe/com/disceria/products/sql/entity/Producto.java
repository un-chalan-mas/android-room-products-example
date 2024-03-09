package pe.com.disceria.products.sql.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Clase que representa el registro de la tabla Producto.
 *
 * @author un-chalan-mas
 */
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
public class Producto implements Serializable {

  /**
   * Identificador único de registro.
   */
  @EqualsAndHashCode.Include
  @PrimaryKey(autoGenerate = true)
  private Long id;

  /**
   * Nombre del producto
   */
  private String nombre;

  /**
   * Descripción detallada del producto.
   */
  private String descripcion;

  /**
   * Precio del producto
   */
  private BigDecimal precio;

  /**
   * Categoría del producto
   */
  private Integer categoria;

}
