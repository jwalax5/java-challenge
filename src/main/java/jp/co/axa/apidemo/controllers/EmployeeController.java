package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.response.ApiSuccessResponse;
import jp.co.axa.apidemo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping(value = "/employees", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiSuccessResponse> getEmployees() {
        final String message = "Employees Get Successfully";
        List<Employee> employees = employeeService.retrieveEmployees();
        ApiSuccessResponse<Employee> response = new ApiSuccessResponse(HttpStatus.OK, message, employees);
        return new ResponseEntity<ApiSuccessResponse>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/employees/{employeeId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiSuccessResponse> getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        final String message = String.format("Employee with id : %d Get Successfully", employeeId);
        Employee employee = employeeService.getEmployee(employeeId);
        ApiSuccessResponse<Employee> response = new ApiSuccessResponse(HttpStatus.OK, message, employee);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/employees", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiSuccessResponse> saveEmployee(@Valid @RequestBody Employee employee) {
        employeeService.saveEmployee(employee);
        final String message = String.format("Employee with id : %d Saved Successfully", employee.getId());
        ApiSuccessResponse<Employee> response = new ApiSuccessResponse(HttpStatus.OK, message, employee);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/employees/{employeeId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiSuccessResponse> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        final String message = String.format("Employee with id : %d Deleted Successfully", employeeId);
        Employee emp = employeeService.getEmployee(employeeId);
        if (emp != null) {
            employeeService.deleteEmployee(employeeId);
        }
        ApiSuccessResponse<Employee> response = new ApiSuccessResponse(HttpStatus.OK, message, employeeId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

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
