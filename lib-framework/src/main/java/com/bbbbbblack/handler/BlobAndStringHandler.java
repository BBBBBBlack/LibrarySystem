package com.bbbbbblack.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.*;

public class BlobAndStringHandler extends BaseTypeHandler<String> {
    protected static Log logger = LogFactory.getLog(BlobAndStringHandler.class);
    // 指定字符集
    private static final String DEFAULT_CHARSET = "utf-8";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        logger.info("【BlobTypeHandler】insert或者update时处理blob字段:");
        //声明一个输入流对象
        ByteArrayInputStream bis=null;
        try {
            // 把String转化成byte流
            //把字符串转为字节流
            bis = new ByteArrayInputStream(parameter.getBytes(DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            logger.error("【BlobTypeHandler】insert或者update处理blob字段出错，错误原因为：" + e);
            throw new RuntimeException("Blob Encoding Error!");
        }finally {
            if (bis!= null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    logger.error("【BlobTypeHandler】insert或者update处理blob字段出错，错误原因为：" + e);
                    throw new RuntimeException("Blob Encoding Error!");
                }
            }
        }
        ps.setBinaryStream(i, bis, parameter.length());
    }
    @Override
    public String getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        logger.info("【BlobTypeHandler】query查询时处理blob字段");
        Blob blob = rs.getBlob(columnName);
        byte[] returnValue = null;
        String result = null;
        if (null != blob) {
            //将取出的流对象转为utf-8的字符串对象
            returnValue = blob.getBytes(1, (int) blob.length());
        }
        try {
            if (null != returnValue) {
                // 把byte转化成string
                result = new String(returnValue, DEFAULT_CHARSET);
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("【BlobTypeHandler】查询处理blob字段出错，错误原因为：" + e);
            throw new RuntimeException("Blob Encoding Error!");
        }
        return result;
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        logger.info("【BlobTypeHandler】query查询时处理blob字段");

        Blob blob = cs.getBlob(columnIndex);
        byte[] returnValue = null;
        String result = null;
        if (null != blob) {

            returnValue = blob.getBytes(1, (int) blob.length());
        }
        try {
            if (null != returnValue) {
                //将取出的流对象转为utf-8的字符串对象
                return new String(returnValue,DEFAULT_CHARSET );
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("【BlobTypeHandler】查询处理blob字段出错，错误原因为：" + e);
            throw new RuntimeException("Blob Encoding Error!");
        }
        return result;
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnName)
            throws SQLException {
        logger.info("【BlobTypeHandler】query查询时处理blob字段");
        String result = null;
        Blob blob = rs.getBlob(columnName);
        byte[] returnValue = null;
        if (null != blob) {
            returnValue = blob.getBytes(1, (int) blob.length());
        }
        try {
            // 把byte转化成string
            if (null != returnValue) {
//                result = new String(returnValue, DEFAULT_CHARSET);
                //将取出的流对象转为utf-8的字符串对象
                return new String(returnValue, DEFAULT_CHARSET);
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("【BlobTypeHandler】查询处理blob字段出错，错误原因为：" + e);
            throw new RuntimeException("Blob Encoding Error!");
        }
        return result;

    }

}
