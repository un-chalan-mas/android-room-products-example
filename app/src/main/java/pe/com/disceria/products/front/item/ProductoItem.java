package pe.com.disceria.products.front.item;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.RoundingMode;
import java.util.List;

import lombok.RequiredArgsConstructor;
import pe.com.disceria.products.R;
import pe.com.disceria.products.front.activity.DetallesProductoActivity;
import pe.com.disceria.products.sql.entity.Producto;
import pe.com.disceria.products.util.Constantes;

/**
 * Clase que representa la lista de productos a mostrar en la actividad principal.
 *
 * @author un-chalan-mas
 */
@RequiredArgsConstructor
public class ProductoItem extends RecyclerView.Adapter<ProductoItem.VisorProducto> {

  /**
   * Actividad de la aplicación.
   */
  private final Context contexto;

  /**
   * Lista de datos.
   */
  private final List<Producto> lista;

  /**
   * Abre la actividad {@link DetallesProductoActivity} y espera un resultado.
   */
  private final ActivityResultLauncher<Intent> gestorActividadDetalles;

  @NonNull
  @Override
  public VisorProducto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_producto, parent, Boolean.FALSE);
    return new VisorProducto(view);
  }

  /**
   * Obtiene el total de elementos de la lista.
   *
   * @return El total de elementos de la lista.
   */
  @Override
  public int getItemCount() {
    return this.lista.size();
  }

  @Override
  public void onBindViewHolder(@NonNull VisorProducto holder, int position) {
    Producto producto = this.lista.get(position);
    holder.nombre.setText(producto.getNombre());
    holder.categoria.setText(this.contexto.getString(producto.getCategoria()));
    holder.precio.setText(producto.getPrecio().setScale(2, RoundingMode.HALF_EVEN).toString());
    holder.itemView.setOnClickListener(v -> {
      Intent intent = new Intent(this.contexto, DetallesProductoActivity.class);
      intent.putExtra(Constantes.INTENT_CLAVE_PRODUCTO, producto);
      intent.putExtra(Constantes.INTENT_CLAVE_INDICE_PRODUCTO, position);
      this.gestorActividadDetalles.launch(intent);
    });
  }

  /**
   * Agrega un elemento a la lista de productos.
   *
   * @param producto Elemento a agregar.
   * @return {@code true} si el elemento fue agregado exitosamente.
   */
  public boolean agregar(Producto producto) {
    return this.lista.add(producto);
  }

  /**
   * Actualiza los datos de un elemento en una posición indicada.
   *
   * @param indice   Posición del elemento a actualizar.
   * @param producto Datos del elemento a actualizar.
   */
  public void actualizar(int indice, Producto producto) {
    this.lista.set(indice, producto);
  }

  /**
   * Elimina un elemento de la lista.
   *
   * @param producto Elemento a eliminar.
   */
  public void eliminar(Producto producto) {
    this.lista.remove(producto);
  }

  /**
   * Clase que representa la interfaz del registro de {@link Producto}
   *
   * @author un-chalan-mas
   */
  public static class VisorProducto extends RecyclerView.ViewHolder {

    /**
     * Texto que contiene el nombre.
     */
    private TextView nombre;

    /**
     * Texto que contiene la categoría.
     */
    private TextView categoria;

    /**
     * Texto que contiene el precio.
     */
    private TextView precio;

    /**
     * Constructor por defecto.
     *
     * @param itemView Vista a nivel de elemento.
     */
    public VisorProducto(@NonNull View itemView) {
      super(itemView);
      this.nombre = itemView.findViewById(R.id.item_producto_nombre);
      this.categoria = itemView.findViewById(R.id.item_producto_categoria);
      this.precio = itemView.findViewById(R.id.item_producto_precio);
    }
  }

}
