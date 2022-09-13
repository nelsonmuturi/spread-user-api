package com.spread.users.model;

import com.spread.users.model.converter.CreationDateConverter;
import com.spread.users.model.converter.TokenConverter;
import com.spread.users.model.primitive.CreationDate;
import com.spread.users.model.primitive.IdChar;
import com.spread.users.model.primitive.Token;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notNull;

@Entity(name = "refreshTokens")
@NamedQueries({
        @NamedQuery(name="RefreshToken.findByToken",
                query="SELECT e" +
                        " FROM com.spread.users.model.RefreshToken e" +
                        " WHERE e.token = :token"),
        @NamedQuery(name="RefreshToken.findByUserId",
                query="SELECT e" +
                        " FROM com.spread.users.model.RefreshToken e" +
                        " WHERE e.user.id = :userId")
})
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"userId"}),
        @UniqueConstraint(columnNames = {"token"})
})
@IdClass(com.spread.users.model.Id.class)
public class RefreshToken implements Serializable {

    @javax.persistence.Id
    private IdChar id;

    @ManyToOne
    @JoinColumn(name="userId",
            referencedColumnName = "id")
    private User user;

    @Convert(converter = TokenConverter.class)
    private Token token;

    @Convert(converter = CreationDateConverter.class)
    private CreationDate creationDate;

    private RefreshToken() {}

    public RefreshToken(final IdChar id,
                        final User user,
                        final Token token,
                        final CreationDate creationDate) {
        this.id = notNull(id);
        this.user = notNull(user);
        this.token = notNull(token);
        this.creationDate = notNull(creationDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefreshToken that = (RefreshToken) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "id=" + id +
                ", user=" + user +
                ", creationDate=" + creationDate +
                '}';
    }

    public IdChar getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Token getToken() {
        return token;
    }

    public CreationDate getCreationDate() {
        return creationDate;
    }
}
