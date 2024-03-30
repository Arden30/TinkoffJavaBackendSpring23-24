/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.jooq.tables.records;

import edu.java.domain.jooq.tables.Chat;
import java.beans.ConstructorProperties;
import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;

/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class ChatRecord extends UpdatableRecordImpl<ChatRecord> implements Record2<Long, OffsetDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>CHAT.ID</code>.
     */
    public void setId(@NotNull Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>CHAT.ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>CHAT.CREATED_AT</code>.
     */
    public void setCreatedAt(@NotNull OffsetDateTime value) {
        set(1, value);
    }

    /**
     * Getter for <code>CHAT.CREATED_AT</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public OffsetDateTime getCreatedAt() {
        return (OffsetDateTime) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row2<Long, OffsetDateTime> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row2<Long, OffsetDateTime> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return Chat.CHAT.ID;
    }

    @Override
    @NotNull
    public Field<OffsetDateTime> field2() {
        return Chat.CHAT.CREATED_AT;
    }

    @Override
    @NotNull
    public Long component1() {
        return getId();
    }

    @Override
    @NotNull
    public OffsetDateTime component2() {
        return getCreatedAt();
    }

    @Override
    @NotNull
    public Long value1() {
        return getId();
    }

    @Override
    @NotNull
    public OffsetDateTime value2() {
        return getCreatedAt();
    }

    @Override
    @NotNull
    public ChatRecord value1(@NotNull Long value) {
        setId(value);
        return this;
    }

    @Override
    @NotNull
    public ChatRecord value2(@NotNull OffsetDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    @NotNull
    public ChatRecord values(@NotNull Long value1, @NotNull OffsetDateTime value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ChatRecord
     */
    public ChatRecord() {
        super(Chat.CHAT);
    }

    /**
     * Create a detached, initialised ChatRecord
     */
    @ConstructorProperties({"id", "createdAt"})
    public ChatRecord(@NotNull Long id, @NotNull OffsetDateTime createdAt) {
        super(Chat.CHAT);

        setId(id);
        setCreatedAt(createdAt);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised ChatRecord
     */
    public ChatRecord(edu.java.domain.jooq.tables.pojos.Chat value) {
        super(Chat.CHAT);

        if (value != null) {
            setId(value.getId());
            setCreatedAt(value.getCreatedAt());
            resetChangedOnNotNull();
        }
    }
}
