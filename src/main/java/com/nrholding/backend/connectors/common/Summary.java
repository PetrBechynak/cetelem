package com.nrholding.backend.connectors.common;

/**
 * Created by pbechynak on 29.2.2016.
 */
public class Summary {
    Integer mismatched=0, confirmed=0, rejected=0, skipped=0, other=0, unknown =0;

    public Summary sum(Summary other){
        Summary result = new Summary();
        result.setConfirmed(this.getConfirmed()+other.getConfirmed());
        result.setMismatched(this.getMismatched()+other.getMismatched());
        result.setRejected(this.getRejected()+other.getRejected());
        result.setSkipped(this.getSkipped()+other.getSkipped());
        result.setOther(this.getOther()+other.getOther());
        result.setUnknown(this.getUnknown()+other.getUnknown());
        return result;
    }

    public Integer getMismatched() {
        return mismatched;
    }

    public void setMismatched(Integer mismatched) {
        this.mismatched = mismatched;
    }

    public void setConfirmed(Integer confirmed) {
        this.confirmed = confirmed;
    }

    public void setRejected(Integer rejected) {
        this.rejected = rejected;
    }

    public void setSkipped(Integer skipped) {
        this.skipped = skipped;
    }

    public void setOther(Integer other) {
        this.other = other;
    }

    public void setUnknown(Integer unknown) {
        this.unknown = unknown;
    }

    public void incMismatched() {
        this.mismatched = mismatched+1;
    }

    public Integer getConfirmed() {
        return confirmed;
    }

    public void incConfirmed() {
        this.confirmed = confirmed+1;
    }

    public Integer getRejected() {
        return rejected;
    }

    public void incRejected() {
        this.rejected = rejected+1;
    }

    public Integer getSkipped() {
        return skipped;
    }

    public void incSkipped() {
        this.skipped = skipped+1;
    }

    public Integer getOther() {
        return other;
    }

    public void incOther() {
        this.other = other+1;
    }

    public Integer getUnknown() {
        return unknown;
    }

    public void incUnknown() {
        this.unknown = unknown+1;
    }
}
