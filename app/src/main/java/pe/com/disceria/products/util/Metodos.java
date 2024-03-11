package pe.com.disceria.products.util;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.content.ContextCompat;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.Executor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Clase que representa los métodos generales de la aplicación.
 *
 * @author un-chalan-mas
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Metodos {

  /**
   * Obtiene la instancia {@link Executor} según la versión Android del dispositivo.
   *
   * @param context Contexto de la aplicación.
   * @return Una instancia {@link Executor}.
   */
  public static Executor obtenerExecutor(Context context) {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
        ? context.getMainExecutor()
        : ContextCompat.getMainExecutor(context);
  }

  /**
   * Obtiene un objeto {@link Serializable} almacenado en un {@link Intent} a partir de la clave
   * enviada.
   *
   * @param intent Instancia {@link Intent}.
   * @param clave  Clave del objeto a obtener.
   * @param clase  Clase del objeto a obtener.
   * @param <T>    Tipo de objeto a obtener.
   * @return Objeto almacenado a partir de la clave enviada, o {@code null} si no se encuentra.
   */
  public static <T extends Serializable> T obtenerObjetoDesdeIntent(Intent intent, String clave,
                                                                    Class<T> clase) {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        ? intent.getSerializableExtra(clave, clase)
        : Optional.of(intent)
        .map(object -> object.getSerializableExtra(clave))
        .map(clase::cast)
        .orElse(null);
  }

}
