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
public class AudienceInsights implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -5326105283219961378L;
  private List<ContrastingFact>  contrastingFacts = new ArrayList<>();
  private List<InterestingFact>  interestingFacts = new ArrayList<>();
  private List<InterestingValue> values           = new ArrayList<>();
  public void addContrastingFact(ContrastingFact value)
  {
    contrastingFacts.add(value);
  }
  public void addInterestingFact(InterestingFact value)
  {
    interestingFacts.add(value);
  }
  public void addValue(InterestingValue value)
  {
    values.add(value);
  }
  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AudienceInsights other = (AudienceInsights) obj;
    if (contrastingFacts == null)
    {
      if (other.contrastingFacts != null)
        return false;
    } else if (!contrastingFacts.equals(other.contrastingFacts))
      return false;
    if (interestingFacts == null)
    {
      if (other.interestingFacts != null)
        return false;
    } else if (!interestingFacts.equals(other.interestingFacts))
      return false;
    if (values == null)
    {
      if (other.values != null)
        return false;
    } else if (!values.equals(other.values))
      return false;
    return true;
  }
  /**
   * @return the contrastingFacts
   */
  public List<ContrastingFact> getContrastingFacts()
  {
    return contrastingFacts;
  }
  /**
   * @return the interestingFacts
   */
  public List<InterestingFact> getInterestingFacts()
  {
    return interestingFacts;
  }
  /**
   * @return the values
   */
  public List<InterestingValue> getValues()
  {
    return values;
  }
  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((contrastingFacts == null) ? 0 : contrastingFacts.hashCode());
    result = prime * result + ((interestingFacts == null) ? 0 : interestingFacts.hashCode());
    result = prime * result + ((values == null) ? 0 : values.hashCode());
    return result;
  }

  /**
   * @param contrastingFacts the contrastingFacts to set
   */
  public void setContrastingFacts(List<ContrastingFact> contrastingFacts)
  {
    this.contrastingFacts = contrastingFacts;
  }
  /**
   * @param interestingFacts the interestingFacts to set
   */
  public void setInterestingFacts(List<InterestingFact> interestingFacts)
  {
    this.interestingFacts = interestingFacts;
  }
  /**
   * @param values the values to set
   */
  public void setValues(List<InterestingValue> values)
  {
    this.values = values;
  }
}
