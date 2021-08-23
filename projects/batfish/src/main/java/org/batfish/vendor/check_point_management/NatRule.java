package org.batfish.vendor.check_point_management;

import static com.google.common.base.Preconditions.checkArgument;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.batfish.common.util.BatfishObjectMapper;

/** A single nat-rule in a {@link NatRulebase}. */
public final class NatRule extends ManagementObject implements NatRuleOrSection {

  public @Nonnull String getComments() {
    return _comments;
  }

  public boolean isEnabled() {
    return _enabled;
  }

  public @Nonnull NatInstallTarget getInstallOn() {
    return _installOn;
  }

  public @Nonnull NatMethod getMethod() {
    return _method;
  }

  public @Nonnull Uid getOriginalDestination() {
    return _originalDestination;
  }

  public @Nonnull Uid getOriginalService() {
    return _originalService;
  }

  public @Nonnull Uid getOriginalSource() {
    return _originalSource;
  }

  public int getRuleNumber() {
    return _ruleNumber;
  }

  public @Nonnull Uid getTranslatedDestination() {
    return _translatedDestination;
  }

  public @Nonnull Uid getTranslatedService() {
    return _translatedService;
  }

  public @Nonnull Uid getTranslatedSource() {
    return _translatedSource;
  }

  @VisibleForTesting
  NatRule(
      String comments,
      boolean enabled,
      NatInstallTarget installOn,
      NatMethod method,
      Uid originalDestination,
      Uid originalService,
      Uid originalSource,
      int ruleNumber,
      Uid translatedDestination,
      Uid translatedService,
      Uid translatedSource,
      Uid uid) {
    super(uid);
    _comments = comments;
    _enabled = enabled;
    _installOn = installOn;
    _method = method;
    _originalDestination = originalDestination;
    _originalService = originalService;
    _originalSource = originalSource;
    _ruleNumber = ruleNumber;
    _translatedDestination = translatedDestination;
    _translatedService = translatedService;
    _translatedSource = translatedSource;
  }

  @JsonCreator
  private static @Nonnull NatRule create(
      @JsonProperty(PROP_COMMENTS) @Nullable String comments,
      @JsonProperty(PROP_ENABLED) @Nullable Boolean enabled,
      @JsonProperty(PROP_INSTALL_ON) @Nullable JsonNode installOn,
      @JsonProperty(PROP_METHOD) @Nullable NatMethod method,
      @JsonProperty(PROP_ORIGINAL_DESTINATION) @Nullable Uid originalDestination,
      @JsonProperty(PROP_ORIGINAL_SERVICE) @Nullable Uid originalService,
      @JsonProperty(PROP_ORIGINAL_SOURCE) @Nullable Uid originalSource,
      @JsonProperty(PROP_RULE_NUMBER) @Nullable Integer ruleNumber,
      @JsonProperty(PROP_TRANSLATED_DESTINATION) @Nullable Uid translatedDestination,
      @JsonProperty(PROP_TRANSLATED_SERVICE) @Nullable Uid translatedService,
      @JsonProperty(PROP_TRANSLATED_SOURCE) @Nullable Uid translatedSource,
      @JsonProperty(PROP_UID) @Nullable Uid uid) {
    checkArgument(comments != null, "Missing %s", PROP_COMMENTS);
    checkArgument(enabled != null, "Missing %s", PROP_ENABLED);
    checkArgument(installOn != null, "Missing %s", PROP_INSTALL_ON);
    checkArgument(method != null, "Missing %s", PROP_METHOD);
    checkArgument(originalDestination != null, "Missing %s", PROP_ORIGINAL_DESTINATION);
    checkArgument(originalService != null, "Missing %s", PROP_ORIGINAL_SERVICE);
    checkArgument(originalSource != null, "Missing %s", PROP_ORIGINAL_SOURCE);
    checkArgument(ruleNumber != null, "Missing %s", PROP_RULE_NUMBER);
    checkArgument(translatedDestination != null, "Missing %s", PROP_TRANSLATED_DESTINATION);
    checkArgument(translatedService != null, "Missing %s", PROP_TRANSLATED_SERVICE);
    checkArgument(translatedSource != null, "Missing %s", PROP_TRANSLATED_SOURCE);
    checkArgument(uid != null, "Missing %s", PROP_UID);
    return new NatRule(
        comments,
        enabled,
        deserializeInstallOn(installOn),
        method,
        originalDestination,
        originalService,
        originalSource,
        ruleNumber,
        translatedDestination,
        translatedService,
        translatedSource,
        uid);
  }

  private static @Nonnull NatInstallTarget deserializeInstallOn(JsonNode installOn) {
    if (installOn instanceof TextNode) {
      String text = installOn.textValue();
      checkArgument(
          text.equals("All"),
          "Unsupported text value for installation-targets (expected \"All\"): %s",
          text);
      return AllNatInstallTarget.instance();
    } else if (installOn instanceof ArrayNode) {
      List<Uid> targets =
          ImmutableList.copyOf(
              BatfishObjectMapper.ignoreUnknownMapper()
                  .convertValue(installOn, new TypeReference<List<Uid>>() {}));
      return new ListNatInstallTarget(targets);
    } else {
      throw new IllegalArgumentException(
          String.format(
              "Unsupporeted JSON node type for value of %s field: %s",
              PROP_INSTALL_ON, installOn.getClass()));
    }
  }

  @Override
  public boolean equals(Object o) {
    if (!baseEquals(o)) {
      return false;
    }
    NatRule natRule = (NatRule) o;
    return _enabled == natRule._enabled
        && _ruleNumber == natRule._ruleNumber
        && _comments.equals(natRule._comments)
        && _installOn.equals(natRule._installOn)
        && _method == natRule._method
        && _originalDestination.equals(natRule._originalDestination)
        && _originalService.equals(natRule._originalService)
        && _originalSource.equals(natRule._originalSource)
        && _translatedDestination.equals(natRule._translatedDestination)
        && _translatedService.equals(natRule._translatedService)
        && _translatedSource.equals(natRule._translatedSource);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        baseHashcode(),
        _comments,
        _enabled,
        _installOn,
        _method.ordinal(),
        _originalDestination,
        _originalService,
        _originalSource,
        _ruleNumber,
        _translatedDestination,
        _translatedService,
        _translatedSource);
  }

  private final @Nonnull String _comments;
  private final boolean _enabled;
  private final @Nonnull NatInstallTarget _installOn;
  private final @Nonnull NatMethod _method;
  private final @Nonnull Uid _originalDestination;
  private final @Nonnull Uid _originalService;
  private final @Nonnull Uid _originalSource;
  private final int _ruleNumber;
  private final @Nonnull Uid _translatedDestination;
  private final @Nonnull Uid _translatedService;
  private final @Nonnull Uid _translatedSource;

  private static final String PROP_COMMENTS = "comments";
  private static final String PROP_ENABLED = "enabled";
  private static final String PROP_INSTALL_ON = "install-on";
  private static final String PROP_METHOD = "method";
  private static final String PROP_ORIGINAL_DESTINATION = "original-destination";
  private static final String PROP_ORIGINAL_SERVICE = "original-service";
  private static final String PROP_ORIGINAL_SOURCE = "original-source";
  private static final String PROP_RULE_NUMBER = "rule-number";
  private static final String PROP_TRANSLATED_DESTINATION = "translated-destination";
  private static final String PROP_TRANSLATED_SERVICE = "translated-service";
  private static final String PROP_TRANSLATED_SOURCE = "translated-source";
}