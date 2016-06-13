package com.sapient.ai.media.dto;

import java.util.ArrayList;
import java.util.List;

import com.sapient.ai.cyc.session.MpConstants;

public class ProofDto extends BaseDtoItem
{
    List<ProofEntryDtoItem> proofs = new ArrayList<ProofEntryDtoItem>();

    public ProofDto()
    {
    }

    public ProofDto(String error)
    {
        if (error != null)
        {
            setError(error);
            setStatusCode(MpConstants.ERROR_CODE);
        }
    }

    public List<ProofEntryDtoItem> getProofs()
    {
        return proofs;
    }

    public void setProofs(List<ProofEntryDtoItem> proofs)
    {
        this.proofs = proofs;
    }

    public void addProof(ProofEntryDtoItem proof)
    {
        proofs.add(proof);
    }

}
