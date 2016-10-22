package com.myunihome.myxapp.web.business.subject.model;

public class SubjectDto {

    /****** Subject ********/

    private String industryCode;

    private String industryName;

    private String tenantId;

    private String tenantName;

    private Long subjectId;

    private String subjectName;

    private String subjectType;

    private String typeName;

    private String unitName;

    private Integer taxType;

    private String subjectDesc;

    /****** SubjectFund ********/
    
    private Long fundSubjectId;//资金属性表中的ID，用来在界面区分是新配置属性还是修改属性

    private String fundType;

    private String isCash;

    private String useMode;

    private String canSettleAll;

    private String validType;

    private Long usePri;

    private Long refundRate;

    private String canTrans;

    private String canCleanFund;

    private String canDelBook;

    private String calScore;

    private String printMode;

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getTaxType() {
        return taxType;
    }

    public void setTaxType(Integer taxType) {
        this.taxType = taxType;
    }

    public String getSubjectDesc() {
        return subjectDesc;
    }

    public void setSubjectDesc(String subjectDesc) {
        this.subjectDesc = subjectDesc;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getIsCash() {
        return isCash;
    }

    public void setIsCash(String isCash) {
        this.isCash = isCash;
    }

    public String getUseMode() {
        return useMode;
    }

    public void setUseMode(String useMode) {
        this.useMode = useMode;
    }

    public String getCanSettleAll() {
        return canSettleAll;
    }

    public void setCanSettleAll(String canSettleAll) {
        this.canSettleAll = canSettleAll;
    }

    public String getValidType() {
        return validType;
    }

    public void setValidType(String validType) {
        this.validType = validType;
    }

    public Long getUsePri() {
        return usePri;
    }

    public void setUsePri(Long usePri) {
        this.usePri = usePri;
    }

    public Long getRefundRate() {
        return refundRate;
    }

    public void setRefundRate(Long refundRate) {
        this.refundRate = refundRate;
    }

    public String getCanTrans() {
        return canTrans;
    }

    public void setCanTrans(String canTrans) {
        this.canTrans = canTrans;
    }

    public String getCanCleanFund() {
        return canCleanFund;
    }

    public void setCanCleanFund(String canCleanFund) {
        this.canCleanFund = canCleanFund;
    }

    public String getCanDelBook() {
        return canDelBook;
    }

    public void setCanDelBook(String canDelBook) {
        this.canDelBook = canDelBook;
    }

    public String getCalScore() {
        return calScore;
    }

    public void setCalScore(String calScore) {
        this.calScore = calScore;
    }

    public String getPrintMode() {
        return printMode;
    }

    public void setPrintMode(String printMode) {
        this.printMode = printMode;
    }

    public Long getFundSubjectId() {
        return fundSubjectId;
    }

    public void setFundSubjectId(Long fundSubjectId) {
        this.fundSubjectId = fundSubjectId;
    }
}
