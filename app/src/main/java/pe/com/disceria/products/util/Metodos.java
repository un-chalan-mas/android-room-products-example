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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Metodos {

  public static Executor obtenerExecutor(Context context) {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
        ? context.getMainExecutor()
        : ContextCompat.getMainExecutor(context);
  }

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
