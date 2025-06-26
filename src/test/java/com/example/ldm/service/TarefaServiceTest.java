package com.example.ldm.service;

import com.example.ldm.model.Tarefa;
import com.example.ldm.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; // Importe estaticamente para usar métodos como 'when', 'any', 'verify'

@ExtendWith(MockitoExtension.class) // Habilita o uso de Mockito com JUnit 5
@DisplayName("Testes do Serviço de Tarefas") // Nome de exibição para a classe de teste
public class TarefaServiceTest {

    @Mock // Cria um mock (uma versão "falsa") do TarefaRepository
    private TarefaRepository tarefaRepository;

    @InjectMocks // Injeta o mock do TarefaRepository no TarefaService
    private TarefaService tarefaService;

    // Um método que será executado antes de cada teste
    @BeforeEach
    void setUp() {
        // Inicializa os mocks. Esta linha é opcional com @ExtendWith(MockitoExtension.class)
        // mas é um bom hábito para saber o que acontece.
        // MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve criar uma nova tarefa com sucesso")
    void deveCriarTarefaComSucesso() {
        // Cenário: Criamos uma tarefa de exemplo
        Tarefa tarefaParaCriar = new Tarefa();
        tarefaParaCriar.setNome("Comprar Leite");
        tarefaParaCriar.setDescricao("Ir ao supermercado e comprar 1 litro de leite integral");
        tarefaParaCriar.setStatus("PENDENTE");
        tarefaParaCriar.setObservacoes("Não esquecer!");

        // Simulação (Stubbing): Quando o repository.save for chamado com qualquer Tarefa,
        // ele deve retornar a própria tarefa que foi passada, simulando o banco de dados.
        // Simulamos que o ID e as datas são gerados após salvar.
        Tarefa tarefaSalva = new Tarefa();
        tarefaSalva.setId(1L);
        tarefaSalva.setNome(tarefaParaCriar.getNome());
        tarefaSalva.setDescricao(tarefaParaCriar.getDescricao());
        tarefaSalva.setStatus(tarefaParaCriar.getStatus());
        tarefaSalva.setObservacoes(tarefaParaCriar.getObservacoes());
        tarefaSalva.onCreate(); // Para simular as datas de criação/atualização
        
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefaSalva);

        // Ação: Chamamos o método que queremos testar
        Tarefa resultado = tarefaService.criarTarefa(tarefaParaCriar);

        // Verificação:
        assertNotNull(resultado); // Verifica se o resultado não é nulo
        assertEquals(1L, resultado.getId()); // Verifica se o ID foi atribuído (simulado)
        assertEquals("Comprar Leite", resultado.getNome()); // Verifica se o nome está correto
        verify(tarefaRepository, times(1)).save(tarefaParaCriar); // Verifica se o método save foi chamado exatamente 1 vez com a tarefa correta
    }

    @Test
    @DisplayName("Deve listar todas as tarefas")
    void deveListarTodasAsTarefas() {
        // Cenário: Criamos algumas tarefas para simular o que viria do banco
        Tarefa tarefa1 = new Tarefa();
        tarefa1.setId(1L);
        tarefa1.setNome("Tarefa 1");
        tarefa1.setStatus("PENDENTE");

        Tarefa tarefa2 = new Tarefa();
        tarefa2.setId(2L);
        tarefa2.setNome("Tarefa 2");
        tarefa2.setStatus("CONCLUIDA");

        List<Tarefa> listaDeTarefas = Arrays.asList(tarefa1, tarefa2);

        // Simulação: Quando o repository.findAll for chamado, ele deve retornar nossa lista simulada
        when(tarefaRepository.findAll()).thenReturn(listaDeTarefas);

        // Ação: Chamamos o método que queremos testar
        List<Tarefa> resultado = tarefaService.listarTarefas();

        // Verificação:
        assertNotNull(resultado); // A lista não deve ser nula
        assertEquals(2, resultado.size()); // A lista deve conter 2 itens
        assertEquals("Tarefa 1", resultado.get(0).getNome()); // Verifica o nome da primeira tarefa
        verify(tarefaRepository, times(1)).findAll(); // Verifica se o método findAll foi chamado exatamente 1 vez
    }

    @Test
    @DisplayName("Deve deletar uma tarefa por ID")
    void deveDeletarTarefaPorId() {
        // Ação: Chamamos o método de deletar.
        // Não precisamos de stubbing aqui porque deleteById é um método void.
        tarefaService.deletarTarefa(1L);

        // Verificação:
        // Verifica se o método deleteById do repositório foi chamado exatamente 1 vez com o ID 1L
        verify(tarefaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve atualizar uma tarefa existente")
    void deveAtualizarTarefaExistente() {
        // Cenário: Tarefa existente no banco
        Tarefa tarefaExistente = new Tarefa();
        tarefaExistente.setId(1L);
        tarefaExistente.setNome("Comprar Pão");
        tarefaExistente.setDescricao("Ir na padaria");
        tarefaExistente.setStatus("PENDENTE");
        tarefaExistente.onCreate(); // Definir datas iniciais

        // Nova versão da tarefa com dados atualizados
        Tarefa novaTarefa = new Tarefa();
        novaTarefa.setNome("Comprar Pão e Café");
        novaTarefa.setDescricao("Ir na padaria e na cafeteria");
        novaTarefa.setStatus("CONCLUIDA");
        novaTarefa.setObservacoes("Pegar na porta");

        // Simulação:
        // 1. Quando findById for chamado com ID 1L, retorna a tarefa existente.
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefaExistente));
        // 2. Quando save for chamado com qualquer Tarefa, retorna a tarefa salva (atualizada).
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefaExistente);

        // Ação:
        Tarefa resultadoAtualizacao = tarefaService.atualizarTarefa(1L, novaTarefa);

        // Verificação:
        assertNotNull(resultadoAtualizacao);
        assertEquals(1L, resultadoAtualizacao.getId());
        assertEquals("Comprar Pão e Café", resultadoAtualizacao.getNome());
        assertEquals("CONCLUIDA", resultadoAtualizacao.getStatus());
        assertEquals("Pegar na porta", resultadoAtualizacao.getObservacoes());
        // Verifica se findById e save foram chamados
        verify(tarefaRepository, times(1)).findById(1L);
        verify(tarefaRepository, times(1)).save(tarefaExistente);
    }
}