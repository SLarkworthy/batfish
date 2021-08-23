package org.batfish.vendor.check_point_management;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import com.google.common.testing.EqualsTester;
import org.apache.commons.lang3.SerializationUtils;
import org.batfish.common.util.BatfishObjectMapper;
import org.batfish.datamodel.Ip;
import org.junit.Test;

/** Test of {@link CpmiVsClusterNetobj}. */
public final class CpmiVsClusterNetobjTest {

  @Test
  public void testJacksonDeserialization() throws JsonProcessingException {
    String input =
        "{"
            + "\"GARBAGE\":0,"
            + "\"type\":\"CpmiVsClusterNetobj\","
            + "\"uid\":\"0\","
            + "\"name\":\"foo\","
            + "\"ipv4-address\":\"0.0.0.0\","
            + "\"cluster-member-names\":[\"m1\"]"
            + "}";
    assertThat(
        BatfishObjectMapper.ignoreUnknownMapper().readValue(input, CpmiVsClusterNetobj.class),
        equalTo(new CpmiVsClusterNetobj(ImmutableList.of("m1"), Ip.ZERO, "foo", Uid.of("0"))));
  }

  @Test
  public void testJavaSerialization() {
    CpmiVsClusterNetobj obj =
        new CpmiVsClusterNetobj(ImmutableList.of(), Ip.ZERO, "foo", Uid.of("0"));
    assertEquals(obj, SerializationUtils.clone(obj));
  }

  @Test
  public void testEquals() {
    CpmiVsClusterNetobj obj =
        new CpmiVsClusterNetobj(ImmutableList.of(), Ip.ZERO, "foo", Uid.of("0"));
    new EqualsTester()
        .addEqualityGroup(
            obj, new CpmiVsClusterNetobj(ImmutableList.of(), Ip.ZERO, "foo", Uid.of("0")))
        .addEqualityGroup(
            new CpmiVsClusterNetobj(ImmutableList.of("m1"), Ip.ZERO, "foo", Uid.of("0")))
        .addEqualityGroup(
            new CpmiVsClusterNetobj(ImmutableList.of(), Ip.parse("0.0.0.1"), "foo", Uid.of("0")))
        .addEqualityGroup(new CpmiVsClusterNetobj(ImmutableList.of(), Ip.ZERO, "bar", Uid.of("0")))
        .addEqualityGroup(new CpmiVsClusterNetobj(ImmutableList.of(), Ip.ZERO, "foo", Uid.of("1")))
        .testEquals();
  }
}