package com.example.inmobiliaria_trabajopractico.ui.inmuble;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.inmobiliaria_trabajopractico.R;
import com.example.inmobiliaria_trabajopractico.databinding.FragmentDetalleInmuebleBinding;
import com.example.inmobiliaria_trabajopractico.modelo.Inmueble;
import com.example.inmobiliaria_trabajopractico.request.ApiClient;

import java.text.NumberFormat;
import java.util.Locale;

public class DetalleInmuebleFragment extends Fragment {

    private DetalleInmuebleViewModel mViewModel;
    private FragmentDetalleInmuebleBinding binding;

    public static DetalleInmuebleFragment newInstance() {
        return new DetalleInmuebleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(DetalleInmuebleViewModel.class);

        mViewModel.getInmueble().observe(getViewLifecycleOwner(), inmueble -> {
            binding.tvDetalleDireccion.setText(inmueble.getDireccion());
            binding.tvDetalleTipo.setText(inmueble.getTipo());
            binding.tvDetalleUso.setText(inmueble.getUso());
            binding.tvDetalleAmbientes.setText(String.valueOf(inmueble.getAmbientes()));
            binding.tvDetalleSuperficie.setText(inmueble.getSuperficie() + " m²");
            binding.cbDetalleEstado.setChecked(inmueble.isEstado());

            NumberFormat nf = NumberFormat.getInstance(new Locale("es", "AR"));
            String valorFormateado = nf.format(inmueble.getPrecio());
            binding.tvDetallePrecio.setText("$ " + valorFormateado);

            Glide.with(getContext())
                    .load(ApiClient.BASE_URL + inmueble.getImagen())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.house)
                    .into(binding.ivDetalleFoto);
        });
        mViewModel.getTextoDisponibilidad().observe(getViewLifecycleOwner(), texto -> {
            binding.cbDetalleEstado.setText(texto);
        });

        // Recupero el  Adapter
        Bundle bundle = getArguments();
        if (bundle != null) {
            Inmueble inmuebleSeleccionado = (Inmueble) bundle.getSerializable("inmueble");
            mViewModel.cargarInmueble(inmuebleSeleccionado);
        }

        // Evento del CheckBox
        binding.cbDetalleEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.cambiarDisponibilidad(binding.cbDetalleEstado.isChecked());
            }
        });

        binding.btnVerContrato.setOnClickListener(v -> {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                Bundle args = new Bundle();
                args.putInt("inmuebleId", inmueble.getId());
                Navigation.findNavController(v)
                        .navigate(R.id.action_detalleInmuebleFragment_to_contratoFragment, args);
            }
        });

        return binding.getRoot();
    }
}