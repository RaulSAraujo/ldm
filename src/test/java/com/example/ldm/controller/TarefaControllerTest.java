package com.example.ldm.controller;

import com.example.ldm.model.Tarefa;
import com.example.ldm.service.TarefaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TarefaController.class)
@DisplayName("Testes do Controlador de Tarefas")
public class TarefaControllerTest {

    @Autowired
    private MockMvc mockMvc; // Objeto para simular as requisições HTTP

    @MockBean // Cria um mock do TarefaService e o injeta no TarefaController
    private TarefaService tarefaService;

    @Autowired
    private ObjectMapper objectMapper; // Para converter objetos Java para JSON e vice-versa

    @Test
    @DisplayName("GET /api/tarefas deve retornar uma lista de tarefas")
    void deveRetornarListaDeTarefas() throws Exception {
        // Cenário: Criamos uma lista de tarefas que o serviço "simulado" retornaria
        Tarefa tarefa1 = new Tarefa();
        tarefa1.setId(1L);
        tarefa1.setNome("Tarefa de Teste 1");
        tarefa1.setStatus("PENDENTE");

        Tarefa tarefa2 = new Tarefa();
        tarefa2.setId(2L);
        tarefa2.setNome("Tarefa de Teste 2");
        tarefa2.setStatus("CONCLUIDA");

        List<Tarefa> tarefas = Arrays.asList(tarefa1, tarefa2);

        // Simulação: Quando tarefaService.listarTarefas() for chamado, retorna nossa
        // lista
        when(tarefaService.listarTarefas()).thenReturn(tarefas);

        // Ação e Verificação: Simula a requisição GET e verifica a resposta
        mockMvc.perform(get("/api/tarefas"))
                .andExpect(status().isOk()) // Espera status HTTP 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Espera Content-Type JSON
                .andExpect(jsonPath("$", hasSize(2))) // Espera uma lista com 2 elementos
                .andExpect(jsonPath("$[0].nome", is("Tarefa de Teste 1"))) // Verifica o nome da primeira tarefa
                .andExpect(jsonPath("$[1].nome", is("Tarefa de Teste 2"))); // Verifica o nome da segunda tarefa
    }

    @Test
    @DisplayName("POST /api/tarefas deve criar uma nova tarefa")
    void deveCriarNovaTarefa() throws Exception {
        // Cenário: Tarefa que será enviada na requisição
        Tarefa novaTarefaInput = new Tarefa();
        novaTarefaInput.setNome("Nova Tarefa");
        novaTarefaInput.setDescricao("Descrição da nova tarefa");
        novaTarefaInput.setStatus("PENDENTE");

        // Tarefa que o serviço "simulado" retornaria após salvar (com ID e datas)
        Tarefa tarefaCriada = new Tarefa();
        tarefaCriada.setId(3L);
        tarefaCriada.setNome("Nova Tarefa");
        tarefaCriada.setDescricao("Descrição da nova tarefa");
        tarefaCriada.setStatus("PENDENTE");
        tarefaCriada.setDataCriacao(LocalDateTime.now());
        tarefaCriada.setDataAtualizacao(LocalDateTime.now());

        // Simulação: Quando tarefaService.criarTarefa() for chamado com qualquer
        // Tarefa, retorna a tarefa criada
        when(tarefaService.criarTarefa(any(Tarefa.class))).thenReturn(tarefaCriada);

        // Ação e Verificação: Simula a requisição POST e verifica a resposta
        mockMvc.perform(post("/api/tarefas")
                .contentType(MediaType.APPLICATION_JSON) // Define o tipo de conteúdo como JSON
                .content(objectMapper.writeValueAsString(novaTarefaInput))) // Converte o objeto Tarefa para JSON
                .andExpect(status().isOk()) // Espera status HTTP 200 OK
                .andExpect(jsonPath("$.id", is(3))) // Verifica o ID da tarefa criada
                .andExpect(jsonPath("$.nome", is("Nova Tarefa"))); // Verifica o nome
    }

    @Test
    @DisplayName("PUT /api/tarefas/{id} deve atualizar uma tarefa existente")
    void deveAtualizarTarefaExistente() throws Exception {
        // Cenário: ID da tarefa a ser atualizada
        Long tarefaId = 1L;
        // Dados da tarefa atualizados a serem enviados
        Tarefa tarefaAtualizadaInput = new Tarefa();
        tarefaAtualizadaInput.setNome("Tarefa Atualizada");
        tarefaAtualizadaInput.setStatus("CONCLUIDA");

        // Tarefa que o serviço "simulado" retornaria
        Tarefa tarefaRetornada = new Tarefa();
        tarefaRetornada.setId(tarefaId);
        tarefaRetornada.setNome(tarefaAtualizadaInput.getNome());
        tarefaRetornada.setStatus(tarefaAtualizadaInput.getStatus());

        // Simulação: Quando tarefaService.atualizarTarefa() for chamado, retorna a
        // tarefa atualizada
        when(tarefaService.atualizarTarefa(any(Long.class), any(Tarefa.class))).thenReturn(tarefaRetornada);

        // Ação e Verificação: Simula a requisição PUT e verifica a resposta
        mockMvc.perform(put("/api/tarefas/{id}", tarefaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarefaAtualizadaInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(tarefaId.intValue())))
                .andExpect(jsonPath("$.nome", is("Tarefa Atualizada")))
                .andExpect(jsonPath("$.status", is("CONCLUIDA")));
    }

    @Test
    @DisplayName("DELETE /api/tarefas/{id} deve deletar uma tarefa")
    void deveDeletarTarefa() throws Exception {
        // Cenário: ID da tarefa a ser deletada
        Long tarefaId = 1L;

        // Simulação: Quando tarefaService.deletarTarefa() for chamado, não faz nada
        // (void method)
        doNothing().when(tarefaService).deletarTarefa(tarefaId);

        // Ação e Verificação: Simula a requisição DELETE e verifica a resposta
        mockMvc.perform(delete("/api/tarefas/{id}", tarefaId))
                .andExpect(status().isNoContent()); // Espera status HTTP 204 No Content
    }
}