package com.spread.users.model;

import com.spread.users.model.primitive.IdChar;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notNull;

@Entity(name = "ready")
@IdClass(com.spread.users.model.Id.class)
public class Ready implements Serializable {

    @javax.persistence.Id
    private IdChar id;

    private Ready() {}

    public Ready(final IdChar id) {
        this.id = notNull(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ready ready = (Ready) o;
        return id.equals(ready.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Ready{" +
                "id=" + id +
                '}';
    }

    public IdChar getId() {
        return id;
    }
}
