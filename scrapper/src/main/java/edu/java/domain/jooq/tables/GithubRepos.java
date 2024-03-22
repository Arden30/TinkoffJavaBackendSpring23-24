/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.jooq.tables;

import edu.java.domain.jooq.DefaultSchema;
import edu.java.domain.jooq.Keys;
import edu.java.domain.jooq.tables.records.GithubReposRecord;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function3;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row3;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

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
public class GithubRepos extends TableImpl<GithubReposRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>GITHUB_REPOS</code>
     */
    public static final GithubRepos GITHUB_REPOS = new GithubRepos();

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<GithubReposRecord> getRecordType() {
        return GithubReposRecord.class;
    }

    /**
     * The column <code>GITHUB_REPOS.LINK_ID</code>.
     */
    public final TableField<GithubReposRecord, Long> LINK_ID =
        createField(DSL.name("LINK_ID"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>GITHUB_REPOS.STARS</code>.
     */
    public final TableField<GithubReposRecord, Long> STARS =
        createField(DSL.name("STARS"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>GITHUB_REPOS.ISSUES</code>.
     */
    public final TableField<GithubReposRecord, Long> ISSUES =
        createField(DSL.name("ISSUES"), SQLDataType.BIGINT.nullable(false), this, "");

    private GithubRepos(Name alias, Table<GithubReposRecord> aliased) {
        this(alias, aliased, null);
    }

    private GithubRepos(Name alias, Table<GithubReposRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>GITHUB_REPOS</code> table reference
     */
    public GithubRepos(String alias) {
        this(DSL.name(alias), GITHUB_REPOS);
    }

    /**
     * Create an aliased <code>GITHUB_REPOS</code> table reference
     */
    public GithubRepos(Name alias) {
        this(alias, GITHUB_REPOS);
    }

    /**
     * Create a <code>GITHUB_REPOS</code> table reference
     */
    public GithubRepos() {
        this(DSL.name("GITHUB_REPOS"), null);
    }

    public <O extends Record> GithubRepos(Table<O> child, ForeignKey<O, GithubReposRecord> key) {
        super(child, key, GITHUB_REPOS);
    }

    @Override
    @Nullable
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    @NotNull
    public UniqueKey<GithubReposRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_3AB;
    }

    @Override
    @NotNull
    public List<UniqueKey<GithubReposRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.CONSTRAINT_3A);
    }

    @Override
    @NotNull
    public List<ForeignKey<GithubReposRecord, ?>> getReferences() {
        return Arrays.asList(Keys.CONSTRAINT_3);
    }

    private transient Link _link;

    /**
     * Get the implicit join path to the <code>PUBLIC.LINK</code> table.
     */
    public Link link() {
        if (_link == null) {
            _link = new Link(this, Keys.CONSTRAINT_3);
        }

        return _link;
    }

    @Override
    @NotNull
    public GithubRepos as(String alias) {
        return new GithubRepos(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public GithubRepos as(Name alias) {
        return new GithubRepos(alias, this);
    }

    @Override
    @NotNull
    public GithubRepos as(Table<?> alias) {
        return new GithubRepos(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public GithubRepos rename(String name) {
        return new GithubRepos(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public GithubRepos rename(Name name) {
        return new GithubRepos(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public GithubRepos rename(Table<?> name) {
        return new GithubRepos(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row3<Long, Long, Long> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function3<? super Long, ? super Long, ? super Long, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(
        Class<U> toType,
        Function3<? super Long, ? super Long, ? super Long, ? extends U> from
    ) {
        return convertFrom(toType, Records.mapping(from));
    }
}
