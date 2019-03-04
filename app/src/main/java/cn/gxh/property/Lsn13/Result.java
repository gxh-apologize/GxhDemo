package cn.gxh.property.Lsn13;// automatically generated, do not modify

import java.nio.*;
import java.lang.*;
import java.util.*;

import cn.gxh.property.Lsn13.flatc.FlatBufferBuilder;
import cn.gxh.property.Lsn13.flatc.Table;

public class Result extends Table {
  public static Result getRootAsResult(ByteBuffer _bb) { return getRootAsResult(_bb, new Result()); }
  public static Result getRootAsResult(ByteBuffer _bb, Result obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public Result __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public String id() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer idAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public String title() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer titleAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public City city(int j) { return city(new City(), j); }
  public City city(City obj, int j) { int o = __offset(8); return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null; }
  public int cityLength() { int o = __offset(8); return o != 0 ? __vector_len(o) : 0; }

  public static int createResult(FlatBufferBuilder builder,
      int id,
      int title,
      int city) {
    builder.startObject(3);
    Result.addCity(builder, city);
    Result.addTitle(builder, title);
    Result.addId(builder, id);
    return Result.endResult(builder);
  }

  public static void startResult(FlatBufferBuilder builder) { builder.startObject(3); }
  public static void addId(FlatBufferBuilder builder, int idOffset) { builder.addOffset(0, idOffset, 0); }
  public static void addTitle(FlatBufferBuilder builder, int titleOffset) { builder.addOffset(1, titleOffset, 0); }
  public static void addCity(FlatBufferBuilder builder, int cityOffset) { builder.addOffset(2, cityOffset, 0); }
  public static int createCityVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startCityVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endResult(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
};

