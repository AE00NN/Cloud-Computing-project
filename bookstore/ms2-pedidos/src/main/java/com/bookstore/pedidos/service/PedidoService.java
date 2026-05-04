package com.bookstore.pedidos.service;

import com.bookstore.pedidos.domain.DetallePedido;
import com.bookstore.pedidos.domain.Pedido;
import com.bookstore.pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public Pedido crear(Pedido pedido) {
        pedido.setEstado("pendiente");
        pedido.setFecha(LocalDate.now().toString());
        if (pedido.getItems() != null) {
            for (DetallePedido item : pedido.getItems()) {
                item.setPedido(pedido);
            }
            double total = pedido.getItems().stream()
                .mapToDouble(i -> i.getPrecioUnitario() * i.getCantidad())
                .sum();
            pedido.setTotal(total);
        }
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    public Pedido obtener(Long id) {
        return pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + id));
    }

    public List<Pedido> porCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public Pedido actualizarEstado(Long id, String estado) {
        Pedido pedido = obtener(id);
        pedido.setEstado(estado);
        return pedidoRepository.save(pedido);
    }
}
