package com.moriarty.base.http.domain;

import java.io.IOException;

public class FailedResultError extends IOException {
    DomainResult<?> domainResult;

    public FailedResultError(String responseJson, DomainResult<?> domainResult) {
        super(responseJson);
        this.domainResult = domainResult;
    }


    public DomainResult<?> getDomainResult() {
        return domainResult;
    }
}
