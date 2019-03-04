package cn.gxh.property.Lsn13;// automatically generated, do not modify

import java.nio.*;
import java.lang.*;
import java.util.*;


import cn.gxh.property.Lsn13.flatc.FlatBufferBuilder;
import cn.gxh.property.Lsn13.flatc.Table;

public class City extends Table {
  public static City getRootAsCity(ByteBuffer _bb) { return getRootAsCity(_bb, new City()); }
  public static City getRootAsCity(ByteBuffer _bb, City obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public City __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public String id() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer idAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public String title() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer titleAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public Children children(int j) { return children(new Children(), j); }
  public Children children(Children obj, int j) { int o = __offset(8); return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null; }
  public int childrenLength() { int o = __offset(8); return o != 0 ? __vector_len(o) : 0; }

  public static int createCity(FlatBufferBuilder builder,
      int id,
      int title,
      int children) {
    builder.startObject(3);
    City.addChildren(builder, children);
    City.addTitle(builder, title);
    City.addId(builder, id);
    return City.endCity(builder);
  }

  public static void startCity(FlatBufferBuilder builder) { builder.startObject(3); }
  public static void addId(FlatBufferBuilder builder, int idOffset) { builder.addOffset(0, idOffset, 0); }
  public static void addTitle(FlatBufferBuilder builder, int titleOffset) { builder.addOffset(1, titleOffset, 0); }
  public static void addChildren(FlatBufferBuilder builder, int childrenOffset) { builder.addOffset(2, childrenOffset, 0); }
  public static int createChildrenVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startChildrenVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endCity(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
};

