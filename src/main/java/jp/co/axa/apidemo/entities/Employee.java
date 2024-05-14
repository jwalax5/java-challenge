package jp.co.axa.apidemo.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name="EMPLOYEE")
public class Employee {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message="please input employee name")
    @Column(name="EMPLOYEE_NAME")
    private String name;

    @NotNull(message="please input salary")
    @Column(name="EMPLOYEE_SALARY")
    private Integer salary;

    @NotEmpty(message="please input department")
    @Column(name="DEPARTMENT")
    private String department;

}
