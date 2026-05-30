package com.example.inmobiliaria_trabajopractico.ui.pago;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inmobiliaria_trabajopractico.R;
import com.example.inmobiliaria_trabajopractico.databinding.ItemPagoBinding;
import com.example.inmobiliaria_trabajopractico.modelo.Pago;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.ViewHolderPago> {

    private List<Pago> listaPagos;
    private LayoutInflater inflater;

    public PagoAdapter(List<Pago> pagos, LayoutInflater inflater) {
        this.listaPagos = pagos;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public ViewHolderPago onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPagoBinding binding = ItemPagoBinding.inflate(inflater, parent, false);
        return new ViewHolderPago(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPago holder, int position) {
        Pago pagoActual = listaPagos.get(position);

        holder.binding.tvFechaPago.setText(pagoActual.getFechaPago());

        NumberFormat nf = NumberFormat.getInstance(new Locale("es", "AR"));
        holder.binding.tvImporte.setText("$ " + nf.format(pagoActual.getImporte()));

        holder.binding.tvDetalle.setText(pagoActual.getDetalle());

        if (pagoActual.isEstado()) {
            holder.binding.tvEstado.setText("PAGADO");
            holder.binding.tvEstado.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
        } else {
            holder.binding.tvEstado.setText("PENDIENTE");
            holder.binding.tvEstado.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return listaPagos != null ? listaPagos.size() : 0;
    }

    public static class ViewHolderPago extends RecyclerView.ViewHolder {
        private final ItemPagoBinding binding;

        public ViewHolderPago(@NonNull ItemPagoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
