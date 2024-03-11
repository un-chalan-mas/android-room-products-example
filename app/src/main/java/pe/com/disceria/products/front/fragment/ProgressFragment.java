package pe.com.disceria.products.front.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import pe.com.disceria.products.R;

/**
 * Fragmento que contiene los componentes a mostrar cuando se bloquean los componentes de una
 * actividad.
 *
 * @author un-chalan-mas
 */
public class ProgressFragment extends Fragment {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_progress, container, false);
  }
}