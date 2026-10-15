package ru.example.lab1.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.example.lab1.model.Response;
import ru.example.lab1.util.DateTimeUtil;

import java.util.Date;

@Slf4j
@Service
@Qualifier("ModifySystemTimeResponseService")
public class ModifySystemTimeResponseService
        implements ModifyResponseService {

    @Override
    public Response modify(Response response) {
        String oldSystemTime = response.getSystemTime();
        String newSystemTime =
                DateTimeUtil.getCustomFormat().format(new Date());

        response.setSystemTime(newSystemTime);

        log.info("Изменён systemTime: {} -> {}",
                oldSystemTime, newSystemTime);

        return response;
    }
}