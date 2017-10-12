package com.siebre.uaa.openapiapplication.service;

import com.siebre.uaa.openapi.mapper.OpenApiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jhonelee on 2017/7/21.
 */
@Service
public class OpenApiApplicationService {

    @Autowired
    private OpenApiMapper openApiMapper;

}
