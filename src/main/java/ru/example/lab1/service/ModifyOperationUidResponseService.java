package ru.example.lab1.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.example.lab1.model.Response;

import java.util.UUID;

@Slf4j
@Service
@Qualifier("ModifyOperationUidResponseService")
public class ModifyOperationUidResponseService
        implements ModifyResponseService {

    @Override
    public Response modify(Response response) {
        String oldOperationUid = response.getOperationUid();
        String newOperationUid = UUID.randomUUID().toString();

        response.setOperationUid(newOperationUid);

        log.info("Изменён operationUid: {} -> {}",
                oldOperationUid, newOperationUid);

        return response;
    }
}