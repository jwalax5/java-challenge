package jp.co.axa.apidemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.IsNot.not;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ApiDemoApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();
    }


    @Test
    public void givenEmployeeId_whenGetEmployee_thenReturnEmployee()
            throws Exception {
        // given - employee existed in database
        Employee employee = Employee.builder()
                .name("testName")
                .salary(100)
                .department("testDepartment")
                .build();
        employeeRepository.save(employee);

        // when - call api getEmployee
        ResultActions response = mvc.perform(get("/api/v1/employees/{id}", employee.getId()));

        // then - expect success with http status 200
        response.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.['result'].name", is(employee.getName())))
                .andExpect(jsonPath("$.['result'].salary", is(employee.getSalary())))
                .andExpect(jsonPath("$.['result'].department", is(employee.getDepartment())));
    }

    @Test
    public void givenEmployee_whenSaveEmployee_thenStatus200()
            throws Exception {
        // given - setup of employee
        Employee employee = Employee.builder()
                .name("testName")
                .salary(100)
                .department("testDepartment")
                .build();

        // when - call api saveEmployee
        ResultActions response = mvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then -  expect success with http status 201
        response.andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.['result'].name", is(employee.getName())))
                .andExpect(jsonPath("$.['result'].salary", is(employee.getSalary())))
                .andExpect(jsonPath("$.['result'].department", is(employee.getDepartment())));
    }

    @Test
    public void givenInvalidEmployee_whenSaveEmployees_thenStatus400()
            throws Exception {
        // given - setup of invalid employee
        Employee invalidEmployee = Employee.builder()
                .name("    ")
                .salary(-999)
                .department("   ")
                .build();

        // when - call api saveEmployee
        ResultActions response = mvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmployee)));

        // then -  expect error with http status 400
        response.andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Invalid parameter(s)")))
                .andExpect(jsonPath("$.errors", not(empty())));
    }

    @Test
    public void givenUnsupportedContentType_whenSaveEmployees_thenStatus415()
            throws Exception {

        // given - setup of employee
        Employee employee = Employee.builder()
                .name("testName")
                .salary(100)
                .department("testDepartment")
                .build();

        // when - call api saveEmployee with not support content type
        ResultActions response = mvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_XML)
                .content(objectMapper.writeValueAsString(employee)));

        // then -  expect error with http status 415
        response.andDo(print()).andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.message", is("Content type 'application/xml' not supported")));
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployee()
            throws Exception {

        // given - employee existed in database
        Employee employee = Employee.builder()
                .name("testName")
                .salary(100)
                .department("testDepartment")
                .build();
        employeeRepository.save(employee);

        Employee updatedEmployee = Employee.builder()
                .name("updatedName")
                .salary(200)
                .department("updatedDepartment")
                .build();

        // when - call api updateEmployee
        ResultActions response = mvc.perform(put("/api/v1/employees/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then -  expect success with http status 200
        response.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.['result'].name", is(updatedEmployee.getName())))
                .andExpect(jsonPath("$.['result'].salary", is(updatedEmployee.getSalary())))
                .andExpect(jsonPath("$.['result'].department", is(updatedEmployee.getDepartment())));
    }


    @Test
    public void givenNotExistEmployee_whenUpdateEmployee_thenStatus404NotFound()
            throws Exception {

        // given - employee not existed in database
        Employee updatedEmployee = Employee.builder()
                .name("testName")
                .salary(100)
                .department("testDepartment")
                .build();
        Long nonExistId = 1L;

        // when - call api updateEmployee
        ResultActions response = mvc.perform(put("/api/v1/employees/{id}", nonExistId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - expect error with http status 404
        response.andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Employee is not exist with id : " + nonExistId)))
                .andExpect(jsonPath("$.errors", not(empty())));
    }


    // todo
    // integration test of deleteEmployee


}
