package cn.gxh.property.Lsn13;// automatically generated, do not modify

import java.nio.*;
import java.lang.*;
import java.util.*;

import cn.gxh.property.Lsn13.flatc.FlatBufferBuilder;
import cn.gxh.property.Lsn13.flatc.Table;

public class ResultList extends Table {
  public static ResultList getRootAsResultList(ByteBuffer _bb) { return getRootAsResultList(_bb, new ResultList()); }
  public static ResultList getRootAsResultList(ByteBuffer _bb, ResultList obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public ResultList __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public Result result(int j) { return result(new Result(), j); }
  public Result result(Result obj, int j) { int o = __offset(4); return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null; }
  public int resultLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }

  public static int createResultList(FlatBufferBuilder builder,
      int result) {
    builder.startObject(1);
    ResultList.addResult(builder, result);
    return ResultList.endResultList(builder);
  }

  public static void startResultList(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addResult(FlatBufferBuilder builder, int resultOffset) { builder.addOffset(0, resultOffset, 0); }
  public static int createResultVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startResultVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endResultList(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishResultListBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
};

