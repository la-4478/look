// com/lookmarket/kb/mybatis/FloatArrayBlobTypeHandler.java
package com.lookmarket.kb.mybatis;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * float[] 임베딩을 DB BLOB(MEDIUMBLOB)으로 저장/조회하는 핸들러.
 * - 저장: float 1개당 4바이트로 직렬화 (Little Endian)
 * - 조회: 바이트 배열을 float[]로 복원
 * - mybatis-config.xml에 등록해서 전역으로 사용
 */
public class FloatArrayBlobTypeHandler extends BaseTypeHandler<float[]> {
  // 바이트 순서: Little Endian (일반적인 임베딩 저장 관례)
  private static final ByteOrder ORDER = ByteOrder.LITTLE_ENDIAN;

  /** 파라미터 바인딩 시: float[] -> byte[] */
  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, float[] parameter, JdbcType jdbcType)
      throws SQLException {
    ByteBuffer bb = ByteBuffer.allocate(parameter.length * 4).order(ORDER);
    for (float f : parameter) bb.putFloat(f);
    ps.setBytes(i, bb.array()); // BLOB로 들어감
  }

  /** 조회 시: byte[] -> float[] (컬럼명으로 꺼낼 때) */
  @Override public float[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return toFloats(rs.getBytes(columnName));
  }
  /** 조회 시: byte[] -> float[] (인덱스로 꺼낼 때) */
  @Override public float[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return toFloats(rs.getBytes(columnIndex));
  }
  /** 프로시저 결과: byte[] -> float[] */
  @Override public float[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return toFloats(cs.getBytes(columnIndex));
  }

  /** 바이트 배열을 float[]로 복원 */
  private float[] toFloats(byte[] b){
    if (b == null || b.length == 0) return new float[0];
    ByteBuffer bb = ByteBuffer.wrap(b).order(ORDER);
    int n = b.length / 4;
    float[] v = new float[n];
    for (int i = 0; i < n; i++) v[i] = bb.getFloat();
    return v;
  }
}
