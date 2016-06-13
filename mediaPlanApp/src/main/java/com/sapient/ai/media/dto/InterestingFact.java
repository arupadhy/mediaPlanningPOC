/**
 * 
 */

package com.sapient.ai.media.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John Free
 */
public class InterestingFact  implements Serializable
{

  private String insight;

  /**
   * @param insight
   * @param justification
   */
  public InterestingFact(String insight, String justification)
  {
    super();
    this.insight = insight;
    this.justification = justification;
  }

  /**
   * 
   */
  public InterestingFact()
  {
    super();
  }

  /**
   * @return the justification
   */
  public String getJustification()
  {
    return justification;
  }

  /**
   * @param justification
   *          the justification to set
   */
  public void setJustification(String justification)
  {
    this.justification = justification;
  }

  /**
   * @return the insight
   */
  public String getInsight()
  {
    return insight;
  }

  /**
   * @param insight
   *          the insight to set
   */
  public void setInsight(String insight)
  {
    this.insight = insight;
  }

  private String justification;
}
