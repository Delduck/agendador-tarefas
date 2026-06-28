package com.delduck.agendadortarefas.business;

import com.delduck.agendadortarefas.business.dto.TarefasDTO;
import com.delduck.agendadortarefas.business.mapper.TarefaConverter;
import com.delduck.agendadortarefas.business.mapper.TarefaUpdateConverter;
import com.delduck.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.delduck.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.delduck.agendadortarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.delduck.agendadortarefas.infrastructure.repository.TarefasRepository;
import com.delduck.agendadortarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefaConverter tarefaConverter;
    private final TarefaUpdateConverter tarefaUpdateConverter;
    private final JwtUtil jwtUtil;

    public TarefasDTO gravarTarefa(String token, TarefasDTO tarefasDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));
        tarefasDTO.setEmailUsuario(email);
        tarefasDTO.setDataCriacao(LocalDateTime.now());
        tarefasDTO.setStatus(StatusNotificacaoEnum.PENDENTE);

        TarefasEntity tarefa = tarefaConverter.paraTarefaEntity(tarefasDTO);
        return tarefaConverter.paraTarefaDTO(tarefasRepository.save(tarefa));
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial,
                                                            LocalDateTime dataFinal) {
        return tarefaConverter.paraListaTarefasDTO(
                tarefasRepository.findByDataEventoBetweenAndStatus(dataInicial, dataFinal, StatusNotificacaoEnum.PENDENTE));
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorEmail(String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        return tarefaConverter.paraListaTarefasDTO(tarefasRepository.findByEmailUsuario(email));
    }

    public void deletaTarefaPorId(String idTarefa) {
        try{
            tarefasRepository.deleteById(idTarefa);
        }catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Recurso inexistente", e.getCause());
        }
    }

    public TarefasDTO alteraStatus(StatusNotificacaoEnum status, String idTarefa) {
        try {
            TarefasEntity tarefasEntity = tarefasRepository.findById(idTarefa).orElseThrow(() ->
                    new ResourceNotFoundException("Recurso inexistente"));
            tarefasEntity.setStatus(status);
            return tarefaConverter.paraTarefaDTO(tarefasRepository.save(tarefasEntity));
        }catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar o status da tarefa", e.getCause());
        }
    }

    public TarefasDTO updateTarefas(TarefasDTO tarefasDTO, String idTarefa) {
        try {
            TarefasEntity tarefasEntity = tarefasRepository.findById(idTarefa).orElseThrow(() ->
                    new ResourceNotFoundException("Recurso inexistente"));
            tarefaUpdateConverter.updateTarefas(tarefasDTO, tarefasEntity);
            return tarefaConverter.paraTarefaDTO(tarefasRepository.save(tarefasEntity));
        }catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar o status da tarefa", e.getCause());
        }
    }



}
