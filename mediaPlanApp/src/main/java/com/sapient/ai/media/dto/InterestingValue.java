/**
 * 
 */

package com.sapient.ai.media.dto;

import java.io.Serializable;

/**
 * @author John Free
 */ 
public class InterestingValue  implements Serializable
{
  private int    index;
  private String insight;
  private double    vertical;

  /**
   * @param index
   * @param insight
   * @param vertical
   * @param justification
   */
  public InterestingValue(String insight, int index, double vertical, String justification)
  {
    super();
    this.index = index;
    this.insight = insight;
    this.vertical = vertical;
    this.justification = justification;
  }


  /**
   * 
   */
  public InterestingValue()
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
   * @param justification the justification to set
   */
  public void setJustification(String justification)
  {
    this.justification = justification;
  }

  /**
   * @return the vertical
   */
  public double getVertical()
  {
    return vertical;
  }


  /**
   * @param vertical the vertical to set
   */
  public void setVertical(double vertical)
  {
    this.vertical = vertical;
  }

  /**
   * @return the index
   */
  public int getIndex()
  {
    return index;
  }


  /**
   * @param index the index to set
   */
  public void setIndex(int index)
  {
    this.index = index;
  }

  /**
   * @return the insight
   */
  public String getInsight()
  {
    return insight;
  }


  /**
   * @param insight the insight to set
   */
  public void setInsight(String insight)
  {
    this.insight = insight;
  }

  private String justification;
}
