package com.josh.Service;

import com.josh.DTO.DepartmentDTO;
import com.josh.DTO.ProjectDTO;
import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.HttpExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@Service
public class DaprExternalServiceClient {

    @Autowired
    RestTemplate restTemplate;

    @Value("${external.service.department.url}")
    private String departmentServiceUrl;

    @Value("${external.service.project.url}")
    private String projectServiceUrl;

    @Value("${appId}")
    private String appId;

    @Value(("${appID}"))
    private String appID;


    public Mono<DepartmentDTO> getDepartmentById(Long departmentId) throws Exception {
        try (DaprClient daprClient = new DaprClientBuilder().build()) {
            return daprClient.invokeMethod(
                    appId,

                    departmentServiceUrl + departmentId,
                    null,
                    HttpExtension.GET,
                    DepartmentDTO.class
            );
        }
    }
    public Mono<ProjectDTO> invokeGetProjectById(Long projectId) throws Exception {
        try (DaprClient daprClient = new DaprClientBuilder().build()) {
            return daprClient.invokeMethod(
                    appID,
                    projectServiceUrl + projectId,
                    null,
                    HttpExtension.GET,
                    ProjectDTO.class
            );
        }
    }
}

