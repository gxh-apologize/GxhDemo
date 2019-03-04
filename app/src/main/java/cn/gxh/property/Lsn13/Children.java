package cn.gxh.property.Lsn13;// automatically generated, do not modify

import java.nio.*;
import java.lang.*;
import java.util.*;

import cn.gxh.property.Lsn13.flatc.FlatBufferBuilder;
import cn.gxh.property.Lsn13.flatc.Table;

public class Children extends Table {
  public static Children getRootAsChildren(ByteBuffer _bb) { return getRootAsChildren(_bb, new Children()); }
  public static Children getRootAsChildren(ByteBuffer _bb, Children obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public Children __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public String id() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer idAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public String title() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer titleAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public int value() { int o = __offset(8); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public boolean isCore() { int o = __offset(10); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public long code() { int o = __offset(12); return o != 0 ? bb.getLong(o + bb_pos) : 0; }

  public static int createChildren(FlatBufferBuilder builder,
      int id,
      int title,
      int value,
      boolean isCore,
      long code) {
    builder.startObject(5);
    Children.addCode(builder, code);
    Children.addValue(builder, value);
    Children.addTitle(builder, title);
    Children.addId(builder, id);
    Children.addIsCore(builder, isCore);
    return Children.endChildren(builder);
  }

  public static void startChildren(FlatBufferBuilder builder) { builder.startObject(5); }
  public static void addId(FlatBufferBuilder builder, int idOffset) { builder.addOffset(0, idOffset, 0); }
  public static void addTitle(FlatBufferBuilder builder, int titleOffset) { builder.addOffset(1, titleOffset, 0); }
  public static void addValue(FlatBufferBuilder builder, int value) { builder.addInt(2, value, 0); }
  public static void addIsCore(FlatBufferBuilder builder, boolean isCore) { builder.addBoolean(3, isCore, false); }
  public static void addCode(FlatBufferBuilder builder, long code) { builder.addLong(4, code, 0); }
  public static int endChildren(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
};

