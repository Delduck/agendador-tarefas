package com.delduck.agendadortarefas.controller;

import com.delduck.agendadortarefas.business.TarefasService;
import com.delduck.agendadortarefas.business.dto.TarefasDTO;
import com.delduck.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class TarefasController {

    private final TarefasService tarefasService;

    @PostMapping
    public ResponseEntity<TarefasDTO> gravarTarefas(@RequestBody TarefasDTO tarefasDTO,
                                                    @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(tarefasService.gravarTarefa(token, tarefasDTO));
    }

    @GetMapping("/eventos")
    public ResponseEntity<List<TarefasDTO>> buscaListaTarefasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal) {

        return ResponseEntity.ok(tarefasService.buscaTarefasAgendadasPorPeriodo(dataInicial, dataFinal));
    }

    @GetMapping
    public ResponseEntity<List<TarefasDTO>> buscaListaTarefasPorEmail(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(tarefasService.buscaTarefasAgendadasPorEmail(token));
    }

    @DeleteMapping("/{idTarefa}")
    public ResponseEntity<Void> deletarTarefaPorId(@PathVariable String idTarefa) {
        tarefasService.deletaTarefaPorId(idTarefa);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{idTarefa}")
    public ResponseEntity<TarefasDTO> alterarStatusNotificacao(@RequestParam StatusNotificacaoEnum status,
                                                               @PathVariable String idTarefa) {
        return ResponseEntity.ok(tarefasService.alteraStatus(status, idTarefa));
    }

    @PutMapping
    public ResponseEntity<TarefasDTO> updateTarefas(@RequestBody TarefasDTO tarefasDTO,
                                                    @RequestParam String id) {
        return ResponseEntity.ok(tarefasService.updateTarefas(tarefasDTO, id));
    }

}
