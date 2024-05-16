package jp.co.axa.apidemo.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "EMPLOYEE")
public class Employee implements Serializable {
    private static final long serialVersionUID = 1446398935944895849L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "please input employee name")
    @Column(name = "EMPLOYEE_NAME")
    private String name;

    @NotNull(message = "please input salary")
    @Column(name = "EMPLOYEE_SALARY")
    @Max(value = Integer.MAX_VALUE, message = "please input valid salary")
    @Min(value = 0, message = "please input valid salary")
    private Integer salary;

    @NotBlank(message = "please input department")
    @Column(name = "DEPARTMENT")
    private String department;

}
