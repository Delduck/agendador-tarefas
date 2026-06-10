package com.delduck.agendadortarefas.business.mapper;

import com.delduck.agendadortarefas.business.dto.TarefasDTO;
import com.delduck.agendadortarefas.infrastructure.entity.TarefasEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TarefaConverter {

    TarefasEntity paraTarefaEntity(TarefasDTO tarefasDTO);

    TarefasDTO paraTarefaDTO(TarefasEntity tarefasEntity);

    List<TarefasDTO> paraListaTarefasDTO(List<TarefasEntity> tarefasEntities);

}
