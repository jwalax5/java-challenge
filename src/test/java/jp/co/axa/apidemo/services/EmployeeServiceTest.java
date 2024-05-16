package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.errorhandling.ResourceNotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepo;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    public void givenEmployee_whenGetEmployee_thenReturnEmployee() {
        final Long id = 1L;
        final String name = "testName";
        final Integer salary = 100;
        final String department = "testDepartment";
        Employee mockEmployee = Employee.builder().id(id).name(name).salary(salary).department(department).build();

        Mockito.when(employeeRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(mockEmployee));

        Employee result = employeeService.getEmployee(id);
        assertTrue(id.equals(result.getId()));
        assertTrue(name.equals(result.getName()));
        assertTrue(salary.equals(result.getSalary()));
        assertTrue(department.equals(result.getDepartment()));
    }

    @Test
    public void givenNotFoundEmployee_whenGetEmployee_thenThrowResourceNotFoundException() {
        final Long id = 1L;

        Mockito.when(employeeRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService
                .getEmployee(id));
    }

    @Test
    public void givenEmployees_whenRetrieveEmployees_thenReturnEmployeeList() {
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

        Mockito.when(employeeRepo.findAll()).thenReturn(mockEmployeeList);

        List<Employee> result = employeeService.retrieveEmployees();
        assertTrue(result.size() == 2);
        assertTrue(result.containsAll(mockEmployeeList) && mockEmployeeList.containsAll(result));

    }

    @Test
    public void givenEmptyEmployee_thenRetrieveEmployees_thenReturnEmptyList() {
        List<Employee> mockEmployeeList = Arrays.asList();

        Mockito.when(employeeRepo.findAll()).thenReturn(mockEmployeeList);

        List<Employee> result = employeeService.retrieveEmployees();
        assertTrue(result.size() == 0);
    }


    // todo
    // unit test of saveEmployee

    // todo
    // unit test of updateEmployee

    // todo
    // unit test of deleteEmployee

}
