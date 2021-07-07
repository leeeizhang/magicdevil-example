package top.magicdevil.example.webapp.sample.bookstore.spark.config;

import java.io.Serializable;

public class ALSConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer maxIter;
    private Integer rank;
    private Integer numBlocks;
    private Double alpha;
    private Double lambda;
    private Double regParam;
    private Integer recNum;

    public Integer getMaxIter() {
        return maxIter;
    }

    public void setMaxIter(Integer maxIter) {
        this.maxIter = maxIter;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getNumBlocks() {
        return numBlocks;
    }

    public void setNumBlocks(Integer numBlocks) {
        this.numBlocks = numBlocks;
    }

    public Double getAlpha() {
        return alpha;
    }

    public void setAlpha(Double alpha) {
        this.alpha = alpha;
    }

    public Double getLambda() {
        return lambda;
    }

    public void setLambda(Double lambda) {
        this.lambda = lambda;
    }

    public Double getRegParam() {
        return regParam;
    }

    public void setRegParam(Double regParam) {
        this.regParam = regParam;
    }

    public Integer getRecNum() {
        return recNum;
    }

    public void setRecNum(Integer recNum) {
        this.recNum = recNum;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alpha == null) ? 0 : alpha.hashCode());
        result = prime * result + ((lambda == null) ? 0 : lambda.hashCode());
        result = prime * result + ((maxIter == null) ? 0 : maxIter.hashCode());
        result = prime * result + ((numBlocks == null) ? 0 : numBlocks.hashCode());
        result = prime * result + ((rank == null) ? 0 : rank.hashCode());
        result = prime * result + ((recNum == null) ? 0 : recNum.hashCode());
        result = prime * result + ((regParam == null) ? 0 : regParam.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ALSConfig other = (ALSConfig) obj;
        if (alpha == null) {
            if (other.alpha != null)
                return false;
        } else if (!alpha.equals(other.alpha))
            return false;
        if (lambda == null) {
            if (other.lambda != null)
                return false;
        } else if (!lambda.equals(other.lambda))
            return false;
        if (maxIter == null) {
            if (other.maxIter != null)
                return false;
        } else if (!maxIter.equals(other.maxIter))
            return false;
        if (numBlocks == null) {
            if (other.numBlocks != null)
                return false;
        } else if (!numBlocks.equals(other.numBlocks))
            return false;
        if (rank == null) {
            if (other.rank != null)
                return false;
        } else if (!rank.equals(other.rank))
            return false;
        if (recNum == null) {
            if (other.recNum != null)
                return false;
        } else if (!recNum.equals(other.recNum))
            return false;
        if (regParam == null) {
            if (other.regParam != null)
                return false;
        } else if (!regParam.equals(other.regParam))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ALSConfig [maxIter=" + maxIter + ", rank=" + rank + ", numBlocks=" + numBlocks
                + ", alpha=" + alpha + ", lambda=" + lambda + ", regParam=" + regParam + ", recNum="
                + recNum + "]";
    }

}
