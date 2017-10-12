package com.siebre.uaa.openapi.service;

import com.siebre.uaa.openapi.entity.OpenApi;
import com.siebre.uaa.openapi.mapper.OpenApiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jhonelee on 2017/7/21.
 */
@Service
public class OpenApiService {

    @Autowired
    private OpenApiMapper openApiMapper;

    @Transactional
    public void createOpenApi(OpenApi openApi) {
        this.openApiMapper.insert(openApi);
    }


}
