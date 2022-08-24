package com.spread.users.model;

import com.spread.users.model.converter.*;
import com.spread.users.model.primitive.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notNull;

@Entity(name = "users")
@NamedQueries({
        @NamedQuery(name="User.findByName",
                query="SELECT e" +
                        " FROM com.spread.users.model.User e" +
                        " WHERE e.name = :name")
})
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"name"}),
        @UniqueConstraint(columnNames = {"password"})
})
@IdClass(com.spread.users.model.Id.class)
public class User implements Serializable {

    @javax.persistence.Id
    private IdChar id;

    @Convert(converter = NameConverter.class)
    private Name name;

    @Convert(converter = RoleConverter.class)
    private Role role;

    @Convert(converter = PasswordConverter.class)
    private Password password;

    @Convert(converter = CreationDateConverter.class)
    private CreationDate creationDate;

    @Convert(converter = ModifiedDateConverter.class)
    private ModifiedDate modifiedDate;

    private User() {}

    public User(final IdChar id,
                final Name name,
                final Role role,
                final Password password,
                final CreationDate creationDate,
                final ModifiedDate modifiedDate) {
        this.id = notNull(id);
        this.name = notNull(name);
        this.role = notNull(role);
        this.password = notNull(password);
        this.creationDate = notNull(creationDate);
        this.modifiedDate = notNull(modifiedDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name=" + name +
                ", role=" + role +
                ", creationDate=" + creationDate +
                ", modifiedDate=" + modifiedDate +
                '}';
    }

    public IdChar getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public Password getPassword() {
        return password;
    }

    public CreationDate getCreationDate() {
        return creationDate;
    }

    public ModifiedDate getModifiedDate() {
        return modifiedDate;
    }
}
