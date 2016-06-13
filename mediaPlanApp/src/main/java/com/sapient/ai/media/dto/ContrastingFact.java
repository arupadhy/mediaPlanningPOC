/**
 * 
 */

package com.sapient.ai.media.dto;

import java.io.Serializable;

/**
 * @author John Free
 */
public class ContrastingFact implements Serializable
{
  private String factA;

  /**
   * @param factA
   * @param factB
   * @param justification
   */
  public ContrastingFact(String factA, String factB, String justification)
  {
    super();
    this.factA = factA;
    this.factB = factB;
    this.justification = justification;
  }

  private String factB;

  /**
   * 
   */
  public ContrastingFact()
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

  private String justification;
}
