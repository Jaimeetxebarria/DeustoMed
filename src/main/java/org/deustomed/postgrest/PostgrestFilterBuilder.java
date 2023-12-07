package org.deustomed.postgrest;


import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class PostgrestFilterBuilder extends PostgrestTransformBuilder {
    private final PostgrestQuery postgrestQuery;
    private boolean negateNextFilter = false;

    // Keeps the number of filters to apply the logical operator to
    private final Stack<Integer> logicalOperatorCounts = new Stack<>();

    // Keeps the logical operator to apply to the next n filters
    private final Stack<String> logicalOperatorStack = new Stack<>();

    // Keeps the subexpressions of each logical operator. E.g. ["column1.eq.value1", "column2.eq.value2"]
    private final Stack<List<String>> logicalOperatorExpressionFilters = new Stack<>();

    public PostgrestFilterBuilder() {
        super(new PostgrestQuery());
        postgrestQuery = super.getQuery();
    }

    public PostgrestFilterBuilder(PostgrestQuery postgrestQuery) {
        super(postgrestQuery);
        this.postgrestQuery = postgrestQuery;
    }

    public PostgrestQuery getQuery() {
        return postgrestQuery;
    }

    /**
     * Equal to
     */
    public PostgrestFilterBuilder eq(@NotNull String column, @NotNull String value) {
        this.addFilter(column, "eq", value);
        return this;
    }

    /**
     * Not equal to
     */
    public PostgrestFilterBuilder neq(@NotNull String column, @NotNull String value) {
        this.addFilter(column, "neq", value);
        return this;
    }

    /**
     * Less than
     */
    public PostgrestFilterBuilder lt(@NotNull String column, @NotNull String value) {
        this.addFilter(column, "lt", value);
        return this;
    }

    /**
     * Greater than
     */
    public PostgrestFilterBuilder gt(@NotNull String column, @NotNull String value) {
        this.addFilter(column, "gt", value);
        return this;
    }


    /**
     * Greater than or equal to
     */
    public PostgrestFilterBuilder gte(@NotNull String column, @NotNull String value) {
        this.addFilter(column, "gte", value);
        return this;
    }

    /**
     * Less than or equal to
     */
    public PostgrestFilterBuilder lte(@NotNull String column, @NotNull String value) {
        this.addFilter(column, "lte", value);
        return this;
    }

    /**
     * Like
     */
    public PostgrestFilterBuilder like(@NotNull String column, @NotNull String value) {
        this.addFilter(column, "like", value);
        return this;
    }

    /**
     * Ilike (case-insensitive like)
     */
    public PostgrestFilterBuilder ilike(@NotNull String column, @NotNull String value) {
        this.addFilter(column, "ilike", value);
        return this;
    }

    /**
     * Is checks for null, true or false
     */
    public PostgrestFilterBuilder is(@NotNull String column, Boolean value) {
        if (value == null) this.addFilter(column, "is", "null");
        else this.addFilter(column, "is", value.toString());

        return this;
    }

    /**
     * In
     */
    // TODO filter values for reserved characters: https://postgrest.org/en/v7.0.0/api.html#reserved-characters
    public PostgrestFilterBuilder in(@NotNull String column, @NotNull String... values) {
        if (values.length == 0) throw new IllegalArgumentException("Cannot use in filter with no values");
        if (Arrays.stream(values).anyMatch(value -> value == null || value.isEmpty()))
            throw new IllegalArgumentException("Cannot use in filter with empty values");

        this.addFilter(column, "in", "(" + Arrays.stream(values).map(s -> "\"" + s + "\"").collect(Collectors.joining(",")) +
                ")");
        return this;
    }

    public PostgrestFilterBuilder in(@NotNull String column, @NotNull Integer... values) {
        if (values.length == 0) throw new IllegalArgumentException("Cannot use in filter with no values");
        if (Arrays.stream(values).anyMatch(Objects::isNull))
            throw new IllegalArgumentException("Cannot use in filter with empty values");

        this.addFilter(column, "in", "(" + Arrays.stream(values).map(Object::toString).collect(Collectors.joining(",")) + ")");
        return this;
    }

    /**
     * Or the next n filters
     */
    public PostgrestFilterBuilder or(int n) {
        if (n < 2) throw new IllegalArgumentException("Cannot use OR filter with n < 2");
        logicalOperatorCounts.push(n);

        String operator = negateNextFilter ? "not.or" : "or";
        negateNextFilter = false;

        logicalOperatorStack.push(operator);
        logicalOperatorExpressionFilters.push(new ArrayList<>());
        return this;
    }

    /**
     * And the next n filters
     */
    public PostgrestFilterBuilder and(int n) {
        if (n < 2) throw new IllegalArgumentException("Cannot use AND filter with n < 2");
        logicalOperatorCounts.push(n);

        String operator = negateNextFilter ? "not.and" : "and";
        negateNextFilter = false;

        logicalOperatorStack.push(operator);
        logicalOperatorExpressionFilters.push(new ArrayList<>());
        return this;
    }

    /** Negate the next filter */
    public PostgrestFilterBuilder not() {
        negateNextFilter = true;
        return this;
    }

    private void addFilter(@NotNull String column, @NotNull String filter, @NotNull String value) {
        if (negateNextFilter) {
            filter = "not." + filter;
            negateNextFilter = false;
        }

        // No logical operators involved, just add the filter to the query
        if (logicalOperatorCounts.isEmpty()) {
            postgrestQuery.getUrlBuilder().addQueryParameter(column, filter + "." + value);
            return;
        }

        // Add the filter to the current logical operator
        logicalOperatorExpressionFilters.peek().add(column + "." + filter + "." + value);

        // If the number of elements in the expression filter list is equal to the number of filters to which the operator
        // needs to be applied, we are done with this logical operator and we can close the expression
        if (logicalOperatorExpressionFilters.peek().size() == logicalOperatorCounts.peek()) {
            closeLogicalOperatorExpression();
        }

    }

    // Closes the logical operator expression and adds it to the query if needed.                                             v
    // Checks that once the expression is closed, it does not complete another logical operator expression. E.g. and(a, or(b, c))
    private void closeLogicalOperatorExpression() {
        do {
            logicalOperatorCounts.pop();
            String currentLogicalOperator = logicalOperatorStack.pop();

            // If there are no more logical operators, add the expression to the query. Otherwise, keep constructing the
            // expression
            if (logicalOperatorStack.isEmpty()) {
                postgrestQuery.getUrlBuilder().addQueryParameter(currentLogicalOperator,
                        "(" + String.join(",", logicalOperatorExpressionFilters.stream().flatMap(List::stream).toList()) + ")");
                logicalOperatorExpressionFilters.clear();
                return;
            } else {
                String expression = currentLogicalOperator + "(" + String.join(",", logicalOperatorExpressionFilters.pop()) + ")";
                logicalOperatorExpressionFilters.peek().add(expression);
            }
        } while (logicalOperatorExpressionFilters.peek().size() == logicalOperatorCounts.peek());
    }
}
