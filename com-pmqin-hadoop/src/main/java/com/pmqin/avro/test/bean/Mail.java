/**
 * Autogenerated by Avro
 * //Mail接口有1个方法send，参数是Message，Message是一个Avro类，可以序列化和反序列化 
 * DO NOT EDIT DIRECTLY
 */
package com.pmqin.avro.test.bean;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public interface Mail {
  public static final org.apache.avro.Protocol PROTOCOL = org.apache.avro.Protocol.parse("{\"protocol\":\"Mail\",\"namespace\":\"com.pmqin.avro.test.bean\",\"types\":[{\"type\":\"record\",\"name\":\"Message\",\"fields\":[{\"name\":\"to\",\"type\":\"string\"},{\"name\":\"from\",\"type\":\"string\"},{\"name\":\"body\",\"type\":\"string\"}]}],\"messages\":{\"send\":{\"request\":[{\"name\":\"message\",\"type\":\"Message\"}],\"response\":\"string\"}}}");
  /**
   */
  java.lang.CharSequence send(com.pmqin.avro.test.bean.Message message) throws org.apache.avro.AvroRemoteException;

  @SuppressWarnings("all")
  public interface Callback extends Mail {
    public static final org.apache.avro.Protocol PROTOCOL = com.pmqin.avro.test.bean.Mail.PROTOCOL;
    /**
     * @throws java.io.IOException The async call could not be completed.
     */
    void send(com.pmqin.avro.test.bean.Message message, org.apache.avro.ipc.Callback<java.lang.CharSequence> callback) throws java.io.IOException;
  }
}