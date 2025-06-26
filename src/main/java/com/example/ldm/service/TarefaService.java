package com.example.ldm.service;

import com.example.ldm.model.Tarefa;
import com.example.ldm.repository.TarefaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    public List<Tarefa> listarTarefas() {
        return tarefaRepository.findAll();
    }

    public Tarefa criarTarefa(Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    public Tarefa atualizarTarefa(Long id, Tarefa novaTarefa) {
        Tarefa tarefaExistente = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        tarefaExistente.setNome(novaTarefa.getNome());
        tarefaExistente.setDescricao(novaTarefa.getDescricao());
        tarefaExistente.setStatus(novaTarefa.getStatus());
        tarefaExistente.setObservacoes(novaTarefa.getObservacoes());
        return tarefaRepository.save(tarefaExistente);
    }

    public void deletarTarefa(Long id) {
        tarefaRepository.deleteById(id);
    }
}