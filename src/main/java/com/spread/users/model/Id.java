package com.spread.users.model;

import com.spread.users.model.converter.IdCharConverter;
import com.spread.users.model.primitive.IdChar;

import javax.persistence.Column;
import javax.persistence.Convert;
import java.io.Serializable;
import java.util.Objects;

public class Id implements Serializable {

    @Column(name = "id")
    @Convert(converter = IdCharConverter.class)
    private IdChar id;

    private Id() {}

    public Id(final IdChar id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Id id1 = (Id) o;
        return id.equals(id1.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Id{" +
                "id=" + id +
                '}';
    }

    public IdChar getId() {
        return id;
    }
}
