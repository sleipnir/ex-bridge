// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: io/cloudevents/v1/spec.proto

package io.cloudevents.v1.proto;

public interface CloudEventOrBuilder extends
    // @@protoc_insertion_point(interface_extends:io.cloudevents.v1.CloudEvent)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Required Attributes
   * </pre>
   *
   * <code>string id = 1;</code>
   * @return The id.
   */
  java.lang.String getId();
  /**
   * <pre>
   * Required Attributes
   * </pre>
   *
   * <code>string id = 1;</code>
   * @return The bytes for id.
   */
  com.google.protobuf.ByteString
      getIdBytes();

  /**
   * <pre>
   * URI-reference
   * </pre>
   *
   * <code>string source = 2;</code>
   * @return The source.
   */
  java.lang.String getSource();
  /**
   * <pre>
   * URI-reference
   * </pre>
   *
   * <code>string source = 2;</code>
   * @return The bytes for source.
   */
  com.google.protobuf.ByteString
      getSourceBytes();

  /**
   * <code>string spec_version = 3;</code>
   * @return The specVersion.
   */
  java.lang.String getSpecVersion();
  /**
   * <code>string spec_version = 3;</code>
   * @return The bytes for specVersion.
   */
  com.google.protobuf.ByteString
      getSpecVersionBytes();

  /**
   * <code>string type = 4;</code>
   * @return The type.
   */
  java.lang.String getType();
  /**
   * <code>string type = 4;</code>
   * @return The bytes for type.
   */
  com.google.protobuf.ByteString
      getTypeBytes();

  /**
   * <pre>
   * Optional &amp; Extension Attributes
   * </pre>
   *
   * <code>map&lt;string, .io.cloudevents.v1.CloudEvent.CloudEventAttributeValue&gt; attributes = 5;</code>
   */
  int getAttributesCount();
  /**
   * <pre>
   * Optional &amp; Extension Attributes
   * </pre>
   *
   * <code>map&lt;string, .io.cloudevents.v1.CloudEvent.CloudEventAttributeValue&gt; attributes = 5;</code>
   */
  boolean containsAttributes(
      java.lang.String key);
  /**
   * Use {@link #getAttributesMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, io.cloudevents.v1.proto.CloudEvent.CloudEventAttributeValue>
  getAttributes();
  /**
   * <pre>
   * Optional &amp; Extension Attributes
   * </pre>
   *
   * <code>map&lt;string, .io.cloudevents.v1.CloudEvent.CloudEventAttributeValue&gt; attributes = 5;</code>
   */
  java.util.Map<java.lang.String, io.cloudevents.v1.proto.CloudEvent.CloudEventAttributeValue>
  getAttributesMap();
  /**
   * <pre>
   * Optional &amp; Extension Attributes
   * </pre>
   *
   * <code>map&lt;string, .io.cloudevents.v1.CloudEvent.CloudEventAttributeValue&gt; attributes = 5;</code>
   */

  io.cloudevents.v1.proto.CloudEvent.CloudEventAttributeValue getAttributesOrDefault(
      java.lang.String key,
      io.cloudevents.v1.proto.CloudEvent.CloudEventAttributeValue defaultValue);
  /**
   * <pre>
   * Optional &amp; Extension Attributes
   * </pre>
   *
   * <code>map&lt;string, .io.cloudevents.v1.CloudEvent.CloudEventAttributeValue&gt; attributes = 5;</code>
   */

  io.cloudevents.v1.proto.CloudEvent.CloudEventAttributeValue getAttributesOrThrow(
      java.lang.String key);

  /**
   * <code>bytes binary_data = 6;</code>
   * @return Whether the binaryData field is set.
   */
  boolean hasBinaryData();
  /**
   * <code>bytes binary_data = 6;</code>
   * @return The binaryData.
   */
  com.google.protobuf.ByteString getBinaryData();

  /**
   * <code>string text_data = 7;</code>
   * @return Whether the textData field is set.
   */
  boolean hasTextData();
  /**
   * <code>string text_data = 7;</code>
   * @return The textData.
   */
  java.lang.String getTextData();
  /**
   * <code>string text_data = 7;</code>
   * @return The bytes for textData.
   */
  com.google.protobuf.ByteString
      getTextDataBytes();

  /**
   * <code>.google.protobuf.Any proto_data = 8;</code>
   * @return Whether the protoData field is set.
   */
  boolean hasProtoData();
  /**
   * <code>.google.protobuf.Any proto_data = 8;</code>
   * @return The protoData.
   */
  com.google.protobuf.Any getProtoData();
  /**
   * <code>.google.protobuf.Any proto_data = 8;</code>
   */
  com.google.protobuf.AnyOrBuilder getProtoDataOrBuilder();

  public io.cloudevents.v1.proto.CloudEvent.DataCase getDataCase();
}