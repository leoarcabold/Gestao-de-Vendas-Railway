package swagger.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import swagger.model.Pedido;
import swagger.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {
    
    @Autowired
    private PedidoService pedidoService;
    
    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoService.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoService.findById(id);
        return pedido.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cliente/{clienteId}")
    public List<Pedido> getPedidosByCliente(@PathVariable Long clienteId) {
        return pedidoService.findByClienteId(clienteId);
    }
    
    @GetMapping("/status/{status}")
    public List<Pedido> getPedidosByStatus(@PathVariable String status) {
        return pedidoService.findByStatus(status);
    }
    
    @GetMapping("/periodo")
    public List<Pedido> getPedidosByPeriodo(@RequestParam LocalDate inicio, @RequestParam LocalDate fim) {
        return pedidoService.findByPeriodo(inicio, fim);
    }
    
    @GetMapping("/{id}/total")
    public ResponseEntity<BigDecimal> getTotalPedido(@PathVariable Long id) {
        try {
            BigDecimal total = pedidoService.calcularTotalPedido(id);
            return ResponseEntity.ok(total);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Pedido> createPedido(@Valid @RequestBody Pedido pedido) {
        try {
            Pedido savedPedido = pedidoService.save(pedido);
            return ResponseEntity.ok(savedPedido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<Pedido> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Pedido pedido = pedidoService.updateStatus(id, status);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Long id) {
        try {
            pedidoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
