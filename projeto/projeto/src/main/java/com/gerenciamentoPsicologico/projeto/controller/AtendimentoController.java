/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gerenciamentoPsicologico.projeto.controller;

import com.gerenciamentoPsicologico.projeto.model.Atendimento;
import com.gerenciamentoPsicologico.projeto.model.AtendimentoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/atendimentos")
public class AtendimentoController {
    
    @Autowired
    private AtendimentoRepository atendimentoRepository;
    
    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("atendimento", new Atendimento()); // Adiciona um novo objeto Atendimento ao modelo
        return "cadastroAtendimento"; // Nome da página HTML para cadastrar atendimento
    }

@PostMapping("/salvar")
public ResponseEntity<String> salvarAtendimento(@RequestBody Atendimento atendimento) {
    try {
        // Certifique-se de que todos os campos necessários estão preenchidos
        if (atendimento.getHorarioInicial() == null) {
            atendimento.setHorarioInicial(LocalDateTime.now()); // Define um valor padrão se necessário
        }
        atendimentoRepository.save(atendimento);
        return ResponseEntity.ok("Atendimento cadastrado com sucesso!");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar o atendimento: " + e.getMessage());
    }
}

   // Este método serve a página HTML com o JavaScript para popular os dados
    @GetMapping("/listar")
    public String exibirPaginaDeListagem() {
        return "listarAtendimento";  // Nome do arquivo HTML
    }

    // Este método retorna os dados em formato JSON
    @GetMapping("/dados")
    @ResponseBody
    public List<Atendimento> listarAtendimentos() {
        return atendimentoRepository.findAll(); // Retorna os atendimentos como JSON
    }


// Este método serve a página HTML para mostrar os detalhes do atendimento
@GetMapping("/detalhes/{id}")
public String exibirPaginaDeDetalhes(@PathVariable Long id, Model model) {
    // Adiciona apenas o ID do atendimento ao modelo, se necessário
    model.addAttribute("id", id);
    return "detalhesAtendimento";  // Nome do arquivo HTML
}
    // Este método retorna os dados do atendimento em formato JSON
    @GetMapping("/detalhes/json/{id}")
    @ResponseBody
    public ResponseEntity<Atendimento> getAtendimentoPorId(@PathVariable Long id) {
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Atendimento inválido: " + id));
        return ResponseEntity.ok(atendimento);  // Retorna o atendimento como JSON
    }



    @PutMapping("/atualizar/{id}")
    @ResponseBody
    public ResponseEntity<String> atualizarAtendimento(@PathVariable Long id, @RequestBody Atendimento updatedAtendimento) {
        Optional<Atendimento> optionalAtendimento = atendimentoRepository.findById(id);
        if (optionalAtendimento.isPresent()) {
            Atendimento atendimento = optionalAtendimento.get();
            atendimento.setNome(updatedAtendimento.getNome());
            atendimento.setHorarioInicial(updatedAtendimento.getHorarioInicial());
            atendimento.setHorarioFinal(updatedAtendimento.getHorarioFinal());
            atendimento.setValor(updatedAtendimento.getValor());
            atendimento.setDesconto(updatedAtendimento.getDesconto());
            atendimento.setValorTotal(updatedAtendimento.getValorTotal());
            atendimento.setMetodoPagamento(updatedAtendimento.getMetodoPagamento());
            atendimento.setConvenio(updatedAtendimento.getConvenio());
            atendimento.setDescricao(updatedAtendimento.getDescricao());
            atendimentoRepository.save(atendimento);
            return ResponseEntity.ok("Atendimento atualizado com sucesso!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/excluir/{id}")
    @ResponseBody
    public ResponseEntity<String> excluirAtendimento(@PathVariable Long id) {
        try {
            atendimentoRepository.deleteById(id);
            return ResponseEntity.ok("Atendimento excluído com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir o atendimento: " + e.getMessage());
        }
    }
}
