package jp.co.axa.apidemo.controllers;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.response.ApiErrorResponse;
import jp.co.axa.apidemo.response.ApiSuccessResponse;
import jp.co.axa.apidemo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get Employee Successfully", response = ApiSuccessResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized" ,response = ApiErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error" ,response = ApiErrorResponse.class)
    })
    @GetMapping(value = "/employees", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiSuccessResponse> getEmployees() {
        final String message = "Employees Get Successfully";
        List<Employee> employees = employeeService.retrieveEmployees();
        ApiSuccessResponse<Employee> response = new ApiSuccessResponse(HttpStatus.OK, message, employees);
        return new ResponseEntity<ApiSuccessResponse>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get Employee Successfully", response = ApiSuccessResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized" ,response = ApiErrorResponse.class),
            @ApiResponse(code = 404, message = "Employee not found" ,response = ApiErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error" ,response = ApiErrorResponse.class)
    })
    @GetMapping(value = "/employees/{employeeId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Transactional(readOnly = true)
    @Cacheable("employee-cache")
    public ResponseEntity<ApiSuccessResponse> getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        final String message = String.format("Employee with id : %d Get Successfully", employeeId);
        Employee employee = employeeService.getEmployee(employeeId);
        ApiSuccessResponse<Employee> response = new ApiSuccessResponse(HttpStatus.OK, message, employee);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Employee is created", response = ApiSuccessResponse.class),
            @ApiResponse(code = 400, message = "Invalid parameter(s)" ,response = ApiErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized" ,response = ApiErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error" ,response = ApiErrorResponse.class)
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/employees", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiSuccessResponse> saveEmployee(@Valid @RequestBody Employee employee) {
        employeeService.saveEmployee(employee);
        final String message = String.format("Employee with id : %d Saved Successfully", employee.getId());
        ApiSuccessResponse<Employee> response = new ApiSuccessResponse(HttpStatus.CREATED, message, employee);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Delete Employee Successfully", response = ApiSuccessResponse.class),
            @ApiResponse(code = 400, message = "Invalid parameter(s)" ,response = ApiErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized" ,response = ApiErrorResponse.class),
            @ApiResponse(code = 404, message = "Employee not found" ,response = ApiErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error" ,response = ApiErrorResponse.class)
    })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/employees/{employeeId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @CacheEvict("employee-cache")
    public ResponseEntity<ApiSuccessResponse> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        final String message = String.format("Employee with id : %d Deleted Successfully", employeeId);
        Employee emp = employeeService.getEmployee(employeeId);
        if (emp != null) {
            employeeService.deleteEmployee(employeeId);
        }
        ApiSuccessResponse<Employee> response = new ApiSuccessResponse(HttpStatus.NO_CONTENT, message, employeeId);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Update Employee Successfully", response = ApiSuccessResponse.class),
            @ApiResponse(code = 400, message = "Invalid parameter(s)" ,response = ApiErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized" ,response = ApiErrorResponse.class),
            @ApiResponse(code = 404, message = "Employee not found" ,response = ApiErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error" ,response = ApiErrorResponse.class)
    })
    @PutMapping(value = "/employees/{employeeId}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiSuccessResponse> updateEmployee(@Valid @RequestBody Employee employee, @PathVariable(name = "employeeId") Long employeeId) {
        final String message = String.format("Employee with id : %d Updated Successfully", employeeId);
        Employee emp = employeeService.getEmployee(employeeId);
        if (emp != null) {
            employeeService.updateEmployee(employee);
        }
        employee.setId(employeeId);
        ApiSuccessResponse<Employee> response = new ApiSuccessResponse(HttpStatus.OK, message, employee);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
