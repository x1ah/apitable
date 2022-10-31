package com.vikadata.api.modular.idaas.service;

/**
 * <p>
 * IDaaS Address book
 * </p>
 */
public interface IIdaasContactService {

    /**
     * Synchronize contacts
     *
     * @param spaceId Space ID
     * @param userId User ID
     */
    void syncContact(String spaceId, Long userId);

}
