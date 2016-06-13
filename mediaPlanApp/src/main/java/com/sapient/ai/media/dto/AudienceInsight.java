/**
 * 
 */

package com.sapient.ai.media.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author John Free
 */
public class AudienceInsight
{
  private List<String> insights      = new ArrayList<>();
  private String       justification = "";

  /**
   * @return the justification
   */
  public String getJustification()
  {
    return justification;
  }

  /**
   * @param justification the justification to set
   */
  public void setJustification(String justification)
  {
    this.justification = justification;
  }

  /**
   * @return the insights
   */
  public List<String> getInsights()
  {
    return insights;
  }

  public AudienceInsight()
  {
  }

  public void addInsight(String insight)
  {
    insights.add(insight);
  }
}
