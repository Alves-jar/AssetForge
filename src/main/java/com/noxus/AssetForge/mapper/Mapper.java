package com.noxus.AssetForge.mapper;

public interface Mapper<T, ResponseDTO, RequestDTO> {

    ResponseDTO toDTO(T entity);
    T toEntity(RequestDTO dto);
}
