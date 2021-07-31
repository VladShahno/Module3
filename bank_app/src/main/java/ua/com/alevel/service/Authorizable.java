package ua.com.alevel.service;

import ua.com.alevel.model.dto.authorization.AuthorizationRequest;
import ua.com.alevel.model.dto.authorization.AuthorizationResult;

public interface Authorizable {

    public AuthorizationResult getAuthorize(AuthorizationRequest request) throws Exception;
}
