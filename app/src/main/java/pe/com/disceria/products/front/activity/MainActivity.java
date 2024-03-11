package pe.com.disceria.products.front.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import java.util.List;
import java.util.Optional;

import pe.com.disceria.products.R;
import pe.com.disceria.products.business.ProductoService;
import pe.com.disceria.products.front.item.ProductoItem;
import pe.com.disceria.products.sql.Operacion;
import pe.com.disceria.products.sql.entity.Producto;
import pe.com.disceria.products.util.Constantes;
import pe.com.disceria.products.util.Metodos;

/**
 * Clase que representa la lógica de la pantalla principal.
 *
 * @author un-chalan-mas
 */
public class MainActivity extends AppCompatActivity {

  /**
   * Componente de la lista de productos.
   */
  private RecyclerView recyclerView;

  /**
   * Adaptador de la lista de productos.
   */
  private ProductoItem lista;

  /**
   * Abre la actividad {@link DetallesProductoActivity} y espera un resultado.
   */
  private ActivityResultLauncher<Intent> gestorActividadDetalles;

  /**
   * Instancia para lógica de negocio.
   */
  private ProductoService servicio;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    iniciarVariables();

  }

  @Override
  public void onStart() {
    super.onStart();
    // Este componente se encuentra dentro de un fragmento y éste se crea después de onCreate()
    TextView textoProgreso = this.findViewById(R.id.fragmento_progreso_texto);
    textoProgreso.setText(this.getString(R.string.fragmento_todas_alarmas));
    listarProductos();
  }

  /**
   * Método a ejecutar en caso de pulsar el botón "Agregar"
   *
   * @param v Instancia del botón "Agregar".
   */
  public void agregar(View v) {
    this.gestorActividadDetalles.launch(new Intent(this.getApplicationContext(),
        DetallesProductoActivity.class));
  }

  /**
   * Inicializa las variables declaradas a nivel de clase.
   */
  private void iniciarVariables() {
    this.recyclerView = this.findViewById(R.id.activity_main_list);
    this.servicio = new ProductoService(this.getApplicationContext(),
        this.findViewById(R.id.activity_main_main_view),
        this.findViewById(R.id.activity_main_progress_view));
    this.gestorActividadDetalles = this.registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(), result -> {
          if (result.getResultCode() == RESULT_OK) {
            Optional.ofNullable(result.getData())
                .ifPresent(this::gestionarProductoGuardado);
          }
        });
    this.setSupportActionBar(this.findViewById(R.id.activity_main_toolbar));
  }

  /**
   * Obtiene los productos desde la base de datos local.
   */
  private void listarProductos() {
    this.servicio.bloquearActividad();
    Futures.addCallback(this.servicio.listarTodo(),
        this.organizarListaRegistros(),
        Metodos.obtenerExecutor(this.getApplicationContext()));
  }

  /**
   * Ejecuta instrucciones posteriores al listado de productos desde la base de datos.
   *
   * @return Una instancia {@link FutureCallback}.
   */
  private FutureCallback<List<Producto>> organizarListaRegistros() {
    return servicio.construirFutureCallback(lista -> {
      this.lista = new ProductoItem(this.getApplicationContext(), lista,
          this.gestorActividadDetalles);
      this.recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
      this.recyclerView.setAdapter(this.lista);
    }, this.getApplicationContext());
  }

  /**
   * Ejecuta instrucciones posteriores a la devolución de un resultado desde la pantalla de detalles
   * de producto.
   *
   * @param intent Instancia {@link Intent}.
   */
  private void gestionarProductoGuardado(Intent intent) {
    Producto producto = Metodos.obtenerObjetoDesdeIntent(intent,
        Constantes.INTENT_CLAVE_PRODUCTO, Producto.class);
    Operacion operacion = Metodos.obtenerObjetoDesdeIntent(intent,
        Constantes.INTENT_CLAVE_OPERACION, Operacion.class);
    int indice = intent.getIntExtra(Constantes.INTENT_CLAVE_INDICE_PRODUCTO,
        Constantes.NUMERO_MENOS_UNO);
    switch (operacion) {
      case INSERTAR:
        int totalPrevio = this.lista.getItemCount();
        this.lista.agregar(producto);
        this.lista.notifyItemInserted(totalPrevio);
        break;
      case ACTUALIZAR:
        this.lista.actualizar(indice, producto);
        this.lista.notifyItemChanged(indice);
        break;
      case ELIMINAR:
        this.lista.eliminar(producto);
        this.lista.notifyItemRemoved(indice);
        break;
    }
  }

}