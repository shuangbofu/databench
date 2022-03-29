package org.example.databench.common.utils;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.example.databench.common.domain.query.result.MetaColumn;
import org.example.databench.common.domain.query.result.ResultMeta;
import org.example.databench.common.domain.query.result.SqlResult;
import org.example.databench.common.utils.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JdbcUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcUtils.class);

    public static int executeUpdate(Connection connection, String sql) throws Exception {
        return execute(connection, sql, PreparedStatement::executeUpdate, i -> i);
    }

    public static SqlResult executeQuery(Connection connection, String sql) throws Exception {
        return execute(connection, sql, PreparedStatement::executeQuery, rs -> {
            try (rs) {
                int columnCount = rs.getMetaData().getColumnCount();
                List<List<Object>> valuesList = new LinkedList<>();
                var meta = new MetaValueGet(rs.getMetaData(), 1);
                var resultMeta = new ResultMeta(meta.value(ResultSetMetaData::getSchemaName), meta.value(ResultSetMetaData::getCatalogName));
                // set meta
                var res = IntStream.range(1, columnCount + 1)
                        .mapToObj(i -> {
                            try {
                                var m = new MetaValueGet(rs.getMetaData(), i);
                                return new MetaColumn(
                                        m.value(ResultSetMetaData::getColumnName),
                                        m.value(ResultSetMetaData::getColumnClassName),
                                        m.value(ResultSetMetaData::getColumnTypeName),
                                        m.value(ResultSetMetaData::getColumnType),
                                        m.value(ResultSetMetaData::getColumnLabel),
                                        m.value(ResultSetMetaData::getColumnDisplaySize),
                                        m.value(ResultSetMetaData::getPrecision),
                                        m.value(ResultSetMetaData::getScale)
                                );
                            } catch (Exception e) {
                                return null;
                            }
                        }).collect(Collectors.toList());
                resultMeta.getMetaColumns().addAll(res);
                // set data
                while (rs.next()) {
                    var values = new LinkedList<>();
                    IntStream.range(1, columnCount + 1).forEach(i -> values
                            .add(ExceptionUtils.roundExRtNull(() -> rs.getObject(i))));
                    valuesList.add(values);
                }
                return new SqlResult(resultMeta, valuesList);
            }
        });
    }

    private static <B, C> C execute(Connection connection, String sql,
                                    Function<PreparedStatement, B, SQLException> fuc, Function<B, C, SQLException> fuc2) throws Exception {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            return fuc2.apply(fuc.apply(statement));
        } finally {
            if (statement != null) {
                ExceptionUtils.roundEx(statement::close, "Close statement error");
            }
        }
    }

    @FunctionalInterface
    public interface EntryTransformer<K, V1, V2> extends Serializable {
        V2 transformEntry(@Nullable K var1, @Nullable V1 var2) throws Exception;
    }

    static class MetaValueGet {
        private final ResultSetMetaData metaData;
        private final int index;

        public MetaValueGet(ResultSetMetaData metaData, int index) {
            this.metaData = metaData;
            this.index = index;
        }

        public static SerializedLambda getSerializedLambda(Serializable fn) throws Exception {
            var method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            return (SerializedLambda) method.invoke(fn);
        }

        private <T> T value(EntryTransformer<ResultSetMetaData, Integer, T> f) {
            try {
                return f.transformEntry(metaData, index);
            } catch (Exception e) {
                try {
                    var columnName = metaData.getColumnName(index);
                    var sl = getSerializedLambda(f);
                    LOGGER.warn("Method {} - {} not supported", columnName, sl.getImplMethodName());
                } catch (Exception ignored) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
