package pe.com.disceria.products.sql;

import androidx.room.TypeConverter;

import java.math.BigDecimal;
import java.util.Optional;

import pe.com.disceria.products.util.Constantes;

/**
 * Clase que contiene los métodos para facilitar el guardado de campos {@link BigDecimal}.
 *
 * @author un-chalan-mas
 */
public class ConvertidorTipoDato {

  /**
   * Almacena un dato de tipo {@link BigDecimal}.
   *
   * @param input Valor a almacenar.
   * @return Una cadena de texto con el valor numérico.
   */
  @TypeConverter
  public String desdeBigDecimal(BigDecimal input) {
    return Optional.ofNullable(input)
        .map(BigDecimal::toPlainString)
        .orElse(Constantes.TEXTO_VACIO);
  }

  /**
   * Obtiene el dato y lo guarda en un campo {@link BigDecimal}.
   *
   * @param input Valor a obtener.
   * @return El valor en {@link BigDecimal}.
   */
  @TypeConverter
  public BigDecimal haciaBigDecimal(String input) {
    return Optional.of(input)
        .filter(value -> !value.isEmpty())
        .map(BigDecimal::new)
        .orElse(BigDecimal.ZERO);
  }

}
