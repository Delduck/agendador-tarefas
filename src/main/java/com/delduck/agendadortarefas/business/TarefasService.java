package com.delduck.agendadortarefas.business;

import com.delduck.agendadortarefas.business.dto.TarefasDTO;
import com.delduck.agendadortarefas.business.mapper.TarefaConverter;
import com.delduck.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.delduck.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
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
    private final JwtUtil jwtUtil;

    public TarefasDTO gravarTarefa(String token, TarefasDTO tarefasDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));
        tarefasDTO.setEmailUsuario(email);
        tarefasDTO.setDataCriacao(LocalDateTime.now());
        tarefasDTO.setStatus(StatusNotificacaoEnum.PENDENTE);

        TarefasEntity tarefa = tarefaConverter.paraTarefaEntity(tarefasDTO);
        return tarefaConverter.paraTarefaDTO(tarefasRepository.save(tarefa));
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return tarefaConverter.paraListaTarefasDTO(tarefasRepository.findByDataEventoBetween(dataInicial, dataFinal));
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorEmail(String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        return tarefaConverter.paraListaTarefasDTO(tarefasRepository.findByEmailUsuario(email));
    }



}
