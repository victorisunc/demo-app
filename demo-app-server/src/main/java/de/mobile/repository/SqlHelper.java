package de.mobile.repository;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.join;

public class SqlHelper {

    public static String select(String table, String... fields) {
        return new StringBuilder()
                .append("select ")
                .append(join(fields, ", "))
                .append(" from ")
                .append(table)
                .toString();
    }

    public static String insert(String table, String... fields) {
        return new StringBuilder()
                .append("insert into ")
                .append(table)
                .append(" (")
                .append(join(fields, ", "))
                .append(") values (")
                .append(asList(fields).stream().map(p -> ":" + p).collect(joining(", ")))
                .append(")")
                .toString();
    }

    public static String upsert(String table, List<String> insertFields, List<String> updateFields, String uniqueKey) {
        return new StringBuilder()
                .append("insert into ")
                .append(table)
                .append(" (")
                .append(join(insertFields, ", "))
                .append(") values (")
                .append(insertFields.stream().map(p -> ":" + p).collect(joining(", ")))
                .append(")")
                .append(" on conflict (").append(uniqueKey).append(") do update set ")
                .append(updateFields.stream().map(p -> p + " = :" + p).collect(joining(", ")))
                .toString();
    }

    public static String update(String table, String... fields) {
        return new StringBuilder()
                .append("update ")
                .append(table)
                .append(" set ")
                .append(asList(fields).stream().map(p -> p + " = :" + p).collect(joining(", ")))
                .toString();
    }

    public static String delete(String table) {
        return "delete from " + table;
    }

    public static String count(String table) {
        return new StringBuilder()
                .append("select count(*)")
                .append(" from ")
                .append(table)
                .toString();
    }

    public static String conditions(String... tableFields) {
        return conditions(Arrays.asList(tableFields));
    }

    public static String conditions(Map<String,? > params) {
        return conditions(params.keySet());
    }

    public static String conditions(Collection<String> tableFields) {
        return new StringBuilder()
                .append(" WHERE ")
                .append(tableFields.stream().map(p -> p + " = :" + p).collect(joining(" AND ")))
                .toString();
    }
}
