package swagger.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import swagger.model.Cliente;
import swagger.repository.ClienteRepository;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }
    
    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }
    
    public Optional<Cliente> findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
    
    public List<Cliente> findByNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    @Transactional
    public Cliente save(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Já existe um cliente com este email: " + cliente.getEmail());
        }
        return clienteRepository.save(cliente);
    }
    
    @Transactional
    public Cliente update(Long id, Cliente clienteDetails) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + id));
        
        // Verifica se o email já existe em outro cliente
        if (!cliente.getEmail().equals(clienteDetails.getEmail()) && 
            clienteRepository.existsByEmail(clienteDetails.getEmail())) {
            throw new RuntimeException("Já existe um cliente com este email: " + clienteDetails.getEmail());
        }
        
        cliente.setNome(clienteDetails.getNome());
        cliente.setEmail(clienteDetails.getEmail());
        cliente.setTelefone(clienteDetails.getTelefone());
        cliente.setDataNascimento(clienteDetails.getDataNascimento());
        
        return clienteRepository.save(cliente);
    }
    
    @Transactional
    public void deleteById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + id));
        
        // Verifica se o cliente tem pedidos antes de deletar
        if (!cliente.getPedidos().isEmpty()) {
            throw new RuntimeException("Não é possível excluir cliente com pedidos associados");
        }
        
        clienteRepository.deleteById(id);
    }
    
    public boolean existsByEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }
}