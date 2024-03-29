/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.jooq.tables.records;

import edu.java.domain.jooq.tables.GithubRepos;
import java.beans.ConstructorProperties;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
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
public class GithubReposRecord extends UpdatableRecordImpl<GithubReposRecord> implements Record3<Long, Long, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>GITHUB_REPOS.LINK_ID</code>.
     */
    public void setLinkId(@NotNull Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>GITHUB_REPOS.LINK_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getLinkId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>GITHUB_REPOS.STARS</code>.
     */
    public void setStars(@NotNull Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>GITHUB_REPOS.STARS</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getStars() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>GITHUB_REPOS.ISSUES</code>.
     */
    public void setIssues(@NotNull Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>GITHUB_REPOS.ISSUES</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getIssues() {
        return (Long) get(2);
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
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row3<Long, Long, Long> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row3<Long, Long, Long> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return GithubRepos.GITHUB_REPOS.LINK_ID;
    }

    @Override
    @NotNull
    public Field<Long> field2() {
        return GithubRepos.GITHUB_REPOS.STARS;
    }

    @Override
    @NotNull
    public Field<Long> field3() {
        return GithubRepos.GITHUB_REPOS.ISSUES;
    }

    @Override
    @NotNull
    public Long component1() {
        return getLinkId();
    }

    @Override
    @NotNull
    public Long component2() {
        return getStars();
    }

    @Override
    @NotNull
    public Long component3() {
        return getIssues();
    }

    @Override
    @NotNull
    public Long value1() {
        return getLinkId();
    }

    @Override
    @NotNull
    public Long value2() {
        return getStars();
    }

    @Override
    @NotNull
    public Long value3() {
        return getIssues();
    }

    @Override
    @NotNull
    public GithubReposRecord value1(@NotNull Long value) {
        setLinkId(value);
        return this;
    }

    @Override
    @NotNull
    public GithubReposRecord value2(@NotNull Long value) {
        setStars(value);
        return this;
    }

    @Override
    @NotNull
    public GithubReposRecord value3(@NotNull Long value) {
        setIssues(value);
        return this;
    }

    @Override
    @NotNull
    public GithubReposRecord values(@NotNull Long value1, @NotNull Long value2, @NotNull Long value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached GithubReposRecord
     */
    public GithubReposRecord() {
        super(GithubRepos.GITHUB_REPOS);
    }

    /**
     * Create a detached, initialised GithubReposRecord
     */
    @ConstructorProperties({"linkId", "stars", "issues"})
    public GithubReposRecord(@NotNull Long linkId, @NotNull Long stars, @NotNull Long issues) {
        super(GithubRepos.GITHUB_REPOS);

        setLinkId(linkId);
        setStars(stars);
        setIssues(issues);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised GithubReposRecord
     */
    public GithubReposRecord(edu.java.domain.jooq.tables.pojos.GithubRepos value) {
        super(GithubRepos.GITHUB_REPOS);

        if (value != null) {
            setLinkId(value.getLinkId());
            setStars(value.getStars());
            setIssues(value.getIssues());
            resetChangedOnNotNull();
        }
    }
}