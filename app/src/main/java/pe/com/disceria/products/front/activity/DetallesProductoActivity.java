package pe.com.disceria.products.front.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import java.math.BigDecimal;
import java.util.Optional;

import pe.com.disceria.products.R;
import pe.com.disceria.products.business.ProductoService;
import pe.com.disceria.products.sql.Operacion;
import pe.com.disceria.products.sql.entity.Producto;
import pe.com.disceria.products.util.Constantes;
import pe.com.disceria.products.util.Metodos;

/**
 * Clase que representa la lógica de la pantalla de detalles de producto
 *
 * @author un-chalan-mas
 */
public class DetallesProductoActivity extends AppCompatActivity {

  /**
   * Lista de identificadores de categorías.
   */
  private static final Integer[] IDENTIFICADORES_CATEGORIAS = {
      R.string.categoria_electronica,
      R.string.categoria_hogar,
      R.string.categoria_vestimenta,
      R.string.categoria_otros
  };

  /**
   * Componente para nombre de producto.
   */
  private EditText nombre;

  /**
   * Componente para descripción de producto.
   */
  private EditText descripcion;

  /**
   * Componente para precio de producto.
   */
  private EditText precio;

  /**
   * Componente para categoría de producto.
   */
  private Spinner categoria;

  /**
   * Componente para texto en caso de mostrar la vista de progreso.
   */
  private TextView textoProgreso;

  /**
   * Producto enviado desde la actividad principal.
   */
  private Optional<Producto> producto;

  /**
   * Adaptador para listar categorías.
   */
  private ArrayAdapter<CharSequence> listaCategorias;

  /**
   * Instancia para lógica de negocio.
   */
  private ProductoService servicio;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detalles_producto);
    iniciarVariables();
    configurarBarra();
    listarCategorias();
    llenarDatos();
  }

  @Override
  public void onStart() {
    super.onStart();
    // Este componente se encuentra dentro de un fragmento y éste se crea después de onCreate()
    this.textoProgreso = this.findViewById(R.id.fragmento_progreso_texto);
  }

  /**
   * Al activar el botón hacia atrás en la interfaz, se debe implementar su método para cerrar
   * la actividad en caso se pulse en éste.
   *
   * @param item The menu item that was selected.
   * @return El resultado del método implementado en la clase {@link AppCompatActivity}
   * @see AppCompatActivity#onOptionsItemSelected(MenuItem)
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        this.finish();
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * Método a ejecutar en caso de pulsar el botón "Guardar".
   *
   * @param v Instancia del botón "Guardar".
   */
  public void guardar(View v) {
    this.textoProgreso.setText(this.getString(R.string.fragmento_guardar));
    this.servicio.bloquearActividad();
    Producto nuevosDatos = this.producto.orElseGet(() -> Producto.builder().build());
    nuevosDatos.setNombre(this.nombre.getText().toString());
    nuevosDatos.setDescripcion(this.descripcion.getText().toString());
    nuevosDatos.setPrecio(new BigDecimal(this.precio.getText().toString()));
    int index = this.categoria.getSelectedItemPosition();
    nuevosDatos.setCategoria(IDENTIFICADORES_CATEGORIAS[index]);
    Futures.addCallback(this.servicio.guardar(nuevosDatos),
        this.gestionarProductoGuardado(),
        Metodos.obtenerExecutor(this.getApplicationContext()));
  }

  /**
   * Método a ejecutar en caso de pulsar el botón "Cancelar".
   *
   * @param v Instancia del botón "Cancelar".
   */
  public void cancelar(View v) {
    this.finish();
  }

  /**
   * Método a ejecutar en caso de pulsar el botón "Eliminar".
   *
   * @param v Instancia del botón "Eliminar".
   */
  public void eliminar(View v) {
    this.textoProgreso.setText(this.getString(R.string.fragmento_eliminar));
    this.servicio.bloquearActividad();
    Futures.addCallback(this.servicio.eliminar(this.producto.get()),
        this.gestionarProductoEliminado(),
        Metodos.obtenerExecutor(this.getApplicationContext()));
  }

  /**
   * Inicializa las variables declaradas a nivel de clase.
   */
  private void iniciarVariables() {
    this.nombre = this.findViewById(R.id.actividad_detalles_nombre);
    this.descripcion = this.findViewById(R.id.actividad_detalles_descripcion);
    this.precio = this.findViewById(R.id.actividad_detalles_precio);
    this.categoria = this.findViewById(R.id.actividad_detalles_categoria);
    this.producto = Optional.ofNullable(Metodos.obtenerObjetoDesdeIntent(this.getIntent(),
        Constantes.INTENT_CLAVE_PRODUCTO, Producto.class));
    this.servicio = new ProductoService(this.getApplicationContext(),
        this.findViewById(R.id.actividad_detalles_vista_principal),
        this.findViewById(R.id.actividad_detalles_vista_progreso));
    this.findViewById(R.id.actividad_detalles_eliminar).setVisibility(
        this.producto.isPresent() ? View.VISIBLE : View.GONE);
  }

  /**
   * Configura la barra de herramientas de la actividad.
   */
  private void configurarBarra() {
    this.setSupportActionBar(this.findViewById(R.id.actividad_detalles_toolbar));
    Optional.ofNullable(this.getSupportActionBar())
        .ifPresent(bar -> bar.setDisplayHomeAsUpEnabled(true));
  }

  /**
   * Configura la lista de categorías en el componente correspondiente.
   */
  private void listarCategorias() {
    this.listaCategorias = ArrayAdapter.createFromResource(
        this.getApplicationContext(), R.array.categorias, android.R.layout.simple_spinner_item);
    this.listaCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    this.categoria.setAdapter(this.listaCategorias);
  }

  /**
   * Coloca los datos del producto enviado en sus componentes correspondientes.
   */
  private void llenarDatos() {
    this.producto.ifPresent(registro -> {
      this.nombre.setText(registro.getNombre());
      this.descripcion.setText(registro.getDescripcion());
      this.precio.setText(registro.getPrecio().toString());
      int position = this.listaCategorias.getPosition(this.getString(registro.getCategoria()));
      this.categoria.setSelection(position);
    });
  }

  /**
   * Ejecuta instrucciones posteriores al guardado del producto en la base de datos.
   *
   * @return Una instancia {@link FutureCallback}.
   */
  private FutureCallback<Producto> gestionarProductoGuardado() {
    return this.servicio.construirFutureCallback(producto -> {
      int indice = this.getIntent().getIntExtra(Constantes.INTENT_CLAVE_INDICE_PRODUCTO,
          Constantes.NUMERO_MENOS_UNO);
      Operacion operacion = indice == Constantes.NUMERO_MENOS_UNO ? Operacion.INSERTAR : Operacion.ACTUALIZAR;
      Intent intent = new Intent();
      intent.putExtra(Constantes.INTENT_CLAVE_OPERACION, operacion);
      intent.putExtra(Constantes.INTENT_CLAVE_PRODUCTO, producto);
      intent.putExtra(Constantes.INTENT_CLAVE_INDICE_PRODUCTO, indice);
      this.setResult(Activity.RESULT_OK, intent);
      Toast.makeText(this.getApplicationContext(), this.getString(
          R.string.actividad_detalles_guardar_exito), Toast.LENGTH_LONG).show();
      this.finish();
    }, this.getApplicationContext());
  }

  /**
   * Ejecuta instrucciones posteriores a la eliminación de un producto de la base de datos.
   *
   * @return Una instancia {@link FutureCallback}.
   */
  private FutureCallback<Void> gestionarProductoEliminado() {
    return this.servicio.construirFutureCallback(non -> {
      Intent intent = new Intent();
      int indice = this.getIntent().getIntExtra(Constantes.INTENT_CLAVE_INDICE_PRODUCTO,
          Constantes.NUMERO_MENOS_UNO);
      intent.putExtra(Constantes.INTENT_CLAVE_OPERACION, Operacion.ELIMINAR);
      intent.putExtra(Constantes.INTENT_CLAVE_PRODUCTO, this.producto.get());
      intent.putExtra(Constantes.INTENT_CLAVE_INDICE_PRODUCTO, indice);
      this.setResult(Activity.RESULT_OK, intent);
      Toast.makeText(this.getApplicationContext(), this.getString(
          R.string.actividad_detalles_eliminar_exito), Toast.LENGTH_LONG).show();
      this.finish();
    }, this.getApplicationContext());
  }

}