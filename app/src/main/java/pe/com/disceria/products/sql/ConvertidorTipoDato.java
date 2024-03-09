package pe.com.disceria.products.sql;

import androidx.room.TypeConverter;

import java.math.BigDecimal;
import java.util.Optional;

import pe.com.disceria.products.util.Constantes;

public class ConvertidorTipoDato {

  @TypeConverter
  public String desdeBigDecimal(BigDecimal input) {
    return Optional.ofNullable(input)
        .map(BigDecimal::toPlainString)
        .orElse(Constantes.TEXTO_VACIO);
  }

  @TypeConverter
  public BigDecimal haciaBigDecimal(String input) {
    return Optional.of(input)
        .filter(value -> !value.isEmpty())
        .map(BigDecimal::new)
        .orElse(BigDecimal.ZERO);
  }

}
