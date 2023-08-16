package com.ceyloncab.otpmgtservice.domain.service.assembler;

/**
 * Assembles DTO to Entity objects.
 *
 */
public interface Assembler< A, B > {

    A fromDto(B dto);

    B toDto(A model);

    B toDto(A model, Object object);
}

