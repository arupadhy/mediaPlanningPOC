
package com.sapient.ai.media.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sapient.ai.cyc.session.MpConstants;

@JsonIgnoreProperties()
public class AudienceInsightsDto extends BaseDtoItem
{
  Map<String, List<Map<String, String>>> audienceInsights;

  public AudienceInsightsDto()
  {
  }

  public AudienceInsightsDto(Map<String, List<Map<String, String>>> audienceInsights)
  {
    if (audienceInsights != null)
      this.audienceInsights = audienceInsights;
  }

  public AudienceInsightsDto(String error)
  {
    if (error != null)
    {
      setError(error);
      setStatusCode(MpConstants.ERROR_CODE);
    }
  }

  public Map<String, List<Map<String, String>>> getAudienceInsights()
  {
    return audienceInsights;
  }

  public void setAudienceInsights(Map<String, List<Map<String, String>>> audienceInsights)
  {
    this.audienceInsights = audienceInsights;
  }

}
