package jp.co.axa.apidemo.controller;

import jp.co.axa.apidemo.controllers.EmployeeController;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.errorhandling.ResourceNotFoundException;
import jp.co.axa.apidemo.response.ApiSuccessResponse;
import jp.co.axa.apidemo.services.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @Mock
    private EmployeeServiceImpl employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    public void givenEmployees_whenGetEmployees_thenReturnApiSuccessResponse() {
        final String expectedMessage = "Employees Get Successfully";
        final Long id1 = 1L;
        final String name1 = "testName1";
        final Integer salary1 = 100;
        final String department1 = "testDepartment1";
        final Long id2 = 2L;
        final String name2 = "testName2";
        final Integer salary2 = 200;
        final String department2 = "testDepartment2";
        Employee mockEmployee1 = Employee.builder().id(id1).name(name1).salary(salary1).department(department1).build();
        Employee mockEmployee2 = Employee.builder().id(id2).name(name2).salary(salary2).department(department2).build();
        List<Employee> mockEmployeeList = Arrays.asList(mockEmployee1, mockEmployee2);

        Mockito.when(employeeService.retrieveEmployees()).thenReturn(mockEmployeeList);

        ApiSuccessResponse result = employeeController.getEmployees().getBody();
        assertTrue(HttpStatus.OK.equals(result.getStatus()));
        assertTrue(expectedMessage.equals(result.getMessage()));
        assertTrue(mockEmployeeList.containsAll(result.getResults()) && result.getResults().containsAll(mockEmployeeList));
        assert (result.getResults().size() == 2);
    }

    @Test
    public void givenNotFoundEmployee_whenGetEmployee_thenThrowResourceNotFoundException() {
        final Long notExistedEmployeeId = 1L;
        final String expectedMessage = "Employee is not exist with id : " + notExistedEmployeeId;

        Mockito.when(employeeService.getEmployee(Mockito.anyLong())).thenThrow(new ResourceNotFoundException(expectedMessage));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> employeeController.getEmployee(notExistedEmployeeId));
        assertTrue(expectedMessage.equals(ex.getMessage()));
    }


    // todo
    // unit test of saveEmployee

    // todo
    // unit test of updateEmployee

    // todo
    // unit test of deleteEmployee

}
