package ru.lappi.users.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Nikita Gorodilov
 */
@Entity
@Table(name = "privileges")
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "privileges_id_generator")
    @SequenceGenerator(name = "privileges_id_generator", sequenceName = "privileges_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "code", length = 50, nullable = false, unique = true)
    private String code;

    @Column(name = "description", length = 200, nullable = false)
    private String description;

    @Column(name = "create_date", nullable = false, columnDefinition = "TIMESTAMP default NOW()")
    @CreationTimestamp
    private Date createDate;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "privileges")
    private List<User> users = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "privileges")
    private List<Role> roles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
