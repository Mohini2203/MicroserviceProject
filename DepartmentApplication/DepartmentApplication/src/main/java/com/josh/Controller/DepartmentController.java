package com.josh.Controller;

import com.josh.DTO.DepartmentDTO;
import com.josh.Entity.Department;
import com.josh.Mapper.DepartmentMapper;
import com.josh.Service.DepartmentService;
//import com.josh.Service.NotificationConsumerService;
import io.dapr.Topic;
import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.CloudEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/departments")

public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

//    @Value("${appId}")
//    private String appId;

//    @Value("${dapr.http.port}")
//    private int daprHttpPort;

//    @Autowired
//    NotificationConsumerService notificationConsumerService;


    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartment();
        // List<DepartmentDTO> departmentDTOS = departments.stream().map(DepartmentMapper::departmentDTOMapper).toList();
        List<DepartmentDTO> departmentDTO = departments.stream().map(DepartmentMapper::departmentDTOMapper).collect(Collectors.toList());
        return new ResponseEntity<>(departmentDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartment(@PathVariable("id") Long id) {
        Department department = departmentService.getDepartment(id);
        DepartmentDTO departmentDTO = DepartmentMapper.departmentDTOMapper(department);
        return new ResponseEntity<>(departmentDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DepartmentDTO> addDepartment(@RequestBody DepartmentDTO departmentDTO) {
        Department departmentToAdd = DepartmentMapper.departmentMapper(departmentDTO);
        Department addedDepartment = departmentService.addDepartment(departmentToAdd);
        DepartmentDTO addedDepartmentDTO = DepartmentMapper.departmentDTOMapper(addedDepartment);
        return new ResponseEntity<>(addedDepartmentDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@RequestBody DepartmentDTO departmentDTO, @PathVariable("id") Long id) {
        Department departmentToUpdate = DepartmentMapper.departmentMapper(departmentDTO);
        Department updatedDepartment = departmentService.updateDepartment(departmentToUpdate, id);
        DepartmentDTO updatedDepartmentDTO = DepartmentMapper.departmentDTOMapper(updatedDepartment);
        return new ResponseEntity<>(updatedDepartmentDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDepartment(@PathVariable("id") Long id) {
        departmentService.deleteDepartment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



//    @Topic(name = "employee-topic", pubsubName = "employee-pub-sub")
//    @PostMapping(path = "/department", consumes = MediaType.ALL_VALUE)
//    public Mono<Void> getConsume(@RequestBody(required = false) CloudEvent<String> cloudEvent) {
//        return Mono.fromRunnable(() -> {
//            try {
//                String message = cloudEvent.getData();
//                log.info("Subscriber received: " + message);
//
//
//                notificationConsumerService.receiveMessage(message);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }

    private static final Logger log = LoggerFactory.getLogger(DepartmentController.class);

    @Topic(name = "employee-department", pubsubName = "employee-pub-sub")
    @PostMapping(path = "/department")


    public Mono<Void> getConsume(@RequestBody(required = false) CloudEvent<String> cloudEvent) {
        return Mono.fromRunnable(() -> {
            try {
                log.info("subscriber received"+cloudEvent.getData());
            }catch (Exception e){
                throw  new RuntimeException(e);
            }
        });

    }



}