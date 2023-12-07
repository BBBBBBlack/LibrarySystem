package com.bbbbbblack.domain.entity;

import lombok.Data;

import java.util.List;

@Data
public class SQLResult<T> {
    List<T> data;
    String database;
    Long es;
    Long id;
    Boolean isDdl;
    MysqlType mysqlType;
    String old;
    List<String> pkNames;
    String sql;
    SqlType sqlType;
    String table;
    Long ts;
    String type;
}
/**
 * {
 *     "data": [
 *         {
 *             "id": "48",
 *             "create_by": "1",
 *             "subject": "12",
 *             "content": "abc",
 *             "create_time": "2022-10-08 22:43:59"
 *         }
 *     ],
 *     "database": "compus",
 *     "es": 1665240243000,
 *     "id": 1,
 *     "isDdl": false,
 *     "mysqlType": {
 *         "id": "bigint",
 *         "create_by": "bigint",
 *         "subject": "varchar(255)",
 *         "content": "varchar(255)",
 *         "create_time": "datetime"
 *     },
 *     "old": null,
 *     "pkNames": [
 *         "id"
 *     ],
 *     "sql": "",
 *     "sqlType": {
 *         "id": -5,
 *         "create_by": -5,
 *         "subject": 12,
 *         "content": 12,
 *         "create_time": 93
 *     },
 *     "table": "re_diary",
 *     "ts": 1665240243753,
 *     "type": "INSERT"
 * }
 */
